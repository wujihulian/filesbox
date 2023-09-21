package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.VideoCutDto;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.service.VideoGetService;
import com.svnlan.home.utils.*;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/17 11:29
 */
@Service
public class VideoGetServiceImpl implements VideoGetService {

    @Resource
    AsyncUtil asyncUtil;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    StorageService storageService;

    @Resource
    AsyncCutImgUtil asyncCutImgUtil;
    @Resource
    BusTypeHandleService busTypeHandleService;

    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource(name = "threadPoolDefault")
    private ThreadPoolTaskExecutor executor;

    public String getOrgPath(String url, String fileName){
        if (url.indexOf(GlobalConfig.private_replace_key) >= 0){
            return url.replace(GlobalConfig.private_replace_key, "/private/") + fileName;
        }else {
            return GlobalConfig.default_disk_path_pre + "/private" + url + fileName;
        }
    }

    @Override
    public List getVideoShearListAll(VideoCommonDto videoCommonDto){
        if (ObjectUtils.isEmpty(videoCommonDto.getpUrl()) || ObjectUtils.isEmpty(videoCommonDto.getFileName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // length 单位：秒
        if (ObjectUtils.isEmpty(videoCommonDto.getLength()) || videoCommonDto.getLength() <= 0 || ObjectUtils.isEmpty(videoCommonDto.getNum())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> list = new ArrayList();

        videoCommonDto.setTaskID(ObjectUtils.isEmpty(videoCommonDto.getTaskID()) ? RandomUtil.getuuid() : videoCommonDto.getTaskID());

        String videoPathOrg = getOrgPath(videoCommonDto.getpUrl(), videoCommonDto.getFileName());

        LogUtil.info("getVideoShearListAll videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String nameDir = videoPathOrg.substring(videoPathOrg.lastIndexOf("/") + 1);
        String tempPath = videoPathOrg.substring(0,videoPathOrg.lastIndexOf(".") ) + "/" ;

        String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);
        String defaultFirstPath = storageService.getDefaultStorageDevicePath();

        String tempPathFinish = tempPath.replace(firstPath + "/private/", defaultFirstPath + "/common/down_temp/")  ;
        File destinationFolder = new File(tempPathFinish);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }


        String firstCutPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_first_1.jpg";
        File firstCutFile = new File(firstCutPath);
        boolean isExist = false;
        if (firstCutFile.exists()) {
            // 已存在截图文件
            isExist = true;
        }
        Boolean first = (!ObjectUtils.isEmpty(videoCommonDto.getIsFirst()) && 1 == videoCommonDto.getIsFirst()) ? true : false;

        if (!first){
            return getVideoShearList(videoCommonDto);
        }


        // covPicBatch(String tempVideoPath, String tempImgPath, String beginN)
        // 毫秒
        Long lengthTime = videoCommonDto.getLength() * 1000;
        int num = videoCommonDto.getNum();
        Long eachTime = lengthTime / num;
        LogUtil.info("getVideoShearListAll lengthTime=" + lengthTime + "，eachTime=" + eachTime);

        //使用Future方式执行多任务
        //生成一个集合
        List<Future> futures = new ArrayList<>();

        if (first){

            // 按分取
            LogUtil.info("getVideoShearListAll tempPathFinish=" + tempPathFinish );
            for (int i = 0 ; i < num; i++){
                // 从视频中截取多张图片
                String tempImgPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_first_" + (i+1) + ".jpg";
                if (!isExist) {
                    LogUtil.info("cutList tempImgPath=" + tempImgPath + "， beginTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS) );
                    long timeParam = eachTime * i;
                    Future<?> future = executor.submit(() -> {
                        String beginN = DateDUtil.msecToTime(timeParam);
                        Boolean check = VideoGetUtil.covPicBatch(videoPathOrg, tempImgPath, beginN);
                    });
                    LogUtil.info("cutList tempImgPath=" + tempImgPath + "， endTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS) );
                    futures.add(future);
                }
                list.add(tempImgPath);
            }

            if (!CollectionUtils.isEmpty(futures)){
                try {
                    //查询任务执行的结果
                    for (Future<?> future : futures) {
                        while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                            if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。

                                LogUtil.info("getVideoShearListAll 任务i=" + future.get() + "获取完成!" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS));
                                break;//当前future获取结果完毕，跳出while
                            } else {
                                Thread.sleep(1);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                            }
                        }
                    }
                }catch (Exception e){
                    LogUtil.error(e, "getVideoShearListAll error ");
                }
            }
        }

        //路径缓存, 供定时任务删除使用
        stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                tempPathFinish, System.currentTimeMillis() + 86400000);
        asyncCutImgUtil.AsyncExecVideoShearList(videoCommonDto);


        String downloadKey = FileUtil.getVideoImgDownloadKey(tempPathFinish);
        List<String> vList = new ArrayList<>();
        for (String path : list){
            String name = path.substring(path.lastIndexOf("/") + 1);
            vList.add("/api/disk/video/img/"+name+"?key=" + downloadKey);
        }
        return vList;
    }

