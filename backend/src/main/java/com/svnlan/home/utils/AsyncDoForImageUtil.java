package com.svnlan.home.utils;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;


@Component
public class AsyncDoForImageUtil {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     *  判断是否需要旋转（竖屏拍照上传时）
     * @param uploadDTO
     * @param commonSource
     */
    @Async(value = "asyncTaskExecutor")
    public void asyncDoForImage(UploadDTO uploadDTO, CommonSource commonSource) {

        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeCode(commonSource.getSourceType());
        String busType = busTypeEnum.getBusType();
        Float quality = null;

        String orgFilePath = commonSource.getPath();
        int p = orgFilePath.lastIndexOf("/") + 1;
        String dirPath = orgFilePath.substring(0, p);
        String orgFileName = orgFilePath.substring(p, orgFilePath.length());
        String newFilePath = dirPath + "rotate_" + orgFileName;

        String filePath = ImageUtil.RotateImg(orgFilePath, newFilePath, true);

        if (filePath.equals(newFilePath)){

        }

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
                // 图片上加水印目前不需要这个功能
                serverName = "filesbox";
            }
            Long size = 0L;
            //生成缩略图
            List<String> thumbList = ImageUtil.createThumb(filePath, busType, "", null,
                    serverName, stringRedisTemplate, quality, hasMark);
            LogUtil.info("asyncDoForImage thumbTime23333333: " + (System.currentTimeMillis() - time1) + ", " + filePath);
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
            LogUtil.error(e, "asyncDoForImage 缩略图生成失败");
        }
    }
}
