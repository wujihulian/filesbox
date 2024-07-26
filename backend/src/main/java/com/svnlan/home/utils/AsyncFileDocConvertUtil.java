package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.utils.office.LibreOfficeDUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.vo.ConvertToPDFMsgDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PropertiesUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/29 13:22
 */
@Component
public class AsyncFileDocConvertUtil {
    @Resource
    BusTypeHandleService busTypeHandleService;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${office.libreOfficeVersion}")
    private String libreOfficeVersion;

    @Async(value = "asyncTaskExecutor")
    public void asyncFileDocConvert(String toSuffix, CommonSource commonSource, HomeExplorerVO disk, String fileName, CommonSource parentSource
    , LoginUser loginUser ,String taskID, String defaultPath, String serviceName) {

        List<CommonSource> convertList = new ArrayList<>();
        String finalTopPath = defaultPath + PropertiesUtil.getUpConfig("cloud.savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("doc2Convert 创建目录失败, path:" + finalFolderPath);
            }
        }
        String namePath = RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + commonSource.getUserID();

        //文件后缀
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath + namePath ;

        String prefix = commonSource.getFileType() + " to " + toSuffix + " : ";
        Integer targetType = commonSource.getTargetType();

        String redisKey = GlobalConfig.progress_key_convert_doc_file +  taskID;
        List<String> filePathList = new ArrayList<>();
        //最终文件
        switch (toSuffix){
            case "doc":
            case "docx":
                PdfDUtil.pdf2Doc(prefix, commonSource.getPath(), finalFilePath + "." + toSuffix, "docx".equals(toSuffix),
                        redisKey, true,
                    stringRedisTemplate);
                filePathList.add(finalFilePath + "." + toSuffix);
                break;
            case "jpg":
            case "png":
                int progressRate = 100;
                String convertPath = null;
                if (!"pdf".equals(commonSource.getFileType())){
                    progressRate = 50;
                    String pdfPath = commonSource.getPath().substring(commonSource.getPath().lastIndexOf("/") + 1).replace("." + commonSource.getFileType(), ".pdf");
                    LibreOfficeDUtil.libreOfficeCommand(prefix, this.libreOfficeVersion, "pdf",
                            commonSource.getPath(), finalFilePath);
                    convertPath = finalFilePath + "/" + pdfPath;
                }else {
                    convertPath = commonSource.getPath();
                }
                PdfDUtil.pdfToPngOrJpgConverter(prefix, convertPath, finalFilePath + "_",  true, filePathList,
                        redisKey, true, toSuffix, stringRedisTemplate, progressRate);
                if (!"pdf".equals(commonSource.getFileType())){
                    File f = new File(convertPath);
                    if (f.exists()){
                        try {
                            f.delete();
                        }catch (Exception e){
                        }
                    }
                }
                break;
            case "pdf":
                File folderPdf = new File(finalFilePath);
                //创建目录
                if (!folderPdf.exists()) {
                    if (!folderPdf.mkdirs()) {
                        LogUtil.error("doc2Convert 创建目录失败, path:" + finalFolderPath);
                    }
                }
                String pdfPath = commonSource.getPath().substring(commonSource.getPath().lastIndexOf("/") + 1).replace("." + commonSource.getFileType(), ".pdf");
                LibreOfficeDUtil.libreOfficeCommand(prefix, this.libreOfficeVersion, "pdf",
                        commonSource.getPath(), finalFilePath);
                filePathList.add(finalFilePath + "/" + pdfPath);
                stringRedisTemplate.opsForValue().set(redisKey, 99+"", 60, TimeUnit.SECONDS);
                break;
            default:
                break;
        }

        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setHasMark(false);
        CommonSource fileSource = null;
        File file = null;
        if (!CollectionUtils.isEmpty(filePathList)){
            int i = 0;
            for (String path : filePathList){
                fileSource = new CommonSource();
                fileSource.setGroupID(disk.getGroupID());
                fileSource.setName(fileName );
                if ((i > 0)){
                    fileSource.setName(fileName.replace("." + toSuffix, "") + "(" + i + ")." + toSuffix);
                }
                fileSource.setParentID(parentSource.getSourceID());
                fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
                fileSource.setTargetType(targetType);
                fileSource.setUserID(loginUser.getUserID());
                fileSource.setFileType(toSuffix);
                file = new File(path);
                fileSource.setSize(file.length());
                fileSource.setHashMd5("");
                fileSource.setNeedHashMd5(1);
                fileSource.setPath(path);
                fileSource.setDomain(serviceName);
                if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(toSuffix)){
                    fileSource.setSourceType(2);
                    busTypeHandleService.doForImage(uploadDTO, false, fileSource);
                }
                fileSource.setTenantId(commonSource.getTenantId());
                fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);

                convertList.add(fileSource);
                i ++;

                fileOptionTool.updateMemory(fileSource);
            }
        }
        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(100), 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_convert_doc_file + taskID, "1", 20, TimeUnit.MINUTES);
    }
}
