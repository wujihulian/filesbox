package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.VideoCutDto;
import com.svnlan.home.service.VideoImagesService;
import com.svnlan.home.utils.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/8 9:17
 */
@Service
public class VideoImagesServiceImpl implements VideoImagesService {

    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ImageToVideoUtil imageToVideoUtil;

    @Override
    public Map<String, Object> imagesToVideo(VideoCommonDto checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getName()) || ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo())
                || ObjectUtils.isEmpty(checkFileDTO.getCutList()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String fileType = checkFileDTO.getName().substring(checkFileDTO.getName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("imagesToVideo 视频类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<VideoCutDto> cutList = checkFileDTO.getCutList();
        for (VideoCutDto dto : cutList){
            if (ObjectUtils.isEmpty(dto) || ObjectUtils.isEmpty(dto.getpUrl()) || ObjectUtils.isEmpty(dto.getFileName())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            String ft = dto.getFileName().substring(dto.getFileName().lastIndexOf(".") + 1 );
            if (!Arrays.asList("jpg","png","jpeg","JPG","PNG","JPEG").contains(ft)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            if (ObjectUtils.isEmpty(dto.getDuration())){
                dto.setDuration(1.5);
            }
            if (ObjectUtils.isEmpty(dto.getLength())){
                dto.setLength(3.0);
            }
            if (ObjectUtils.isEmpty(dto.getBackground())){
                dto.setBackground("#000000");
            }
            if (ObjectUtils.isEmpty(dto.getTransition())){
                dto.setTransition("rectcrop");
            }
        }
        File file = new File("/Cheerfulness.mp3");
        if (!file.exists()) {
            Mp3InitUtils mp3Init = new Mp3InitUtils();
        }

        //最终文件目录路径
        String finalTopPath = fileOptionTool.getPropertiesFilePathAll("cloud.savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
            }
        }
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "." + fileType;

        String redisKey = GlobalConfig.async_key_convert_img_video + checkFileDTO.getTaskID();
        String redisProgressKey = GlobalConfig.progress_key_convert_img_video + checkFileDTO.getTaskID();

        String serverUrl = HttpUtil.getRequestRootUrl(null);
        stringRedisTemplate.opsForValue().set(redisKey, "0", 1, TimeUnit.HOURS);

        imageToVideoUtil.execImageToVideo(finalFilePath, checkFileDTO, redisKey, fileType, serverUrl, loginUser, redisProgressKey);


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
        String proKey = GlobalConfig.async_key_convert_img_video+"_pro" + checkFileDTO.getTaskID();
        String proString = stringRedisTemplate.opsForValue().get(proKey);

        int progress = 0;
        if (!ObjectUtils.isEmpty(proString)){
            progress = Integer.valueOf(proString);
        }
        resultMap.put("taskID", checkFileDTO.getTaskID());
        resultMap.put("status", 0);
        resultMap.put("taskID", checkFileDTO.getTaskID());
        String key = GlobalConfig.async_key_convert_img_video + checkFileDTO.getTaskID();
        String denyString = stringRedisTemplate.opsForValue().get(key);
        // 进度
        String progressKey = GlobalConfig.progress_key_convert_img_video +  checkFileDTO.getTaskID();
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("progress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            int p = Integer.valueOf(progressString);
            if (p >= 90){
                if (p == 100){
                    resultMap.put("status", 1);
                }
                resultMap.put("progress", p);
            }else {
                if (p <= progress){
                    int p1 = progress > 90 ? progress : progress + 1;
                    resultMap.put("progress", p1);
                    stringRedisTemplate.opsForValue().set(proKey, "" + p1, 1, TimeUnit.HOURS);
                }else {
                    resultMap.put("progress", p);
                    stringRedisTemplate.opsForValue().set(proKey, "" + p, 1, TimeUnit.HOURS);
                }
            }
        }else {
            resultMap.put("progress", progress);
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
            stringRedisTemplate.delete(proKey);
        }else if ("2".equals(denyString)) {
            resultMap.put("status", 2);
            resultMap.put("progress", 0);
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(progressKey);
            stringRedisTemplate.delete(proKey);
        }
        return true;
    }
}