    @Override
    public String getVideoShearImg(HttpServletResponse response, VideoCommonDto videoCommonDto, String passedFileName, Map<String, Object> resultMap){

        if (ObjectUtils.isEmpty(passedFileName) || ObjectUtils.isEmpty(videoCommonDto.getKey())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String tempPathFinish = fileOptionTool.getAttachmentToken3(videoCommonDto.getKey());
        String videoPathOrg = "";
        if (!ObjectUtils.isEmpty(videoCommonDto.getShowPreview()) && 1 == videoCommonDto.getShowPreview()){
            videoPathOrg = tempPathFinish ;
        }else if (!ObjectUtils.isEmpty(videoCommonDto.getFileName())){
            videoPathOrg = tempPathFinish + videoCommonDto.getFileName();
        }else {
            videoPathOrg = tempPathFinish + passedFileName;
        }
        String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);
        File file = null;
        if (!ObjectUtils.isEmpty(videoCommonDto.getNameSuffix()) && videoPathOrg.indexOf(firstPath + "/private/cloud") >= 0){
            String suffix = videoPathOrg.substring(videoPathOrg.lastIndexOf(".") + 1);
            String videoPathOrgThumb = videoPathOrg.replace("." + suffix, "!" + videoCommonDto.getNameSuffix() + "." + suffix)
                    .replace(firstPath + "/private/cloud", firstPath + "/common/cloud");

            file = new File(videoPathOrgThumb);
            if (!file.exists()){
                LogUtil.info("getVideoShearImg img is no exists  videoPathOrgThumb=" + videoPathOrgThumb);
                file = new File(videoPathOrg);
            }else {
                videoPathOrg = videoPathOrgThumb;
                LogUtil.info("getVideoShearImg img is exists  videoPathOrgThumb=" + videoPathOrgThumb + "， videoPathOrg=" + videoPathOrg + "， fileSize=" + file.length());
            }
        }else {
            file = new File(videoPathOrg);
        }

        if (!file.exists()){
            LogUtil.error("getVideoShearImg videoPathOrg=" + videoPathOrg);
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String cdnPath = "";
        if (ObjectUtils.isEmpty(cdnDomain)) {
            cdnPath = HttpUtil.getRequestRootUrl(null) + videoPathOrg;
        } else {
            cdnPath = "//" + cdnDomain + videoPathOrg;
        }
        resultMap.put("filePath", cdnPath);
        resultMap.put("fileSize", file.length());
        resultMap.put("videoPathOrg", videoPathOrg);

        return videoPathOrg;
    }

    @Override
    public boolean checkImgCut(VideoCommonDto videoCommonDto){
        if (ObjectUtils.isEmpty(videoCommonDto.getpUrl()) || ObjectUtils.isEmpty(videoCommonDto.getFileName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // l
        String videoPathOrg  = getOrgPath(videoCommonDto.getpUrl(), videoCommonDto.getFileName());

        LogUtil.info("checkImgCut videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);
        String defaultFirstPath = storageService.getDefaultStorageDevicePath();

        String tempPath = videoPathOrg.substring(0,videoPathOrg.lastIndexOf(".") ) + "/" ;
        String tempPathFinish = tempPath.replace(firstPath + "/private/", defaultFirstPath + "/common/down_temp/")  ;
        File destinationFolder = new File(tempPathFinish);
        if (!destinationFolder.exists()) {
            return false;
        }


        if (!ObjectUtils.isEmpty(videoCommonDto.getCheckName())){
            String firstCutPath = tempPathFinish + videoCommonDto.getCheckName();
            File firstCutFile = new File(firstCutPath);
            boolean isExist = false;
            if (firstCutFile.exists()) {
                // 已存在截图文件
                isExist = true;
            }
            return isExist;
        }

        String nameDir = videoPathOrg.substring(videoPathOrg.lastIndexOf("/") + 1);
        Long lengthTime = videoCommonDto.getLength() * 1000;

        // 一次切两分钟
        long eachTime = 120 * 1000;
        long num;
        num = lengthTime / eachTime;
        if (lengthTime % eachTime != 0){
            num ++;
        }

        String endPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_frame_" + num +  "_1.jpg";
        File endPathFile = new File(endPath);
        if (endPathFile.exists()) {
            // 已存在截图文件
            return true;
        }

        endPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_frame_" + num +  "_01.jpg";
        endPathFile = new File(endPath);
        if (endPathFile.exists()) {
            // 已存在截图文件
            return true;
        }
        return false;
    }

    @Override
    public List getVideoShearList(VideoCommonDto videoCommonDto){

        if (ObjectUtils.isEmpty(videoCommonDto.getpUrl()) || ObjectUtils.isEmpty(videoCommonDto.getFileName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(videoCommonDto.getLength()) || videoCommonDto.getLength() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List list = new ArrayList();

        videoCommonDto.setTaskID(ObjectUtils.isEmpty(videoCommonDto.getTaskID()) ? RandomUtil.getuuid() : videoCommonDto.getTaskID());

        String videoPathOrg = getOrgPath(videoCommonDto.getpUrl(), videoCommonDto.getFileName()) ;

        LogUtil.info("getVideoShearList videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String nameDir = videoPathOrg.substring(videoPathOrg.lastIndexOf("/") + 1);
        String tempPath = videoPathOrg.substring(0,videoPathOrg.lastIndexOf(".") ) + "/" ;

        String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);
        String defaultFirstPath = storageService.getDefaultStorageDevicePath();
        // taskID 防止多人剪辑的情况
        String tempTaskPath = tempPath.replace(firstPath + "/private/", defaultFirstPath + "/common/down_temp/")
                + this.getTaskIDUrl(videoCommonDto.getTaskID()) ;



        /** 防止每次取图被覆盖，用时间类型加时间作为文件夹*/
        String tempPathFinish = tempTaskPath + this.getTimeTypePath(videoCommonDto);
        File destinationFolder = new File(tempPathFinish);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        // 从视频中截取多张图片
        String tempImgPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_%02d.jpg";

        Double begin = videoCommonDto.getBeginTime();
        Double end = videoCommonDto.getEndTime();
        if (ObjectUtils.isEmpty(begin) || begin < 0){
            begin = 0.0;
        }
        if (ObjectUtils.isEmpty(end) || end < begin){
            end = begin + 1;
        }

        // ffmpeg 默认 24 帧
        int frame = ObjectUtils.isEmpty(videoCommonDto.getFrame()) ? 24 : videoCommonDto.getFrame();

        // 间隔多少取图
        int frameInterval = 1;

        Integer beginN = 0;
        Integer endN = 1;

        // /** 1 分 2 秒 3 帧 */
        if ("1".equals(videoCommonDto.getTimeType())){
            frameInterval = frame * 60;
            beginN = (int)Math.ceil(begin * frameInterval);
            endN = (int)Math.ceil(end * frameInterval);
        }else if("3".equals(videoCommonDto.getTimeType())){
            beginN = begin.intValue();
            endN = end.intValue();
        }else {
            frameInterval = frame;
            beginN = (int)Math.ceil(begin * frameInterval);
            endN = (int)Math.ceil(end * frameInterval);
        }

        LogUtil.info("getVideoShearList tempImgPath=" + tempImgPath );
        Boolean check = VideoGetUtil.covPicBatch( videoPathOrg,  tempImgPath, beginN, endN, frameInterval);

        if (!check){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        int num = 1;
        if (endN > beginN) {
            double n = (endN - beginN);
            num = (int) Math.ceil(n / frameInterval);
        }
        LogUtil.info("getVideoShearList num=" + num + "，beginN="  + beginN + "，endN="  + endN+ "，frameInterval="  + frameInterval);
        for (int i = 1; i <= num ; i ++){
            list.add(String.format(tempImgPath, i));
        }

        //路径缓存, 供定时任务删除使用
        stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                tempPathFinish, System.currentTimeMillis() + 86400000);

        // 异步删除文件或者文件夹 已解析在cutVideo中
        //stringRedisTemplate.opsForValue().set(GlobalConfig.video_edit_getVideoShearList + videoCommonDto.getTaskID(), tempPathFinish, 5, TimeUnit.HOURS);

        return list;
    }

    private String getTimeTypePath(VideoCommonDto videoCommonDto){
        return videoCommonDto.getTimeType() + "_" + videoCommonDto.getBeginTime() + "_" + videoCommonDto.getEndTime() + "/";
    }

    /**
     * 视频剪辑  （剪切、拆分）(拆分的转码放controller,剪切的转码放这个方法内因需要合并视频)
     * @param videoCommonDto
     * @param loginUser
     * @return
     */
    @Override
    public Map<String, Object> cutVideo(VideoCommonDto videoCommonDto, LoginUser loginUser){

        if (ObjectUtils.isEmpty(videoCommonDto.getpUrl()) || ObjectUtils.isEmpty(videoCommonDto.getFileName())
                || ObjectUtils.isEmpty(videoCommonDto.getTaskID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (CollectionUtils.isEmpty(videoCommonDto.getCutList())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        /** 1 剪切 2 拆分 */
        if (!Arrays.asList("1","2").contains(videoCommonDto.getCutType())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        String defaultFirstPath = storageService.getDefaultStorageDevicePath();
        // 原始文件路径
        String videoPathOrg  = getOrgPath(videoCommonDto.getpUrl(), videoCommonDto.getFileName());

        LogUtil.info("cutVideo videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        Map<String, Object> reMap = new HashMap<>(1);
        String suffix = videoPathOrg.substring(videoPathOrg.lastIndexOf(".") + 1 );
        videoCommonDto.setSuffix(suffix);

        //最终文件目录路径
        String finalTopPath = defaultFirstPath + PropertiesUtil.getUpConfig("cloud.savePath");
        //基础路径+日期路径
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
                + "_" + loginUser.getUserID() + "." + suffix;


        // 从视频中剪切
        List<String> cutList = null;
        if (videoCommonDto.getCutList().size() > 1){

            String nameDir = videoPathOrg.substring(videoPathOrg.lastIndexOf("/") + 1);
            String tempPath = videoPathOrg.substring(0,videoPathOrg.lastIndexOf(".") ) + "/" ;
            String tempPathFinish = "";
            String tempCutPath = null;
            /** 1 剪切 2 拆分 */
            if ("1".equals(videoCommonDto.getCutType())){
                // taskID 防止多人剪辑的情况

                String firstPath = FileUtil.getFirstStorageDevicePath(videoPathOrg);
                tempPathFinish = tempPath.replace(firstPath + "/private/", defaultFirstPath +"/common/down_temp/") + this.getTaskIDUrl(videoCommonDto.getTaskID()) ;
                File destinationFolder = new File(tempPathFinish);
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }
                //路径缓存, 供定时任务删除使用
                /*stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                        tempPathFinish, System.currentTimeMillis() + 86400000);*/

                tempCutPath = tempPathFinish + nameDir.replace(".", "_")+"_cut_%02d." + suffix;

                reMap.put("tempDirPath", tempPathFinish);
                reMap.put("cutPath", finalFilePath);
            }else {
                tempCutPath = finalFilePath.replace("." + suffix, "")+"_%02d." + suffix;
                reMap.put("cutPath", tempCutPath);
            }
            List<VideoCutDto> cutListParam = videoCommonDto.getCutList();
            LogUtil.info("cutVideo suffix=" + suffix + " tempImgPath=" + tempCutPath );
            cutList = VideoGetUtil.covCutBatch(videoPathOrg, tempCutPath, cutListParam, videoCommonDto, executor);
            reMap.put("cutDtoList", cutListParam);

            // 剪切多视频需要合并 根据tempPathFinish判断是否需要合并
            if (!CollectionUtils.isEmpty(cutList) && !ObjectUtils.isEmpty(tempPathFinish)) {
                asyncUtil.cutVideoMerge(cutList, finalFilePath, suffix, tempPathFinish, videoCommonDto.getConvertList(), videoCommonDto.getOtherType()
                ,fileOptionTool, loginUser.getUserID());
            }

        }else {
            List<VideoCutDto> cutListParam = videoCommonDto.getCutList();
            reMap.put("cutPath", finalFilePath);
            // 只有一个
            cutList = VideoGetUtil.covCutBatch(videoPathOrg, finalFilePath, cutListParam, videoCommonDto, executor);
            reMap.put("cutDtoList", cutListParam);
        }
        reMap.put("cutList", cutList);
        reMap.put("success", CollectionUtils.isEmpty(cutList) ? false : true);
        reMap.put("cutType", videoCommonDto.getCutType());

        if ("1".equals(videoCommonDto.getCutType())){
            LogUtil.info("cutVideo cutVideoSave reMap=" + JsonUtils.beanToJson(reMap));
        }else {
            LogUtil.info("cutVideo splitVideoSave reMap=" + JsonUtils.beanToJson(reMap));
        }
        return reMap;
    }

    @Override
    public Map<String, Object> cutVideoSave(VideoCommonDto checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getName()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (CollectionUtils.isEmpty(checkFileDTO.getCutList())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        for (VideoCutDto dto : checkFileDTO.getCutList()){
            if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getDuration())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }
        String serverUrl = HttpUtil.getRequestRootUrl(null);
        int size = checkFileDTO.getCutList().size();
        String fileType = checkFileDTO.getFileName().substring(checkFileDTO.getFileName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoSave 视频编辑类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();
        String fileName = checkFileDTO.getName();
        /** 操作剪辑 */
        checkFileDTO.setCutType("1");


        checkFileDTO.setOtherType("cutVideo");

        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(fileName, fileName, fileType, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(fileType);
        fileSource.setSize(0L);
        fileSource.setHashMd5("");
        fileSource.setPath("");
        fileSource.setDomain(serverUrl);
        // 需要获取md5并修改
        fileSource.setNeedHashMd5(1);
        List<CommonSource> convertList = new ArrayList<>();

        if (size > 1) {
            convertList.add(fileSource);
            checkFileDTO.setConvertList(convertList);
        }
        Map<String, Object> cutMap = this.cutVideo(checkFileDTO, loginUser);
        if (ObjectUtils.isEmpty(cutMap)){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        if (!cutMap.containsKey("success") || "false".equals(cutMap.get("success").toString())){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        if (size > 1){
            return cutMap;
        }

        if (cutMap.containsKey("cutPath")) {
            String cutPath = cutMap.get("cutPath").toString();
            fileSource.setPath(cutPath);
            try {
                File f = new File(cutPath);
                if (f.exists()){
                    fileSource.setSize(f.length());
                }
            }catch (Exception e){

            }
        }

        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
        // 需要获取md5并修改
        fileSource.setNeedHashMd5(1);
        convertList.add(fileSource);
        cutMap.put("convertList", convertList);
        // 修改file的path

        return cutMap;
    }

    @Override
    public Map<String, Object> splitVideoSave(VideoCommonDto checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (CollectionUtils.isEmpty(checkFileDTO.getCutList())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        for (VideoCutDto dto : checkFileDTO.getCutList()){
            if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getDuration())
                    || ObjectUtils.isEmpty(dto.getName())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }
        String fileType = checkFileDTO.getFileName().substring(checkFileDTO.getFileName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoSave 视频编辑类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();

        /** 操作剪辑 */
        checkFileDTO.setCutType("2");
        Map<String, Object> cutMap = this.cutVideo(checkFileDTO, loginUser);
        if (ObjectUtils.isEmpty(cutMap)){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        if (!cutMap.containsKey("success") || "false".equals(cutMap.get("success").toString())){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        List<VideoCutDto> cutList = ObjUtil.objectToList(cutMap.get("cutDtoList"), VideoCutDto.class);
         List<CommonSource> convertList = new ArrayList<>();
        LogUtil.info("splitVideoSave addCommonSource beginTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS));
        for (VideoCutDto cutDto : cutList) {
            CommonSource fileSource = new CommonSource();
            fileSource.setName(fileOptionTool.checkRepeatName(cutDto.getName(), cutDto.getName(), fileType, sourceNameList, 1));
            fileSource.setParentID(parentSource.getSourceID());
            fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
            fileSource.setTargetType(targetType);
            fileSource.setFileType(fileType);
            fileSource.setSize(0L);
            fileSource.setHashMd5("");
            fileSource.setPath(cutDto.getPath());
            fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
            // 需要获取md5并修改
            fileSource.setNeedHashMd5(1);
            convertList.add(fileSource);
        }
        LogUtil.info("splitVideoSave addCommonSource endTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS));
        cutMap.put("convertList",convertList);
        return cutMap;
    }


    @Override
    public Map<String, Object> mergeVideoSave(VideoCommonDto checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) || ObjectUtils.isEmpty(checkFileDTO.getName())
                || ObjectUtils.isEmpty(checkFileDTO.getResolution()) || ObjectUtils.isEmpty(checkFileDTO.getConvertSuffix())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (CollectionUtils.isEmpty(checkFileDTO.getCutList())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> cutList = new ArrayList<>();
        for (VideoCutDto dto : checkFileDTO.getCutList()){
            if (ObjectUtils.isEmpty(dto.getpUrl()) || ObjectUtils.isEmpty(dto.getFileName())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            dto.setVideoPathOrg( getOrgPath(dto.getpUrl(), dto.getFileName()));
            File file = new File(dto.getVideoPathOrg());
            if (!file.exists()){
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
            }
            cutList.add(dto.getVideoPathOrg());
        }
        String fileType = checkFileDTO.getConvertSuffix();
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoSave 视频编辑类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();

        //最终文件目录路径
        String finalTopPath = fileOptionTool.getPropertiesFilePathAll("cloud.savePath");
        //基础路径+日期路径
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
                + "_" + loginUser.getUserID() + "." + checkFileDTO.getConvertSuffix();


        String serverUrl = HttpUtil.getRequestRootUrl(null);
        List<CommonSource> convertList = new ArrayList<>();
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(checkFileDTO.getName(), checkFileDTO.getName(), fileType, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(fileType);
        fileSource.setSize(0L);
        fileSource.setHashMd5("");
        fileSource.setDomain(serverUrl);
        fileSource.setPath(finalFilePath);
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
        // 需要获取md5并修改
        fileSource.setNeedHashMd5(1);
        convertList.add(fileSource);
        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("sourceID", fileSource.getSourceID());
        reMap.put("fileType", fileSource.getFileType());
        reMap.put("name", fileSource.getName());

        // 异步合并 并转码
        VideoCommonDto videoCommonDto = new VideoCommonDto();
        videoCommonDto.setTaskID(checkFileDTO.getTaskID());
        videoCommonDto.setFinalFilePath(finalFilePath);
        videoCommonDto.setOpType("3");
        videoCommonDto.setResolution(checkFileDTO.getResolution());
        videoCommonDto.setServerUrl(serverUrl);
        // 转码
        asyncUtil.cutVideoMerge(cutList, finalFilePath, fileType, "", convertList, "mergeVideo", null, 0L);

        return reMap;
    }

    @Override
    public Map<String, Object> convertVideoSave(VideoCommonDto checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl()) || ObjectUtils.isEmpty(checkFileDTO.getName())
                || ObjectUtils.isEmpty(checkFileDTO.getResolution()) || ObjectUtils.isEmpty(checkFileDTO.getFrameRate())
                || ObjectUtils.isEmpty(checkFileDTO.getConvertSuffix())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(checkFileDTO.getConvertSuffix())){
            LogUtil.error("convertVideoSave 转换格式错误 checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String fileType = checkFileDTO.getConvertSuffix();
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoSave 视频编辑类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        // 原始文件路径
        String videoPathOrg = getOrgPath(checkFileDTO.getpUrl(), checkFileDTO.getFileName()) ;
        LogUtil.info("convertVideoSave videoPathOrg=" + videoPathOrg + "， checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();
        //最终文件目录路径
        String finalTopPath = fileOptionTool.getPropertiesFilePathAll("cloud.savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }

        String serverUrl = HttpUtil.getRequestRootUrl(null);
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "." + fileType;

        List<CommonSource> convertList = new ArrayList<>();
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(checkFileDTO.getName(), checkFileDTO.getName(), fileType, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(fileType);
        fileSource.setSize(0L);
        fileSource.setHashMd5("");
        fileSource.setPath(finalFilePath);
        fileSource.setDomain(serverUrl);
        fileSource.setResolution(checkFileDTO.getResolution());
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
        // 需要获取md5并修改
        fileSource.setNeedHashMd5(1);
        convertList.add(fileSource);

        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("sourceID", fileSource.getSourceID());
        reMap.put("fileType", fileSource.getFileType());
        reMap.put("name", fileSource.getName());

        /** 操作剪辑-转码 */
        VideoCommonDto videoCommonDto = new VideoCommonDto();
        videoCommonDto.setTaskID(checkFileDTO.getTaskID());
        videoCommonDto.setFinalFilePath(finalFilePath);
        videoCommonDto.setVideoPathOrg(videoPathOrg);
        videoCommonDto.setOpType("2");
        videoCommonDto.setResolution(checkFileDTO.getResolution().replace("x","*"));
        videoCommonDto.setFrameRate(checkFileDTO.getFrameRate());
        videoCommonDto.setServerUrl(serverUrl);
        // 转码
        asyncUtil.convertVideoMerge(videoCommonDto, convertList);

        return reMap;
    }

    @Override
    public Map<String, Object> videoConfigSave(VideoCommonDto checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl()) || ObjectUtils.isEmpty(checkFileDTO.getName())
                || ObjectUtils.isEmpty(checkFileDTO.getConvertSuffix())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(checkFileDTO.getConvertSuffix())){
            LogUtil.error("convertVideoSave 转换格式错误 checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String fileType = checkFileDTO.getConvertSuffix();
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoSave 视频编辑类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 原始文件路径
        String videoPathOrg = getOrgPath(checkFileDTO.getpUrl(), checkFileDTO.getFileName());
        LogUtil.info("convertVideoSave videoPathOrg=" + videoPathOrg + "， checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();

        //最终文件目录路径
        String finalTopPath = fileOptionTool.getPropertiesFilePathAll("cloud.savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }

        String serverUrl = HttpUtil.getRequestRootUrl(null);
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "." + fileType;

        List<CommonSource> convertList = new ArrayList<>();
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(checkFileDTO.getName(), checkFileDTO.getName(), fileType, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(fileType);
        fileSource.setSize(0L);
        fileSource.setHashMd5("");
        fileSource.setPath(finalFilePath);
        fileSource.setDomain(serverUrl);
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
        // 需要获取md5并修改
        fileSource.setNeedHashMd5(1);
        convertList.add(fileSource);

        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("sourceID", fileSource.getSourceID());
        reMap.put("fileType", fileSource.getFileType());
        reMap.put("name", fileSource.getName());

        /** 操作剪辑-各种设置 */
        checkFileDTO.setTaskID(checkFileDTO.getTaskID());
        checkFileDTO.setFinalFilePath(finalFilePath);
        checkFileDTO.setVideoPathOrg(videoPathOrg);
        checkFileDTO.setOpType("2");
        checkFileDTO.setResolution(checkFileDTO.getResolution());
        checkFileDTO.setFrameRate(checkFileDTO.getFrameRate());
        checkFileDTO.setServerUrl(serverUrl);
        // 转码
        asyncUtil.settingVideo(checkFileDTO, convertList);

        return reMap;
    }

    private String getTaskIDUrl(String taskID){
        if (!ObjectUtils.isEmpty(taskID)){
            return taskID + "/";
        }
        return "";
    }

    /**
     * 视频剪辑  （照相）(切一张图片)
     * @param checkFileDTO
     * @param loginUser
     * @return
     */
    @Override
    public Map<String, Object> cutVideoImg(VideoCommonDto checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getName()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl()) || ObjectUtils.isEmpty(checkFileDTO.getCutTime())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String fileType = checkFileDTO.getFileName().substring(checkFileDTO.getFileName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("cutVideoImg 原视频类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String suffix = checkFileDTO.getName().substring(checkFileDTO.getName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)){
            LogUtil.error("cutVideoImg 视频照相图片类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }


        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();
        String fileName = checkFileDTO.getName();
        /** 操作剪辑照相 */
        // 原始文件路径
        String videoPathOrg = getOrgPath(checkFileDTO.getpUrl(), checkFileDTO.getFileName());

        LogUtil.info("cutVideoImg videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(checkFileDTO));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
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
//                return null;
            }
        }
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "." + suffix;

// ffmpeg -i input.mp4 -vn -c:a copy output.aac

        Boolean check = VideoGetUtil.covPicBatch(videoPathOrg, finalFilePath, checkFileDTO.getCutTime(), false);

        if (!check){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        String serverUrl = HttpUtil.getRequestRootUrl(null);
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(fileName, fileName, suffix, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(suffix);
        fileSource.setPath(finalFilePath);
        fileSource.setDomain(serverUrl);

        Long size = 0L;
        String md5 = "";

        //最终文件
        File finalFile = new File(finalFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(finalFile);
            md5 = org.springframework.util.DigestUtils.md5DigestAsHex(fis);
            size = finalFile.length();
            fis.close();
        } catch (IOException e) {
            md5 = "";
            LogUtil.error(e.getMessage(), " 获取图片md5失败 失败 commonSource=" + JsonUtils.beanToJson(fileSource));
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        fileSource.setSize(size);
        fileSource.setHashMd5(md5);
        fileSource.setSourceType(BusTypeEnum.CLOUD.getTypeCode());
        if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)) {
            String firstPath = FileUtil.getFirstStorageDevicePath(fileSource.getPath());
            busTypeHandleService.doForImage(null, false, fileSource);
            fileSource.setThumb((fileSource.getPath())
                    .replace(firstPath + "/doc/", firstPath + "/common/doc/")
                    .replace(firstPath + "/attachment/", firstPath + "/common/attachment/")
                    .replace(firstPath + "/private/", firstPath + "/common/")
            );
        }
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);


        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("path",finalFilePath);
        reMap.put("sourceID",fileSource.getSourceID());
        reMap.put("name",fileSource.getName());
        return reMap;
    }

    @Override
    public Map<String, Object> copyAudio(VideoCommonDto checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getName()) || ObjectUtils.isEmpty(checkFileDTO.getFileName())
                || ObjectUtils.isEmpty(checkFileDTO.getpUrl()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String fileType = checkFileDTO.getFileName().substring(checkFileDTO.getFileName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileType)){
            LogUtil.error("copyAudio 原视频类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String suffix = checkFileDTO.getName().substring(checkFileDTO.getName().lastIndexOf(".") + 1 );
        if (!Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(suffix)){
            LogUtil.error("copyAudio 视频照相图片类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }


        List<String> sourceNameList = null;
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();
        String fileName = checkFileDTO.getName();
        /** 操作剪辑照相 */
        // 原始文件路径
        String videoPathOrg = getOrgPath(checkFileDTO.getpUrl(), checkFileDTO.getFileName());
        LogUtil.info("copyAudio videoPathOrg=" + videoPathOrg + "， videoCommonDto=" + JsonUtils.beanToJson(checkFileDTO));

        File file = new File(videoPathOrg);
        if (!file.exists()){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
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
//                return null;
            }
        }
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "." + suffix;

        Boolean check = VideoGetUtil.copyAudioBatch(videoPathOrg, finalFilePath);

        if (!check){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        String serverUrl = HttpUtil.getRequestRootUrl(null);
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(fileName, fileName, suffix, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(suffix);
        fileSource.setPath(finalFilePath);
        fileSource.setDomain(serverUrl);

        Long size = 0L;
        String md5 = "";

        //最终文件
        File finalFile = new File(finalFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(finalFile);
            md5 = org.springframework.util.DigestUtils.md5DigestAsHex(fis);
            size = finalFile.length();
            fis.close();
        } catch (IOException e) {
            md5 = "";
            LogUtil.error(e.getMessage(), " copyAudio 获取音频md5失败 失败 commonSource=" + JsonUtils.beanToJson(fileSource));
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        fileSource.setSize(size);
        fileSource.setHashMd5(md5);
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);

        Map<String, Object> reMap = new HashMap<>(3);
        reMap.put("path",finalFilePath);
        reMap.put("sourceID",fileSource.getSourceID());
        reMap.put("name",fileSource.getName());
        return reMap;
    }
}
