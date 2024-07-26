package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/13 15:36
 */
@Component
public class AsyncCutImgUtil {

    @Resource
    StorageService storageService;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    ConvertUtil convertUtil;

    @Resource(name = "threadPoolDefault")
    private ThreadPoolTaskExecutor executor;

    @Async(value = "asyncTaskExecutor")
    public List AsyncExecVideoShearList(VideoCommonDto videoCommonDto, String defaultFirstPath){

        videoCommonDto.setTaskID(ObjectUtils.isEmpty(videoCommonDto.getTaskID()) ? RandomUtil.getuuid() : videoCommonDto.getTaskID());

        String videoPathOrg;
        if (videoCommonDto.getpUrl().indexOf(GlobalConfig.private_replace_key) >= 0){
            videoPathOrg = videoCommonDto.getpUrl().replace(GlobalConfig.private_replace_key, "/private/") + videoCommonDto.getFileName();
        }else {
            videoPathOrg = GlobalConfig.default_disk_path_pre + "/private" + videoCommonDto.getpUrl() + videoCommonDto.getFileName();
        }
        LogUtil.info("AsyncExecVideoShearList videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));

        String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String nameDir = videoPathOrg.substring(videoPathOrg.lastIndexOf("/") + 1);
        String tempPath = videoPathOrg.substring(0,videoPathOrg.lastIndexOf(".") ) + "/" ;
        String tempPathFinish = tempPath.replace(firstPath + "/private/", defaultFirstPath + "/common/down_temp/")  ;
        File destinationFolder = new File(tempPathFinish);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        // = 1 则重新生成
        if (ObjectUtils.isEmpty(videoCommonDto.getRegeneration()) || videoCommonDto.getRegeneration().intValue() != 1) {

            String firstCutPath = tempPathFinish + nameDir.replace(".", "_") + "_cut_frame_1.jpg";
            File firstCutFile = new File(firstCutPath);
            if (firstCutFile.exists()) {
                // 已存在截图文件
                return null;
            }
        }
        // ffmpeg 默认 24 帧
        String frameInterval = ObjectUtils.isEmpty(videoCommonDto.getFrame()) ? "24" : String.valueOf(videoCommonDto.getFrame());
        //
        Long lengthTime = videoCommonDto.getLength() * 1000;

        // 一次切两分钟
        long eachTime = 120 * 1000;
        long num;
        num = lengthTime / eachTime;
        if (lengthTime % eachTime != 0){
            num ++;
        }
        String endN = DateUtil.msecToTime(eachTime);
        LogUtil.info("AsyncExecVideoShearList lengthTime=" + lengthTime + "，eachTime=" + eachTime);

        //使用Future方式执行多任务
        //生成一个集合
        List<Future> futures = new ArrayList<>();
        // 按分取
        LogUtil.info("AsyncExecVideoShearList tempPathFinish=" + tempPathFinish );
        for (int i = 0 ; i < num; i++){
            // 从视频中截取多张图片
            String tempImgPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_frame_" + (i+1) +  "_%02d.jpg";
            long timeParam = eachTime * i;
            Future<?> future = executor.submit(() -> {
                String beginN = DateUtil.msecToTime(timeParam);

                VideoGetUtil.covPicBatch(videoPathOrg, tempImgPath, beginN, endN, frameInterval);
            });
            futures.add(future);
        }

        if (!CollectionUtils.isEmpty(futures)){
            try {
                //查询任务执行的结果
                for (Future<?> future : futures) {
                    while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                        if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。

                            LogUtil.info("AsyncExecVideoShearList 任务i=" + future.get() + "获取完成!" + new Date());
                            break;//当前future获取结果完毕，跳出while
                        } else {
                            Thread.sleep(1);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                        }
                    }
                }
            }catch (Exception e){
                LogUtil.error(e, "AsyncExecVideoShearList error ");
            }
        }
        return null;
    }

    @Async(value = "asyncTaskExecutor")
    public void AsyncExecVideoImgThumb(CommonSource commonSource, StringRedisTemplate stringRedisTemplate, boolean check){
        String filePath = commonSource.getPath();
        File f = new File(filePath);
        if (!f.exists()) {
            LogUtil.error("AsyncExecVideoImgThumb filePath is null fileID=" + commonSource.getFileID());
            if (check) {
                stringRedisTemplate.opsForHash().put(GlobalConfig.temp_img_video_key, commonSource.getSourceID() + "", "1");
            }
            return;
        }

        try {
            if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType())){
                String value = ioFileDao.getFileUrlValue(commonSource.getFileID());
                Map<String, Object> map = null;
                if (!ObjectUtils.isEmpty(value)) {
                    map = JsonUtils.jsonToMap(value);
                }
                if (!ObjectUtils.isEmpty(map) && map.containsKey("thumb") && !ObjectUtils.isEmpty(map.get("thumb"))) {
                    commonSource.setThumb(map.get("thumb").toString());
                    // 需要重新查出 thumb 的路径
                    UploadDTO uploadDTO = new UploadDTO();
                    uploadDTO.setBusType("cloud");

                    String resolution = null;
                    if (map.containsKey("resolution") && !ObjectUtils.isEmpty(map.get("resolution"))){
                        resolution = map.get("resolution").toString();
                    }
                    VideoUtil.getVideoPic(commonSource.getPath(), commonSource.getThumb(), resolution);
                }
            }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType())){
                ImageUtil.createThumb(filePath, "cloud", "", null,
                        "" , stringRedisTemplate, null, false);
            }else if (Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType())){
               convertUtil.convertCameraToImgCommon("", commonSource);
            }

        }catch (Exception e){
            LogUtil.error(e, "AsyncExecVideoImgThumb 失败");
        }
        if (check) {
            stringRedisTemplate.opsForHash().put(GlobalConfig.temp_img_video_key, commonSource.getSourceID()+ "", "1");
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void AsyncExecImageThumb(String filePath, StringRedisTemplate stringRedisTemplate){
        File f = new File(filePath);
        if (!f.exists()) {
            LogUtil.error("AsyncExecImageThumb filePath is null ");
            return;
        }
        LogUtil.info("AsyncExecImageThumb生成缩略图 filePath=" + filePath);
        ImageUtil.createThumb(filePath, "cloud", "", null,
                "" , stringRedisTemplate, null, false);
    }
}
