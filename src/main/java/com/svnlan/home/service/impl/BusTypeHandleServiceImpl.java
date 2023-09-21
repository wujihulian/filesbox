package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.ImageUtil;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 10:15
 */
@Slf4j
@Service
public class BusTypeHandleServiceImpl implements BusTypeHandleService {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemOptionDao systemOptionDao;

    /**
     * @Description: 图片类型处理
     * @params:  [uploadDTO, isBefore, commonSource]
     * @Return:  void
     * @Modified:
     */
    @Override
    public void doForImage(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource) {
        if (isBefore){
            String fileName;
            try {
                fileName = uploadDTO.getFile().getOriginalFilename();
            } catch (Exception e) {
                log.warn("Did not derive the fileName， fallback to commonSource.getName()");
                fileName = commonSource.getName();
            }
            String suffix = FileUtil.getFileExtension(fileName);
            if (ObjectUtils.isEmpty(suffix) || !Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)){
                // 图片类型不正确
                throw new SvnlanRuntimeException(CodeMessageEnum.userAvatarExt.getCode());
            }
        } else {
            BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeCode(commonSource.getSourceType());
            String busType = busTypeEnum.getBusType();
            Float quality = null;

            String filePath = commonSource.getPath();
            try {
//                LoginUser loginUser = loginUserUtil.getLoginUser();
                Long time1 = System.currentTimeMillis();
                boolean hasMark = false;
                if (!ObjectUtils.isEmpty(uploadDTO)){
                    if (ObjectUtils.isEmpty(uploadDTO.getHasMark())){
                    /*String needMark = systemOptionDao.getSystemConfigByKey("needMark");
                    hasMark = (!ObjectUtils.isEmpty(needMark) && "1".equals(needMark)) ? true : false;*/
                    }else {
                        hasMark = uploadDTO.getHasMark();
                    }
                }

                String serverName = commonSource.getDomain();
                if (ObjectUtils.isEmpty(serverName)){
                    serverName = loginUserUtil.getServerName();
                }
                Long size = 0L;
                //生成缩略图
                List<String> thumbList = ImageUtil.createThumb(filePath, busType, "", null,
                        serverName, stringRedisTemplate, quality, hasMark);
                LogUtil.info("thumbTime23333333: " + (System.currentTimeMillis() - time1) + ", " + filePath);
                if (!CollectionUtils.isEmpty(thumbList)){
                    File thumbFile = null;
                    for (String thumbPath :  thumbList){
                        thumbFile = new File(thumbPath);
                        if (thumbFile.exists()){
                            size = size + thumbFile.length();
                        }
                    }
                    if (size > 0){
                        commonSource.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
                        commonSource.setThumbSize(commonSource.getThumbSize() + size);
                    }
                }
            } catch (Exception e){
                LogUtil.error(e, "缩略图生成失败");
            }
        }
    }

    /**
     * @Description: 云盘
     * @params:  [uploadDTO, isBefore, commonSource]
     * @Return:  void
     * @Modified:
     */
    @Override
    public void doForCloud(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource) {


        if (!isBefore){
            if (uploadDTO.getIgnoreFileSize() > 0 && commonSource.getSize() > uploadDTO.getIgnoreFileSize() * 1024 * 1024 * 1024 ){
                LogUtil.error("云盘文件超限，" + JsonUtils.beanToJson(commonSource));
                throw new SvnlanRuntimeException(CodeMessageEnum.ignoreFileSizeTips.getCode());
            }

            String sourceSuffix = commonSource.getFileType();
            /*Map<String, Object> map = new HashMap<>(2);

            map.put("busId", commonSource.getFileID());
            map.put("busType", busType);
            if ("doc".equals(sourceSuffix)
                    || "docx".equals(sourceSuffix)
                    || "ppt".equals(sourceSuffix)
                    || "pptx".equals(sourceSuffix)
                    || "pdf".equals(sourceSuffix)){
                ConvertDTO convertDTO = JSON.parseObject(JsonUtils.beanToJson(map), ConvertDTO.class);
                // 转码放外面
                //convertService.doConvert(convertDTO, commonSource);

                /*if ("doc".equals(sourceSuffix) || "docx".equals(sourceSuffix)){
将PDF、PPT、WORD、EXCEL原文件转成图片  homework
                    ConvertToPDFMsgDTO convertToPDFMsgDTO = new ConvertToPDFMsgDTO(RandomUtil.getuuid(), DateDUtil.getCurrentTime(), commonSource.getCommonSourceId());
                    convertToPDFMsgDTO.setNoImage(true);
                    kafkaTemplate.send("convertToJPGKafka006", JsonUtils.beanToJson(convertToPDFMsgDTO));
                }* /
                return;
            }*/
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(sourceSuffix)){
                this.doForImage(uploadDTO, isBefore, commonSource);
            } /*else {
                ConvertDTO convertDTO = JSON.parseObject(JsonUtils.beanToJson(map), ConvertDTO.class);
                // 转码放外面
               // convertService.doConvert(convertDTO, commonSource);
            }*/
        }
    }

    @Override
    public void doForExtraImage(UploadDTO uploadDTO, CommonSource commonSource) {
        String filePath = commonSource.getPath();

        try {
            Long time1 = System.currentTimeMillis();
            String[] sizeStringArr = uploadDTO.getThumbSize().split(",");
            if (sizeStringArr.length != 2){
                return;
            }
            boolean hasMark = false;
            if (ObjectUtils.isEmpty(uploadDTO.getHasMark())){
                /*String needMark = systemOptionDao.getSystemConfigByKey("needMark");
                hasMark = (!ObjectUtils.isEmpty(needMark) && "1".equals(needMark)) ? true : false;*/
            }else {
                hasMark = uploadDTO.getHasMark();
            }

            Long size = 0L;

//            LoginUser loginUser = loginUserUtil.getLoginUser();
            int[][] sizeArr = new int[][] {{Integer.parseInt(sizeStringArr[0]), Integer.parseInt(sizeStringArr[1])}};
            List<String> thumbList = ImageUtil.createThumb(filePath, uploadDTO.getBusType(), null, sizeArr, loginUserUtil.getServerName()
                    , stringRedisTemplate, hasMark);
            if (!CollectionUtils.isEmpty(thumbList)){
                File thumbFile = null;
                for (String thumbPath :  thumbList){
                    thumbFile = new File(thumbPath);
                    if (thumbFile.exists()){
                        size = size + thumbFile.length();
                    }
                }
                if (size > 0){
                    commonSource.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
                    commonSource.setThumbSize(commonSource.getThumbSize() + size);
                }
            }
            LogUtil.info("thumbTime: " + (System.currentTimeMillis() - time1));
        } catch (Exception e){
            LogUtil.error(e, "缩略图生成失败");
        }
    }

    @Override
    public void checkForCloud(CheckFileDTO checkFileDTO, CommonSource commonSource) {
        if (checkFileDTO.getIgnoreFileSize() > 0 && commonSource.getSize() > checkFileDTO.getIgnoreFileSize() * 1024 * 1024 * 1024 ){
            LogUtil.error("云盘文件超限，" + JsonUtils.beanToJson(commonSource));
            throw new SvnlanRuntimeException(CodeMessageEnum.ignoreFileSizeTips.getCode());
        }
    }

    @Override
    public void doForWareAndAttachment(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource) {

        // 已改
        String pre = PropertiesUtil.getUpConfig(uploadDTO.getBusType() + ".savePath");
        String filename = commonSource.getName();
        String suffix = FileUtil.getFileExtension(filename);
        String path = commonSource.getPath();
        String firstPath = FileUtil.getFirstStorageDevicePath(path);
        String picPath =  firstPath + "/mu/images/"
                + commonSource.getPath().replace("." + suffix, ".jpg").replace(firstPath + pre, "");


//        if (isBefore){
        //设置截图字段
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(suffix)) {
            commonSource.setThumb(picPath);
        }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)) {

            commonSource.setThumb(path.replace(firstPath + "/doc/", firstPath + "/common/doc/")
                    .replace(firstPath + "/attachment/", firstPath + "/common/attachment/")
                    .replace(firstPath + "/private/", firstPath + "/common/")
            );
        }
        //生成截图
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(suffix)) {
            if (!ObjectUtils.isEmpty(commonSource.getCheckMerge()) && commonSource.getCheckMerge() ){
                // 判断视频是否异步合并
            }else {
                VideoUtil.getVideoPic(path, picPath, commonSource.getResolution());
            }
        }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)) {
            this.doForImage(uploadDTO, isBefore, commonSource);
        }else if(Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(suffix)){
            boolean checkPic = VideoUtil.getAudioPic(path, picPath);
            if (checkPic){
                commonSource.setThumb(picPath);
            }
        }
//        }
        File thumbFile = new File(picPath);
        if (thumbFile.exists()){
            commonSource.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
            commonSource.setThumbSize(commonSource.getThumbSize() + thumbFile.length());
        }
    }
}
