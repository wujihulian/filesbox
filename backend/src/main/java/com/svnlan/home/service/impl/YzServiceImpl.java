package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.YzOfficDto;
import com.svnlan.home.service.YzService;
import com.svnlan.home.utils.AsyncUtil;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/10 16:31
 */
@Service
public class YzServiceImpl implements YzService {

    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;
    @Resource
    AsyncUtil asyncUtil;
    @Resource
    StorageService storageService;
    @Value("${yz.fileId.prefix}")
    private String yzFileIdPreFix;


    @Override
    public void yzCallback(YzOfficDto officDto, MultipartFile file){

        if (file == null || ObjectUtils.isEmpty(officDto) || ObjectUtils.isEmpty(officDto.getFileId())) {
            LogUtil.error("yzCallback file is null officDto=" + JsonUtils.beanToJson(officDto));
            return ;
        }
        Long userID = 0L;
        if (!CollectionUtils.isEmpty(officDto.getUserId())){
            userID = Long.parseLong(officDto.getUserId().get(0));
        }
        String fileIDUUID = null;
        if (officDto.getFileId().startsWith(yzFileIdPreFix)){
            fileIDUUID = officDto.getFileId().replaceFirst(yzFileIdPreFix, "");
        }else {
            fileIDUUID = officDto.getFileId();
        }
        String uuid = "";
        String prefix = "";
        Long sourceID = null;
        int index = fileIDUUID.indexOf(yzFileIdPreFix);
        if (index >= 0){
            sourceID = Long.parseLong(fileIDUUID.substring(0, index));
            uuid = fileIDUUID.substring(index + 1, fileIDUUID.length());
            prefix = yzFileIdPreFix;
        }else {
            sourceID = Long.parseLong(fileIDUUID);
        }

        String key = GlobalConfig.yzwo_file_edit_key + sourceID + prefix + uuid;
        String value = stringRedisTemplate.opsForValue().get(key);
        int count = 0;
        if (!ObjectUtils.isEmpty(value)){
            count = Integer.valueOf(value);
        }else {
            stringRedisTemplate.opsForValue().set(key, "1", 5, TimeUnit.HOURS);
        }


        CommonSource commonSource = fileOptionTool.getSourceInfo(sourceID);
        if (ObjectUtils.isEmpty(commonSource)) {
            LogUtil.error("yzCallback commonSource is null officDto=" + JsonUtils.beanToJson(officDto));
            return ;
        }

        LogUtil.info("yzCallback count==========" + count + "，officDto=" + JsonUtils.beanToJson(officDto));

        if (ObjectUtils.isEmpty(userID) || userID <= 0){
            String userKey = GlobalConfig.yzwo_file_edit_user_key + uuid;
            String userMapString = stringRedisTemplate.opsForValue().get(userKey);
            LogUtil.info("yzCallback count==========" + count + "，userMapString=" + userMapString);
            Map<String, Object> userMap = new HashMap<>(1);
            if (!ObjectUtils.isEmpty(userMapString)){
                try {
                    userMap = JsonUtils.jsonToMap(userMapString);
                }catch (Exception e){
                    LogUtil.error(e, "yzCallback join error");
                }
            }
            if (!ObjectUtils.isEmpty(userMap) && userMap.containsKey("userID")){
                userID = Long.parseLong(userMap.get("userID").toString());
            }else {
                userID = commonSource.getUserID();
            }
        }
        commonSource.setUserID(userID);
        if (count <= 0){
            LogUtil.info("yzCallback newFileSave ===== commonSource=" + JsonUtils.beanToJson(commonSource));
            // 新版本
            newFileSave(commonSource, file, userID, sourceID);
        }else {
            LogUtil.info("yzCallback thisFileSave ===== commonSource=" + JsonUtils.beanToJson(commonSource));
            // 老版本
            thisFileSave(commonSource, file, userID, sourceID);
        }


        return ;
    }


    public void newFileSave(CommonSource commonSource, MultipartFile file, Long userID, Long sourceID) {

        Long fileID = commonSource.getFileID();
        Long sizeOld = commonSource.getSize();

        // 已改
        String finalTopPath = fileOptionTool.getPropertiesFilePathAll("cloud.savePath", commonSource.getTenantId());
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();

        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }

        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + commonSource.getUserID() + "." + commonSource.getFileType();

        Long size = file.getSize();
        //最终文件
        File finalFile = new File(finalFilePath);
        FileInputStream fis = null;
        try {
            FileUtil.writeFile(finalFile, file.getBytes());
            fis = new FileInputStream(finalFile);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage() , " yzCallback 上传文件失败");
            return ;
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (Exception e){
                    LogUtil.error(e);
                }
            }
        }

        commonSource.setSize(size);
        //commonSource.setHashMd5(serverChecksum);
        commonSource.setPath(finalFilePath);

        LogUtil.info("editIoSourceDetail------addCommonSource---------commonSource=" + JsonUtils.beanToJson(commonSource));
        commonSource.setIsEdit("1");
        fileOptionTool.addCommonSource(commonSource.getUserID(), commonSource, EventEnum.edit);
        // 添加历史记录
        asyncUtil.asyncAddSourceHistory(commonSource, userID, fileID, sizeOld);

        asyncUtil.asyncUpdateFileMd5(finalFilePath);

        String domain = HttpUtil.getRequestRootUrl(null);
        // 预览
        commonSource.setDomain(domain);
        convertUtil.yongZhongPre(commonSource, true);

    }
    public void thisFileSave(CommonSource commonSource, MultipartFile file, Long userID, Long sourceID) {

        String finalFilePath = commonSource.getPath();
        Long size = file.getSize();
        //最终文件
        File finalFile = new File(finalFilePath);
        String serverChecksum;
        FileInputStream fis = null;
        try {
            FileUtil.writeFile(finalFile, file.getBytes());
            fis = new FileInputStream(finalFile);
            serverChecksum = DigestUtils.md5DigestAsHex(fis);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage() , " yzCallback 上传文件失败");
            return ;
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (Exception e){
                    LogUtil.error(e);
                }
            }
        }

        String domain = HttpUtil.getRequestRootUrl(null);
        // 预览
        commonSource.setDomain(domain);
        convertUtil.yongZhongPre(commonSource, true);
        // 修改size
        if (!ObjectUtils.isEmpty(commonSource.getFileThumbSize()) && commonSource.getFileThumbSize() > 0 && size > commonSource.getFileThumbSize()){
            size = size - commonSource.getFileThumbSize();
        }
        IOSource sourceUpdate = new IOSource();
        sourceUpdate.setSize(size);
        sourceUpdate.setModifyUser(userID);
        sourceUpdate.setId(sourceID);
        sourceUpdate.setFileId(commonSource.getFileID());
        sourceUpdate.setHashMd5(serverChecksum);


        // 修改文件source修改人、file文件大小
        try {
            ioSourceDao.updateFileSize(sourceUpdate);
        }catch (Exception e){
            LogUtil.error(e, " yzCallback updateFileSize error");
        }

        try {
            ioSourceDao.updateSourceModifyUser(sourceUpdate);
        }catch (Exception e){
            LogUtil.error(e, " yzCallback updateSourceModifyUser error");
        }
    }
}
