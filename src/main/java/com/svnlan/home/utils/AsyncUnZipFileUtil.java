package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.CompressFileDto;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.utils.HttpUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/20 13:19
 */
@Component
public class AsyncUnZipFileUtil {

    @Resource
    ConvertUtil convertUtil;


    @Async(value = "asyncTaskExecutor")
    public void asyncUnZipFile(CommonSource commonSource, CheckFileDTO checkFileDTO, LoginUser loginUser, StringRedisTemplate stringRedisTemplate
            , String finalFolderPath, List<ChangeSourceVo> fileList, SystemLogTool systemLogTool, FileOptionTool fileOptionTool, BusTypeHandleService busTypeHandleService
    , UploadDTO uploadDTO, CommonSource parentSource, List<String> sourceNameList, Integer targetType, String serverUrl) {

        /** 解压整个文件 */
        CompressFileDto dto = CompressFileUtil.unzipFilePassword(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID()
                , checkFileDTO.getPassword(), checkFileDTO.getTaskID(), true, stringRedisTemplate );
        if (ObjectUtils.isEmpty(dto) || !dto.getSuccess()){
            if (!ObjectUtils.isEmpty(checkFileDTO.getPassword())){
                throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
            }else {
                throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
            }
        }
        List<CommonSource> convertList = new ArrayList<>();



        LogUtil.info("unZip asyncUnZipFile parentSource=" + JsonUtils.beanToJson(parentSource));
        LogUtil.info("unZip asyncUnZipFile fileList=" + JsonUtils.beanToJson(fileList));

        // 返回前端
        List<Long> sourceIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(parentSource) && !CollectionUtils.isEmpty(fileList)){
            List<ChangeSourceVo> sourceFileVos = new ArrayList<>();
            List<ChangeSourceVo> sourceDirVos = new ArrayList<>();
            for (ChangeSourceVo entry: fileList) {
                if (entry.getIsFolder().intValue() == 1){
                    if (entry.getFilePath().indexOf("//") >= 0){
                        entry.setFilePath(entry.getFilePath().replaceAll("//","/"));
                    }
                    sourceDirVos.add(entry);
                }else {
                    sourceFileVos.add(entry);
                }
            }

            Map<String, CommonSource> parentMap = new HashMap<>(1);
            parentMap.put(finalFolderPath, parentSource);
            // 文件夹
            if (!CollectionUtils.isEmpty(sourceDirVos)){
                //finalFolderPath
                sourceDirVos = sourceDirVos.stream().sorted(Comparator.comparing(ChangeSourceVo::getPathLength)).collect(Collectors.toList());
                for (ChangeSourceVo changeVo : sourceDirVos){
                    String parentPath = changeVo.getFilePath().substring(0, changeVo.getFilePath().indexOf(changeVo.getName()));
                    if (parentMap.containsKey(parentPath)) {
                        CommonSource pSource = parentMap.get(parentPath);
                        CommonSource dir = new CommonSource();
                        if (parentPath.equals(finalFolderPath)) {
                            dir.setName(fileOptionTool.checkRepeatName(changeVo.getName(), changeVo.getName(), sourceNameList, 1));
                        }else {
                            dir.setName(changeVo.getName());
                        }
                        dir.setParentID(pSource.getSourceID());
                        dir.setParentLevel(pSource.getParentLevel() + pSource.getSourceID() + ",");
                        dir.setTargetType(targetType);
                        dir.setFileType("");
                        dir.setSize(0L);
                        fileOptionTool.addIoSourceDetail(dir, loginUser.getUserID(), 0L, EventEnum.mkdir);
                        if (parentPath.equals(finalFolderPath)){
                            sourceIds.add(dir.getSourceID());
                        }
                        parentMap.put(changeVo.getFilePath(), dir);
                    }
                }
            }

            LogUtil.info("unZip asyncUnZipFile parentMap=" + JsonUtils.beanToJson(parentMap));
            LogUtil.info("unZip asyncUnZipFile sourceDirVos=" + JsonUtils.beanToJson(sourceDirVos));
            LogUtil.info("unZip asyncUnZipFile sourceFileVos=" + JsonUtils.beanToJson(sourceFileVos));

            // 文件
            if (!CollectionUtils.isEmpty(sourceFileVos)){
                for (ChangeSourceVo changeVo : sourceFileVos){
                    String parentPath ;
                    if (changeVo.getFilePath().equals(changeVo.getName())){
                        parentPath = finalFolderPath;
                    }else {
                        parentPath = finalFolderPath + changeVo.getFilePath().substring(0, changeVo.getFilePath().indexOf(changeVo.getName()));
                    }
                    LogUtil.info("unZip asyncUnZipFile check=" + parentMap.containsKey(parentPath) + " parentPath=" + parentPath);
                    if (parentMap.containsKey(parentPath)) {
                        CommonSource pSource = parentMap.get(parentPath);
                        CommonSource fileSource = new CommonSource();
                        if (parentPath.equals(finalFolderPath)) {
                            fileSource.setName(fileOptionTool.checkRepeatName(changeVo.getName(), changeVo.getName(), changeVo.getFileType(), sourceNameList, 1));
                        }else {
                            fileSource.setName(changeVo.getName());
                        }
                        fileSource.setParentID(pSource.getSourceID());
                        fileSource.setParentLevel(pSource.getParentLevel() + pSource.getSourceID() + ",");
                        fileSource.setTargetType(targetType);
                        fileSource.setFileType(changeVo.getFileType());
                        fileSource.setSize(changeVo.getSize());
                        fileSource.setHashMd5(changeVo.getHashMd5());
                        fileSource.setPath(changeVo.getPath());
                        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
                        if (parentPath.equals(finalFolderPath)){
                            sourceIds.add(fileSource.getSourceID());
                        }

                        if (ObjectUtils.isEmpty(changeVo.getHashMd5())) {
                            fileSource.setNeedHashMd5(1);
                        }
                        LogUtil.info("unZip asyncUnZipFile fileSource=" + JsonUtils.beanToJson(fileSource));
                        if (Arrays.asList(GlobalConfig.VIDEO_AUDIO_TYPE_CONVERT).contains(changeVo.getFileType())){
                            // 转码、图片加缩略图
                            convertList.add(fileSource);
                        }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(changeVo.getFileType())){
                            // "cloud"
                            fileSource.setSourceType(2);
                            busTypeHandleService.doForImage(uploadDTO, false, fileSource);
                            if (!ObjectUtils.isEmpty(changeVo.getHashMd5())) {
                                convertList.add(fileSource);
                            }
                        } else {
                            if (!ObjectUtils.isEmpty(changeVo.getHashMd5())) {
                                convertList.add(fileSource);
                            }
                        }
                    }
                }
            }
        }

        checkFileDTO.setSourceIds(sourceIds);

        stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);

        LogUtil.info("解压缩：转码：convertList=" + JsonUtils.beanToJson(convertList));
        // 视频转码
        if (!CollectionUtils.isEmpty(convertList)) {
            for (CommonSource source : convertList) {
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("unZip");

                source.setDomain(serverUrl);
                convertDTO.setDomain(serverUrl);
                convertUtil.doConvertMain(convertDTO, source);
            }
        }

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "upload");
        reMap.put("pathName", commonSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, null);

    }
}
