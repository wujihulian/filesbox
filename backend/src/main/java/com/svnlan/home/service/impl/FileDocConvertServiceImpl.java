package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.FileDocConvertService;
import com.svnlan.home.utils.AsyncFileDocConvertUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/28 13:57
 */
@Service
public class FileDocConvertServiceImpl implements FileDocConvertService {

    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    AsyncFileDocConvertUtil asyncFileDocConvertUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    StorageService storageService;
    @Resource
    LoginUserUtil loginUserUtil;

    @Override
    public Map<String, Object> doc2Convert(CheckFileDTO checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0 || ObjectUtils.isEmpty(checkFileDTO.getSuffix())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) && ObjectUtils.isEmpty(checkFileDTO.getPathTo())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long sourceID = checkFileDTO.getSourceID();
        CommonSource commonSource = fileOptionTool.getSourceInfo(sourceID);
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList("doc","docx","jpg","jpeg","pdf","png","ppt","pptx").contains(commonSource.getFileType().toLowerCase())){
            LogUtil.error("doc2Convert 转换类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList("doc","docx","jpg","jpeg","pdf","png").contains(checkFileDTO.getSuffix().toLowerCase())){
            LogUtil.error("doc2Convert 转换类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (Arrays.asList("jpg","jpeg","png").contains(commonSource.getFileType().toLowerCase()) && !"pdf".equals(checkFileDTO.getSuffix().toLowerCase())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String toSuffix = checkFileDTO.getSuffix().toLowerCase();
        if (commonSource.getFileType().toLowerCase().equals(toSuffix)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String defaultPath = storageService.getDefaultStorageDevicePath();
        /** 获取企业云盘 */
        HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), checkFileDTO.getSourceID());
        fileOptionTool.checkMemory(disk, commonSource.getSize());

        Integer targetType = commonSource.getTargetType();
        List<String> sourceNameList = null;
        CommonSource parentSource = null;
        if (!ObjectUtils.isEmpty(checkFileDTO.getPathTo()) ){
            sourceNameList = ioSourceDao.getSourceNameList(commonSource.getParentID());
            parentSource = new CommonSource();
            parentSource.setName(fileOptionTool.checkRepeatName(checkFileDTO.getPathTo(), checkFileDTO.getPathTo(), sourceNameList, 1));
            parentSource.setParentID(commonSource.getParentID());
            parentSource.setParentLevel(commonSource.getParentLevel());
            parentSource.setTargetType(targetType);
            parentSource.setFileType("");
            parentSource.setSize(0L);
            fileOptionTool.addIoSourceDetail(parentSource, loginUser.getUserID(), 0L, EventEnum.mkdir);

        }else if(!ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) && checkFileDTO.getSourceIDTo() > 0){
            parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
            // 查询文件夹下的文件、解析是否要重命名
            sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
            targetType = parentSource.getTargetType();
        }else {
            LogUtil.error("doc2Convert 目标目录不存在 checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        String fileNameDir = commonSource.getName().substring(0,commonSource.getName().lastIndexOf("."));
        // 文件名
        String fileName = fileNameDir + "." + toSuffix;
        fileNameDir = fileOptionTool.checkRepeatName(fileNameDir, fileNameDir, sourceNameList, 1);

        // 转图片则创建文件夹
        if (Arrays.asList("jpg","jpeg","png").contains(checkFileDTO.getSuffix().toLowerCase())){
            CommonSource dirParentSource = parentSource;
            parentSource = new CommonSource();
            parentSource.setName(fileNameDir);
            parentSource.setParentID(dirParentSource.getSourceID());
            parentSource.setParentLevel(dirParentSource.getParentLevel() + dirParentSource.getSourceID() + ",");
            parentSource.setTargetType(targetType);
            parentSource.setFileType("");
            parentSource.setSize(0L);
            fileOptionTool.addIoSourceDetail(parentSource, loginUser.getUserID(), 0L, EventEnum.mkdir);
        }else {
            // 新创建的目录下添加文件不需要查重
            fileName = fileOptionTool.checkRepeatName(fileName, fileName, toSuffix, sourceNameList, 1);
        }
        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_convert_doc_file + checkFileDTO.getTaskID(), "0", 1, TimeUnit.HOURS);
        asyncFileDocConvertUtil.asyncFileDocConvert(toSuffix, commonSource, disk, fileName, parentSource, loginUser, checkFileDTO.getTaskID() ,defaultPath, loginUserUtil.getServerName());

        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("taskID", checkFileDTO.getTaskID());
        reMap.put("status", 0);
        reMap.put("progress", 0);

        return reMap;
    }


    @Override
    public boolean taskAction(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getTaskID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        resultMap.put("status", 0);
        resultMap.put("taskID", checkFileDTO.getTaskID());
        String key = GlobalConfig.async_key_convert_doc_file + checkFileDTO.getTaskID();
        String denyString = stringRedisTemplate.opsForValue().get(key);
        // 进度
        String progressKey = GlobalConfig.progress_key_convert_doc_file +  checkFileDTO.getTaskID();
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("progress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("progress", progressString);
        }


        LogUtil.info("convertTaskAction key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
        if (ObjectUtils.isEmpty(denyString)){
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            return true;
        }

        if ("1".equals(denyString)) {
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(progressKey);
        }
        return true;
    }

}
