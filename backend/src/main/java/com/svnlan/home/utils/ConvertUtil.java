package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFileMeta;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.home.vo.FileMetaVo;
import com.svnlan.home.vo.YzEditParamsDto;
import com.svnlan.user.dao.CommonConvertDao;
import com.svnlan.user.dao.Impl.SystemOptionDaoImpl;
import com.svnlan.user.domain.CommonConvert;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 10:47
 */
@Component
public class ConvertUtil {
    @Resource
    private DocUtil docUtil;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    CommonConvertDao commonConvertDao;
    @Resource
    StorageService storageService;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    SystemOptionDaoImpl systemOptionDaoImpl;



    @Value("${m3u8.buildkey}")
    private String buildKey;
    @Value("${m3u8.buildkey2}")
    private String buildKey2;


    @Value("${yz.view.convert.url}")
    private String yongZhongConvertUrl;
    @Value("${yz.edit.convert.url}")
    private String yongZhongEditUrl;
    @Value("${yz.edit.post.url}")
    private String yongZhongGetEditPostUrl;
    @Value("${yz.fileId.prefix}")
    private String yzFileIdPreFix;

    public static final String yongZhongEditUrlApi = "/plugin/yzwo";
    public static final String editPostUrlApi = "/plugin/yzwo/api.do";
    public static final String yongZhongConvertUrlApi = "/fcscloud/composite/upload";
    public static final String yongzhong_editUrlHost = "https://demo.filesbox.cn";
    /*

yz.view.convert.url=https://demo.filesbox.cn
yz.edit.convert.url=https://demo.filesbox.cn/plugin/yzwo
yz.edit.post.url=https://demo.filesbox.cn/plugin/yzwo/api.do


yz.view.convert.url=https://demo.filesbox.cn/fcscloud/composite/httpfile
yz.edit.convert.url=https://demo.filesbox.cn/plugin/yzwo
yz.edit.post.url=https://demo.filesbox.cn/plugin/yzwo/api.do
     */
    @Value("${convert.frequency.video.count}")
    private Integer convertVideoFrequencyCount;
    @Value("${convert.frequency.audio.count}")
    private Integer convertAudioFrequencyCount;


    @Resource(name = "threadExecConvert")
    private ThreadPoolTaskExecutor executorConvert;

    private static String[] VideoType = {"flv","avi","mpeg","mpg","mp4","rmvb","rm","mov","3gp","f4v","wmv"};
    private static String[] DocType = {"doc", "docx", "ppt", "pptx", "pdf"};
    private static String[] AudioType = {"mp3", "wav", "flac", "mpa", "aac", "m4a"};

    /**
     * @Description: 视频转码
     * @params:  [videoConvert]
     * @Return:  com.svnlan.upload.vo.VideoConvertVO
     * @Modified:
     */
    @Async(value = "asyncTaskExecutor")
    public void doConvert(ConvertDTO convertDTO, CommonSource commonSource) {
        doConvertMain(convertDTO, commonSource);
    }

    public void doConvertMain(ConvertDTO convertDTO, CommonSource commonSource) {
        LogUtil.info("doConvert begin进入转码流程 : "+ JsonUtils.beanToJson(convertDTO) +
                "，time=" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) + ", commonSource=" + (!ObjectUtils.isEmpty(commonSource) ? JsonUtils.beanToJson(commonSource) : " is null") );

        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(convertDTO.getBusType());
        if (busTypeEnum == null){
            LogUtil.error("业务类型不正确："+ JsonUtils.beanToJson(convertDTO));
            return;
        }
        switch (busTypeEnum) {
            case CLOUD:
                /** 后台转码时使用
                 CommonSource commonSource = this.getAttachment(convertDTO.getBusId());*/
                if ((ObjectUtils.isEmpty(commonSource) || ObjectUtils.isEmpty(commonSource.getSourceID())) && !ObjectUtils.isEmpty(convertDTO.getBusId())) {
                    commonSource = ioFileDao.findCommonSourceById(convertDTO.getBusId());
                    if (ObjectUtils.isEmpty(commonSource)) {
                        LogUtil.error("附件不存在，id：" + convertDTO.getBusId());
                        return;
                    }
                }
                if (ObjectUtils.isEmpty(commonSource.getDomain())){
                    commonSource.setDomain(convertDTO.getDomain());
                }
                // 转码类型 // 1 手动执行 2 定时任务执行
                if (ObjectUtils.isEmpty(commonSource.getOpType())) {
                    commonSource.setOpType(convertDTO.getOpType());
                }
                if (!ObjectUtils.isEmpty(commonSource.getOpType()) && "1".equals(commonSource.getOpType())) {
                    // 转码手动执行人
                    commonSource.setUserID(convertDTO.getUserID());
                }

                if (ObjectUtils.isEmpty(commonSource.getSourceType())){
                    commonSource.setSourceType(busTypeEnum.getTypeCode());
                }

                File pathFile = new File(commonSource.getPath());
                if (!pathFile.exists()){
                    if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType()) &&
                            !ObjectUtils.isEmpty(convertDTO.getOtherType())
                            && Arrays.asList("unZip","cutVideo","splitVideo","mergeVideo","convertVideo","settingVideo","syncDing").contains(convertDTO.getOtherType())){
                        this.saveVideoConvertRecord(commonSource, "2", "源文件还未生成");
                    }
                    return;
                }

                if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType()) &&
                        !ObjectUtils.isEmpty(convertDTO.getOtherType())
                        && Arrays.asList("unZip","cutVideo","splitVideo","mergeVideo","convertVideo","settingVideo","syncDing").contains(convertDTO.getOtherType())){
                    this.saveVideoConvertRecord(commonSource, "0", "");
                }
                //
                if (!ObjectUtils.isEmpty(convertDTO.getIsNew()) && 1 == convertDTO.getIsNew()){
                    // 强制转码
                    commonSource.setIsM3u8(0);
                }
                this.doAttachment(commonSource);
                break;
            default:
                LogUtil.error("非转码业务, busType:" + convertDTO.getBusType() + ", busId" + convertDTO.getBusId());
        }
        stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + commonSource.getUserID(), 1, TimeUnit.MILLISECONDS);
    }

    /**
     * @Description: 处理附件
     * @params:  [commonSource]
     * @Return:  void
     * @Modified:
     */
    private void doAttachment(CommonSource commonSource) {
        // boolean isVideo = Arrays.asList(VideoType).contains(commonSource.getFileType());
        boolean isVideo = Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType());
        boolean isAudio = Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(commonSource.getFileType());
        boolean isYongZhong = Arrays.asList(GlobalConfig.yongzhong_doc_excel_ppt_type).contains(commonSource.getFileType());
        boolean isCamera = Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType());
        if (isVideo){
            if (commonSource.getIsM3u8() != null && commonSource.getIsM3u8().equals(1)){
                // 转码完成记录
                this.saveVideoConvertRecord(commonSource, "1", "");
                return;
            }
            // 视频转码
            this.execConvertFrequencyCountVideo(commonSource, true, 0);
        } else if (isAudio){
            // 音频转码
            execConvertFrequencyCountAudio("", commonSource, true, 0);
        } else if (isYongZhong){
            this.yongZhong(commonSource);
        }else if (isCamera) {
            // 相机文件
            convertCameraToImgCommon("相机文件转Img", commonSource);
        }
        // 非视频音频文件
        if (!isVideo && !isAudio) {
            // 视频音频不在这里执行，放里面执行，排队
            if (!ObjectUtils.isEmpty(commonSource.getNeedHashMd5()) && commonSource.getNeedHashMd5().intValue() == 1) {
                // 需修改文件md5
                this.updateFileHashMd5(commonSource);
            }
        }

        /*
        boolean isH5Doc = Arrays.asList(DocType).contains(commonSource.getFileType());
            if (isH5Doc){
                this.h5Doc(commonSource);
            }
         */
    }

    private void execConvertFrequencyCountVideo(CommonSource commonSource, boolean check, int a){

        long tenantId = ObjectUtils.isEmpty(commonSource.getTenantId()) ? 1 : commonSource.getTenantId();
        String videoRedisExecKey = GlobalConfig.videoRedisExecKey + tenantId;
        String videoRedisExecRedisKey = GlobalConfig.videoRedisExecRedisKey + tenantId;
        String hashSourceIDKey = commonSource.getSourceID() + "";
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        if (check){
            // videoRedisExecRedisKey
            Boolean e = stringRedisTemplate.opsForValue().setIfAbsent(videoRedisExecKey, "1");
            LogUtil.info("execConvertFrequencyCountVideo setIfAbsent :" + e + "，hashSourceIDKey=" + hashSourceIDKey);
            if (!e){
                    String checkExist = operations.get(videoRedisExecRedisKey, hashSourceIDKey);
                    if (ObjectUtils.isEmpty(checkExist)) {
                        LogUtil.info("execConvertFrequencyCountVideo 转码执行中，加入队列 sourceId="+hashSourceIDKey+"  a="+a+" " );
                        // 已经在执行则放入队列
                        operations.put(videoRedisExecRedisKey, hashSourceIDKey, JsonUtils.beanToJson(commonSource));
                    }
                return;
            }else {
                stringRedisTemplate.expire(videoRedisExecKey, 1,TimeUnit.HOURS);
            }
        }

        // 需要执行的List
        List<CommonSource> execList = new ArrayList<>();
        // 如果已经有则排队
        Long hashLength = operations.size(videoRedisExecRedisKey);
        LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+" 第一次查询 a="+a+" hashLength=" + hashLength);
        if (!ObjectUtils.isEmpty(hashLength) && hashLength > 0) {
            Set<String> keyList = operations.keys(videoRedisExecRedisKey);
            LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+" 第一次查询 a="+a+" keyList=" + JsonUtils.beanToJson(keyList));
            String checkExist = operations.get(videoRedisExecRedisKey, hashSourceIDKey);
            if (ObjectUtils.isEmpty(checkExist)) {
                // 已经在执行则放入队列
                operations.put(videoRedisExecRedisKey, hashSourceIDKey, JsonUtils.beanToJson(commonSource));
                keyList.add(hashSourceIDKey);
            }
            CommonSource c = null;
            int i = 0;
            for (Object o : keyList){
                if (i < convertVideoFrequencyCount){
                    String value = operations.get(videoRedisExecRedisKey, o);
                    try {
                        c = JsonUtils.jsonToBean(value, CommonSource.class);
                        execList.add(c);
                    }catch (Exception e){

                    }
                }else {
                    break;
                }
                i++;
            }
        }else {
            LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+" 第一次查询 a="+a);
            execList.add(commonSource);
        }
        LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+"  a="+a+" execList=" + JsonUtils.beanToJson(execList));

        if (!CollectionUtils.isEmpty(execList) && execList.size() > 0){
            if (convertVideoFrequencyCount > 1){
                List<Future> futures = new ArrayList<>();
                for (CommonSource cs : execList){
                    Future<?> future = executorConvert.submit(() -> {
                        this.video(cs);
                        operations.delete(videoRedisExecRedisKey,cs.getSourceID()+"");
                    });
                    futures.add(future);
                }
                if (!CollectionUtils.isEmpty(futures)){
                    try {
                        //查询任务执行的结果
                        for (Future<?> future : futures) {
                            while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                                if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                                    LogUtil.info("execConvertFrequencyCountVideo 任务i=" + future.get() + "获取完成!" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
                                    break;//当前future获取结果完毕，跳出while
                                } else {
                                    Thread.sleep(2);//每次轮询休息2毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                                }
                            }
                        }
                    }catch (Exception e){
                        LogUtil.error(e, "execConvertFrequencyCountVideo error ");
                    }
                }
            }else {
                // convertVideoFrequencyCount
                this.video(execList.get(0));
                operations.delete(videoRedisExecRedisKey,hashSourceIDKey);
            }
        }

        hashLength = operations.size(videoRedisExecRedisKey);
        LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+" 第二次查询 a="+a+" hashLength=" + hashLength);
        if (!ObjectUtils.isEmpty(hashLength) && hashLength > 0){
            // 判断是否还有在排队，有则继续执行
            Set<String> keySet2 = operations.keys(videoRedisExecRedisKey);
            LogUtil.info("execConvertFrequencyCountVideo sourceId="+hashSourceIDKey+"  a="+a+" 第二次查询 keyList=" + JsonUtils.beanToJson(keySet2));
            if (!CollectionUtils.isEmpty(keySet2)){
                List keyList2 = new ArrayList(keySet2);
                try {
                    String value = operations.get(videoRedisExecRedisKey, keyList2.get(0));
                    CommonSource c = JsonUtils.jsonToBean(value, CommonSource.class);
                    execConvertFrequencyCountVideo(c, false, a+1);
                }catch (Exception e){
                    LogUtil.error(e, "判断是否还有在排队时错误 ");
                }
            }
        }

        // 执行完删除
        stringRedisTemplate.delete(videoRedisExecKey);
    }
    private void execConvertFrequencyCountAudio(String prefix, CommonSource commonSource, boolean check, int a){

        long tenantId = ObjectUtils.isEmpty(commonSource.getTenantId()) ? 1 : commonSource.getTenantId();
        String audioRedisExecKey = GlobalConfig.audioRedisExecKey + tenantId;
        String audioRedisExecRedisKey = GlobalConfig.audioRedisRedisExecKey + tenantId;
        String hashSourceIDKey = commonSource.getSourceID() + "";
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        if (check){
            Boolean e = stringRedisTemplate.opsForValue().setIfAbsent(audioRedisExecKey, "1");
            LogUtil.info("execConvertFrequencyCountAudio setIfAbsent="+e);
            // audioRedisExecKey
            if (!e){
                String checkExist = operations.get(audioRedisExecRedisKey, hashSourceIDKey);
                if (ObjectUtils.isEmpty(checkExist)) {
                    LogUtil.info("execConvertFrequencyCountAudio 转码执行中，加入队列 sourceId="+hashSourceIDKey+"  a="+a+" " );
                    // 已经在执行则放入队列
                    operations.put(audioRedisExecRedisKey, hashSourceIDKey, JsonUtils.beanToJson(commonSource));
                }
                return;
            }
        }

        // 需要执行的List
        List<CommonSource> execList = new ArrayList<>();
        Long hashLength = operations.size(audioRedisExecRedisKey);
        LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+" 第一次查询 a="+a+" hashLength=" + hashLength);

        // 如果已经有则排队
        if (!ObjectUtils.isEmpty(hashLength) && hashLength > 0) {
            Set<String> keyList = operations.keys(audioRedisExecRedisKey);
            LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+" 第一次查询 a="+a+" keyList=" + JsonUtils.beanToJson(keyList));

            String checkExist = operations.get(audioRedisExecRedisKey, hashSourceIDKey);
            if (ObjectUtils.isEmpty(checkExist)) {
                // 已经在执行则放入队列
                operations.put(audioRedisExecRedisKey, hashSourceIDKey, JsonUtils.beanToJson(commonSource));
                keyList.add(hashSourceIDKey);
            }
            CommonSource c = null;
            int i = 0;
            for (Object o : keyList){
                if (i < convertAudioFrequencyCount){
                    String value = operations.get(audioRedisExecRedisKey, o);
                    try {
                        c = JsonUtils.jsonToBean(value, CommonSource.class);
                        execList.add(c);
                    }catch (Exception e){

                    }
                }else {
                    break;
                }
                i++;
            }
        }else {
            LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+" 第一次查询 a="+a+" keyList=" + hashSourceIDKey);
            execList.add(commonSource);
        }
        LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+"  a="+a+" execList=" + JsonUtils.beanToJson(execList));

        if (!CollectionUtils.isEmpty(execList) && execList.size() > 0){
            if (convertAudioFrequencyCount > 1){
                List<Future> futures = new ArrayList<>();
                for (CommonSource cs : execList){
                    Future<?> future = executorConvert.submit(() -> {
                        this.convertAudioCommon(prefix, cs);
                        operations.delete(audioRedisExecRedisKey,cs.getSourceID()+"");
                    });
                    futures.add(future);
                }
                if (!CollectionUtils.isEmpty(futures)){
                    try {
                        //查询任务执行的结果
                        for (Future<?> future : futures) {
                            while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                                if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                                    LogUtil.info("execConvertFrequencyCountAudio 任务i=" + future.get() + "获取完成!" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
                                    break;//当前future获取结果完毕，跳出while
                                } else {
                                    Thread.sleep(2);//每次轮询休息2毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                                }
                            }
                        }
                    }catch (Exception e){
                        LogUtil.error(e, "execConvertFrequencyCountAudio error ");
                    }
                }
            }else {
                this.convertAudioCommon(prefix, execList.get(0));
                operations.delete(audioRedisExecRedisKey,hashSourceIDKey);
            }
        }
        hashLength = operations.size(audioRedisExecRedisKey);
        LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+" 第二次查询 a="+a+" hashLength=" + hashLength);
        if (!ObjectUtils.isEmpty(hashLength) && hashLength > 0){
            // 判断是否还有在排队，有则继续执行
            Set<String> keySet2 = operations.keys(audioRedisExecRedisKey);
            LogUtil.info("execConvertFrequencyCountAudio sourceId="+hashSourceIDKey+"  a="+a+" 第二次查询 keyList=" + JsonUtils.beanToJson(keySet2));
            if (!CollectionUtils.isEmpty(keySet2)){
                List keyList2 = new ArrayList(keySet2);
                try {
                    String value = operations.get(audioRedisExecRedisKey, keyList2.get(0));
                    CommonSource c = JsonUtils.jsonToBean(value, CommonSource.class);
                    execConvertFrequencyCountAudio(prefix,c, false, a+1);
                }catch (Exception e){
                    LogUtil.error(e, "判断是否还有在排队时错误 ");
                }
            }
        }

        // 执行完删除
        stringRedisTemplate.delete(audioRedisExecKey);
    }

    public void updateFileHashMd5(CommonSource commonSource) {
        Long size = 0L;
        String serverChecksum = "";
        //最终文件
        File finalFile = new File(commonSource.getPath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(finalFile);
            serverChecksum = org.springframework.util.DigestUtils.md5DigestAsHex(fis);
            size = finalFile.length();
            commonSource.setSize(size);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " updateFileHashMd5 失败 commonSource=" + JsonUtils.beanToJson(commonSource));
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        if (!ObjectUtils.isEmpty(serverChecksum)){
            ioFileDao.updateFileMd5AndSizeByFileID(serverChecksum, commonSource.getFileID(), size);
            Long sourceSize = ioSourceDao.getSourceSize(commonSource.getSourceID());
            boolean updateParentSize = false;
            if (!ObjectUtils.isEmpty(sourceSize) && sourceSize <= 0){
                try {
                    ioSourceDao.updateSourceSize(commonSource.getSourceID(), size);
                }catch (Exception e){
                    LogUtil.error(e,"updateSourceSize error");
                }
                commonSource.setGroupID(0L);
                this.updateMemory(commonSource);
                updateParentSize = true;
            }
            if (!updateParentSize){
                commonSource.setGroupID(0L);
                this.updateMemory(commonSource);
            }
        }

    }

    public void updateMemory(CommonSource commonSource) {
        if (ObjectUtils.isEmpty(commonSource.getGroupID())){
            commonSource.setGroupID(0L);
        }
        LogUtil.info("updateMemory 111 commonSource=" + JsonUtils.beanToJson(commonSource));
        updateMemory(commonSource.getSize(), commonSource.getGroupID(), commonSource.getUserID(), commonSource.getTargetType(), commonSource.getParentLevel());
    }

    public void updateMemory(long size, Long groupID, long userID, Integer targetType, String parentLevel) {
        if (ObjectUtils.isEmpty(groupID)){
            groupID = 0L;
        }
        LogUtil.info(String.format("updateMemory 111 size=%s，targetType=%s，parentLevel=%s" , size,targetType, parentLevel));
        if (size > 0 && !ObjectUtils.isEmpty(targetType)) {
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("groupID", groupID);
            paramMap.put("memory", size);
            paramMap.put("userID", userID);
            LogUtil.info("updateMemory paramMap=" + JsonUtils.beanToJson(paramMap) + ",targetType=" + targetType + "， parentLevel=" + parentLevel);
            try {
                if (targetType.intValue() == 1) {
                    homeExplorerDao.updateUserMemory(paramMap);
                } else {
                    if (groupID > 0) {
                        this.updateGroupMemoryCopy(paramMap, groupID);
                    }else {
                        this.updateGroupMemoryCopyBySearch(paramMap, parentLevel);
                    }
                }
            } catch (Exception e) {
                LogUtil.error(e, "更新 企业云盘 memory失败");
            }
            // source updateSourceMemoryList
            if (!ObjectUtils.isEmpty(parentLevel)) {
                try {
                    List<Long> sourceIds = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sourceIds)) {
                        ioSourceDao.updateSourceMemoryList(sourceIds, size);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " updateMemory updateSourceMemoryList error ");
                }

            }
        }
    }

    public void updateGroupMemoryCopyBySearch(Map<String, Object> paramMap, String parentLevel) {
        List<Long> sourcePIDs = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sourcePIDs)){
            return;
        }
        List<String> gpList = homeExplorerDao.getGroupParentLevelList(sourcePIDs);
        if (CollectionUtils.isEmpty(gpList)){
            return;
        }
        String groupParentLevel = Joiner.on(",").join(gpList);
        List<Long> groupIDs = Arrays.asList(groupParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIDs)){
            return;
        }
        paramMap.put("list", groupIDs);
        homeExplorerDao.updateMemoryList(paramMap);
    }
    public void updateGroupMemoryCopy(Map<String, Object> paramMap, Long groupID) {
        String groupParentLevel = homeExplorerDao.getGroupParentLevel(groupID);
        List<Long> groupIDs = Arrays.asList(groupParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIDs)){
            groupIDs = new ArrayList<>();
        }
        groupIDs.add(groupID);
        paramMap.put("list", groupIDs);
        homeExplorerDao.updateMemoryList(paramMap);
    }

    public void yongZhong(CommonSource commonSource) {
        if (ObjectUtils.isEmpty(commonSource.getDomain())){
            LogUtil.error("yongZhong domain is null !");
            return;
        }
        // 预览
        String yzViewData = yongZhongView(commonSource, true);
        // 编辑
        //String yzEditData = yongZhongEdit(commonSource);
        commonSource.setYzViewData(yzViewData);
        //commonSource.setYzEditData(yzEditData);

        /** 更新文档其他信息 */
        this.updateYZPreviewUrlValue(Arrays.asList(commonSource.getFileID()), yzViewData);
    }

    public void yongZhongPre(CommonSource commonSource, boolean isView) {
        if (ObjectUtils.isEmpty(commonSource.getDomain())){
            LogUtil.error("yongZhong domain is null !");
            return;
        }
        String urlData = "";

        if (isView){
            // 预览
            urlData = yongZhongView(commonSource, true);
            commonSource.setYzViewData(urlData);
            /** 更新文档其他信息 */
            this.updateYZPreviewUrlValue(Arrays.asList(commonSource.getFileID()), urlData, isView);
            commonSource.setIsPreview(1);
        }else {
            commonSource.setToken(RandomUtil.getuuid());
            // 编辑
            urlData = yongZhongEdit(commonSource);
            commonSource.setYzEditData(urlData);
        }
    }



    public String yongZhongEdit(CommonSource commonSource) {


        String editUrlHost = null;
        OptionVo optionVo = null;
        try {// 获取永中插件
            optionVo = systemOptionDaoImpl.getSystemOtherConfigByKey("System.pluginList", "ID-2");
            if (ObjectUtils.isEmpty(optionVo)){
                LogUtil.error("edit永中插件未查询到数据");
                return "";
            }
            String valueConfig = optionVo.getValue();
            JSONObject o = JSONObject.parseObject(valueConfig);
            Integer status = o.getInteger("status");
            if (!ObjectUtils.isEmpty(status) && 1 == status){
                editUrlHost = o.getString("editUrl");
            }
            if (ObjectUtils.isEmpty(editUrlHost)){
                editUrlHost = yongzhong_editUrlHost;
            }
        }catch (Exception e){
            LogUtil.error(e,"edit 永中编辑插件数据解析错误 optionVo=" + (ObjectUtils.isEmpty(optionVo) ? null : JsonUtils.beanToJson(optionVo)));
        }
        if (ObjectUtils.isEmpty(editUrlHost)){
            LogUtil.error("edit 永中编辑插件未开启或者未配置永中编辑地址 optionVo=" + (ObjectUtils.isEmpty(optionVo) ? null : JsonUtils.beanToJson(optionVo)));
            return "";
        }

        String yongZhongEditUrl = editUrlHost + yongZhongEditUrlApi;
        String yongZhongGetEditPostUrl = editUrlHost + editPostUrlApi;
        //String downloadKey = FileUtil.getDownloadKey("");
        String yzEditData = "";
        String downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(commonSource.getName())
                + "?busId=" + commonSource.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" ;
        // 编辑
        String configCallbackHost = null;
        String networkConfig = systemOptionDaoImpl.getSystemConfigByKey("networkConfig",null);
        if (!ObjectUtils.isEmpty(networkConfig) && !"{}".equals(networkConfig)) {
            try {
                JSONObject obj = JSONObject.parseObject(networkConfig);
                configCallbackHost = obj.getString("external");
            }catch (Exception e){

            }
        }

        String filePath = (ObjectUtils.isEmpty(configCallbackHost) ? commonSource.getDomain() : configCallbackHost) + downloadUrl;
        String uuid = ObjectUtils.isEmpty(commonSource.getToken()) ? "" : "00" + commonSource.getToken();

        if (!ObjectUtils.isEmpty(commonSource.getToken())){
            Map<String, Object> editMap = new HashMap<>(1);
            editMap.put("userID", commonSource.getUserID().toString());
            String userKey = GlobalConfig.yzwo_file_edit_user_key + commonSource.getToken();
            stringRedisTemplate.opsForValue().set(userKey, JsonUtils.beanToJson(editMap), 5, TimeUnit.HOURS);
        }
        //String filePath = commonSource.getDomain() + commonSource.getPath();
        String callbackUrl = (ObjectUtils.isEmpty(configCallbackHost) ? commonSource.getDomain() : configCallbackHost) + "/api/disk/yzwo/office";
        Map<String, Object> param = new HashMap<>(2);
        param.put("method", 1);
        param.put("params", new YzEditParamsDto(commonSource.getUserID().toString(), yzFileIdPreFix + commonSource.getSourceID()+ yzFileIdPreFix +  uuid, commonSource.getName(), filePath, callbackUrl));
        try {
            String postUrl = yongZhongGetEditPostUrl + "?jsonParams=" + URLEncoder.encode(JsonUtils.beanToJson(param),"UTF-8") ;

            String res1 = docUtil.postYZFile(postUrl);
            LogUtil.info("yongZhong yongZhongEdit return retry : " + res1 + "postUrl : " + postUrl );
            Map<String, Object> resMap = JsonUtils.jsonToMap(res1);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorCode") && "0".equals(resMap.get("errorCode").toString())){
                yzEditData = this.yzEditUrl(resMap, yongZhongEditUrl);
            }else if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){
                yzEditData = this.yzEditUrl(resMap, yongZhongEditUrl);
            }
            LogUtil.info("yongZhong yongZhongEdit resMap=" + JsonUtils.beanToJson(resMap));
        } catch (Exception e){
            LogUtil.error(e, "yongZhong yongZhongEdit 失败 , o:" + filePath);
        }
        return yzEditData;
    }

    private String yzEditUrl(Map<String, Object> resMap, String yongZhongEditUrl){
        String data = resMap.get("result").toString();
        Map<String, Object> urlMap = JsonUtils.jsonToMap(data);
        if (!ObjectUtils.isEmpty(urlMap) && urlMap.containsKey("urls")){
            String urls = urlMap.get("urls").toString();
            int index = urls.indexOf("/s/");
            if (index >= 0){
                return yongZhongEditUrl + urls.substring(index, urls.length());
            }
        }
        return "";
    }

    private String getYzConvertType(String fileType){
        switch (fileType){
            case "zip":
            case "gz":
            case "rar":
            case "iso":
            case "tar":
            case "7z":
            case "ar":
            case "bz":
            case "bz2":
            case "xz":
            case "arj":
                return "19";
            case "pdf":
                return "20";
            default:
                return "61";
        }


    }

    public String yongZhongView(CommonSource commonSource, boolean save) {
        // 获取永中插件
        OptionVo optionVo = systemOptionDaoImpl.getSystemOtherConfigByKey("System.pluginList", "ID-2");
        if (ObjectUtils.isEmpty(optionVo)){
            LogUtil.error("永中插件未查询到数据");
            return "";
        }
        String url = null;
        String valueConfig = optionVo.getValue();
        try {
            JSONObject o = JSONObject.parseObject(valueConfig);
            Integer status = o.getInteger("status");
            if (!ObjectUtils.isEmpty(status) && 1 == status){
                String viewUrl = o.getString("viewUrl");
                if (!ObjectUtils.isEmpty(viewUrl)){
                    url = viewUrl + yongZhongConvertUrlApi;
                }else {
                    url = yongzhong_editUrlHost + yongZhongConvertUrlApi;
                }
            }
        }catch (Exception e){
            LogUtil.error(e,"永中编辑插件数据解析错误 optionVo=" + JsonUtils.beanToJson(optionVo));
        }
        if (ObjectUtils.isEmpty(url)){
            LogUtil.error("永中编辑插件未开启或者未配置永中编辑预览地址 optionVo=" + JsonUtils.beanToJson(optionVo));
            return "";
        }
        String yzViewData = "";
        String filePath = commonSource.getPath();

        String convertType = getYzConvertType(commonSource.getFileType());
        long time = 2626560;
        String result = postParams(url, filePath, convertType, time);
        LogUtil.info("yongZhong h5 api return retry : " + result + "filePath : " + filePath + ", o:" + commonSource.getName());
        if (!ObjectUtils.isEmpty(result)){
            Map<String, Object> resMap = JsonUtils.jsonToMap(result);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){
                yzViewData = resMap.get("data").toString();
                if (save) {
                    commonSource.setIsPreview(1);
                    commonSource.setIsM3u8(0);
                    ioFileDao.updateVideoConvert(commonSource);
                }
            }else{
                LogUtil.info("yongZhong  转码失败 res1=" + result + ", commonSource=" + JsonUtils.beanToJson(commonSource));
            }
        }
        return yzViewData;
    }
    public String yongZhongView1(CommonSource commonSource, boolean save) {
        String yzViewData = "";
        String convertType = getYzConvertType(commonSource.getFileType());
        // 预览
        String viewKey = FileUtil.getVideoImgDownloadKey(commonSource.getPath());

        String filePath = commonSource.getDomain() + "/api/disk/video/img/"+ commonSource.getSourceID()+ "." + commonSource.getFileType() +"?showPreview=1&key=" + viewKey;
        try { // %20 为空格
            String postUrl = yongZhongConvertUrl + "?noCache=1&convertType=" + convertType + "&htmlName=%20&htmlTitle=%20&fileUrl=" + URLEncoder.encode(filePath,"UTF-8");
            String res1 = docUtil.postYZFile(postUrl);
            LogUtil.info("yongZhong h5 api return retry : " + res1 + "filePath : " + filePath + ", o:" + commonSource.getName());
            Map<String, Object> resMap = JsonUtils.jsonToMap(res1);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){
                yzViewData = resMap.get("data").toString();
                if (save) {
                    commonSource.setIsPreview(1);
                    commonSource.setIsM3u8(0);
                    ioFileDao.updateVideoConvert(commonSource);
                }
            }else{
                LogUtil.info("yongZhong  转码失败 res1=" + res1 + ", commonSource=" + JsonUtils.beanToJson(commonSource));
            }
        } catch (Exception e){
            LogUtil.error(e, "yongZhong 转码失败失败 , o:" + commonSource.getName());
        }
        return yzViewData;
    }

    //上传文件  time  预览/下载链接 过期时间 单位秒,小于等于 0 表示不限制
    public static String postParams(String url, String filepath, String type, long time) {

        LogUtil.info("yongZhong 永中上传并转换 postParams url=" + url + "，filepath=" + filepath+ "，type=" + type+ "，time=" + time);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_0);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.setCharset(Charset.forName("UTF-8"));
            FileBody file = new FileBody(new File(filepath));
            mEntityBuilder.addPart("file", file);
            StringBody comment = new StringBody(type, ContentType.APPLICATION_JSON);
            mEntityBuilder.addPart("convertType", comment);
            StringBody timeBody = new StringBody(time +"", ContentType.APPLICATION_JSON);
            mEntityBuilder.addPart("Time", timeBody);
            mEntityBuilder.addPart("time", timeBody);
            StringBody showFooter = new StringBody("0", ContentType.APPLICATION_JSON);
            mEntityBuilder.addPart("showFooter", showFooter);
            StringBody isPrint = new StringBody("1", ContentType.APPLICATION_JSON);
            mEntityBuilder.addPart("isPrint", isPrint);
            HttpEntity reqEntity = mEntityBuilder.build();
            httpPost.setEntity(reqEntity);
            response = httpclient.execute(httpPost);
            int statusCode =
                    response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                byte [] josn = EntityUtils.toByteArray(resEntity);
                result = new String(josn,"UTF-8");
                LogUtil.info("yongZhong 永中上传并转换 postParams result=" + result);
                EntityUtils.consume(resEntity);
            }
        } catch (Exception e) {
            LogUtil.error(e, "yongZhong 永中上传并转换 失败 ");
        } finally {
            HttpClientUtils.closeQuietly(httpclient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

    private void h5Doc(CommonSource commonSource) {
        LogUtil.info("进入h5流程  source:" + JsonUtils.beanToJson(commonSource));
        String sourcePath;
        String fileName;
        String sourceSuffix;
        CommonSource cs = new CommonSource();

        boolean isAttachment;

        Integer h5ConvertState;
        Integer swfConverState;
        String pdfPageRedisKey;
        //处理附件
        h5ConvertState = commonSource.getAppPreview();
        swfConverState = commonSource.getIsM3u8();
        sourcePath = commonSource.getPath();
        //云盘
        fileName = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())) + commonSource.getFileID();
        String basePath = "";// commonSource.getSourcePathPre().equals("") ? PropertiesUtil.getUpConfig("cloud.savePath") : commonSource.getSourcePathPre();

        sourceSuffix = commonSource.getFileType();
        cs.setName(commonSource.getName());
        cs.setFileID(commonSource.getFileID());
        isAttachment = true;
        pdfPageRedisKey = commonSource.getHashMd5() + "_" + commonSource.getSize();

        //源文件
        String sourceFileName = basePath + sourcePath;
        //目标目录路径
        String filePath = this.getDestFilePath(sourcePath);
        //是否已转
        boolean converted = !this.checkConvertFile(sourceFileName, isAttachment);
        //是否需要h5转码
        boolean needH5Convert = (!converted || h5ConvertState.equals(-2)) && !h5ConvertState.equals(1);
        //是否需要swf转码
        boolean needSwfConvert = (!converted || swfConverState.equals(-2)) && !swfConverState.equals(1);
        String targetFileName = null;
        String imagePath = filePath + fileName + "/";
        try {
            switch (sourceSuffix){
                case "doc":
                case "docx":
                    if (needH5Convert) {
//                        docUtil.doc2Image(sourceFileName, cs);
                    }
                    if (needSwfConvert) {
                        docUtil.pdf2Swf(sourceFileName, cs);
                    }
                    break;
                case "ppt":
                case "pptx":
                    if (needH5Convert) {
//                        docUtil.doc2Image(sourceFileName, cs);
                    }
                    if (needSwfConvert) {
                        docUtil.pdf2Swf(sourceFileName, cs);
                    }
                    break;
                case "pdf":
                    if (needH5Convert) {
                        int pageCount = docUtil.pdf2image(sourceFileName, imagePath, pdfPageRedisKey);
                        if (pageCount == 0) {
                            LogUtil.info("转pdf，页数为0. source:" + JsonUtils.beanToJson(commonSource));
                        }
                        targetFileName = imagePath + "[" + (pageCount) + "].png";
                    }
                    if (needSwfConvert) {
                        docUtil.pdf2Swf(sourceFileName, cs);
                    }
                default:
                    break;
            }
            //完成,数据入库
            if (targetFileName != null) {
                this.finishDoc(commonSource, targetFileName);
            }
        } catch (Exception e){
            LogUtil.error(e, "doc转码失败 source:" + JsonUtils.beanToJson(commonSource));
        }
    }

    private String getDestFilePath(String sourcePath) {
        // 已改
        String h5Path = PropertiesUtil.getUpConfig("h5doc.savePath");
        String relativePath = sourcePath.substring(0, sourcePath.lastIndexOf("/") + 1);
        String firstPath = FileUtil.getFirstStorageDevicePath(sourcePath);
        return relativePath.replace(firstPath + "/private/cloud/", firstPath + h5Path);
    }
    private String getPathByBusType(Integer sourceType, String sourcePathPre) {
        // 上级方法已改
        String basePath;
        if (sourceType.equals(BusTypeEnum.CLOUD.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("cloud.savePath") : sourcePathPre;
        }else if (sourceType.equals(BusTypeEnum.ATTACHMENT.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("attachment.savePath") : sourcePathPre;
        }else if (sourceType.equals(BusTypeEnum.HOMEPAGE_IMAGE.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("homepage_image.savePath") : sourcePathPre;
        }else if (sourceType.equals(BusTypeEnum.HOMEPAGE_ATTACHMENT.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("homepage_attachment.savePath") : sourcePathPre;
        }else if (sourceType.equals(BusTypeEnum.HOMEPAGE_VIDEO.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("homepage_video.savePath") : sourcePathPre;
        }else if (sourceType.equals(BusTypeEnum.BROCHURE_ATTACHMENT.getTypeCode()) ) {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("brochure_attachment.savePath") : sourcePathPre;
        }else {
            basePath = sourcePathPre.equals("") ? PropertiesUtil.getUpConfig("attachment.savePath") : sourcePathPre;
        }
        return basePath;
    }

    /**
     * @Description: 视频转码
     * @params:  [sourcePath, fileType]
     * @Return:  void
     * @Modified:
     */

    private void video(CommonSource commonSource){

        LogUtil.info("doConvert" + commonSource.getFileID() + " video begin :  time=" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) );

        //获得保存文件的路径
        String basePath;
        String muPath;
        String muShowPath;
        String sourcePath;
        Long id;
        Integer videoLength = null;
        boolean isAttachment;

        id = commonSource.getSourceID();
        sourcePath = commonSource.getPath();
        String defaultPath = storageService.getDefaultStorageDevicePath(id, 2);
        //记录了业务目录pre的,使用pre
        basePath = defaultPath + this.getPathByBusType(commonSource.getSourceType(), "");

        muPath = defaultPath + PropertiesUtil.getUpConfig("attachment.muPath");
        muShowPath = defaultPath + PropertiesUtil.getUpConfig("attachment.muShowPath");
        isAttachment = true;

        Map<String, Object> videoInfoMap = VideoUtil.getVideoInfo(sourcePath);
        videoLength = VideoUtil.getVideoLength( sourcePath, videoInfoMap, false);

        if (videoLength <= 0){
            this.saveVideoConvertRecord(commonSource, "2", "文件损坏或者转换失败");
            return;
        }

        commonSource.setSourceLength(videoLength);
        // 转码类型 // 1 手动执行 2 定时任务执行
        if (ObjectUtils.isEmpty(commonSource.getOpType()) ||  !Arrays.asList("1","2").contains(commonSource.getOpType())) {
            if (videoLength > 3600 *6){
                LogUtil.error(" doConvert" + commonSource.getFileID() + " video  转码时长超过6小时, " + id + ", " +  sourcePath);
                if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType())) {
                    // 记录转码
                    this.saveVideoConvertRecord(commonSource, "2", "转码时长超过6小时");
                }
                return ;
            }
        }
        //文件名
        String fileName = sourcePath.substring(0,sourcePath.lastIndexOf(".")).replace(basePath, "");
        String fileNameWithoutPath = fileName.substring(fileName.lastIndexOf("/") + 1);
        //待转码的文件路径
        String filePath = sourcePath;
        //校验相同文件重复转码 redis
        if (!this.checkConvertFile(filePath, isAttachment)){
            LogUtil.info("doConvert" + commonSource.getFileID() + " video error checkCovertFile false  time=" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) );
            return;
        }
        //转换后待mp4文件路径
        //String mp4FilePath = basePath + fileName + ".mp4";
        //转换后的ts文件路径
        String tsPath = muPath + "ts/" + fileName + ".ts";
        //ts切片文件路径
        //String tsCutDirPath = muPath + "m3u8/" + fileName + "/";
        String tsCutPath = muPath + "m3u8/" + fileName + "/" + fileNameWithoutPath + "-%04d.ts";
        String muPre = GlobalConfig.m3u8ConvertUrlPlaceholder + muShowPath + "m3u8/" + fileName + "/";
        //m3u8索引文件路径
        String m3u8Path = basePath + fileName + ".m3u8";
        //视频截图文件路径
        String mediaPicPath ="";
        if (ObjectUtils.isEmpty(commonSource.getThumb())){
            mediaPicPath = muPath + "images/" + fileName + "/" + fileNameWithoutPath + ".jpg";    //设置上传视频截图的保存路径
        }else {
            File ff = new File(commonSource.getThumb());
            if (!ff.exists()) {
                mediaPicPath = commonSource.getThumb();    //设置上传视频截图的保存路径
            }
        }

        String smallPicPath = muPath + "images/" + fileName + "/" + "scut/%04d.jpg";    //设置上传视频截图的保存路径
        // 转换工具（ffmpeg）的存放路径
        String ffmpegPath = "ffmpeg";

        String remark = "";
        String state = null;
        try {
            boolean flag;    //转码成功与否的标记

            List<String> muPathList = new ArrayList<>();
            muPathList.add(tsPath);
            muPathList.add(tsCutPath);
            muPathList.add(m3u8Path);
            if (!ObjectUtils.isEmpty(mediaPicPath)){
                muPathList.add(mediaPicPath);
            }
            muPathList.add(smallPicPath);
            //创建目录
            VideoUtil.mkMuDir(muPathList);
            //key info
            String keyInfoPath = this.buildTsKey(id, filePath, false);
            LogUtil.info("doConvert" + commonSource.getFileID() + " video 开始转码 :  time=" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) );

            //mp4视频文件转码、切片、截图
            flag = VideoUtil.executeCodecs(ffmpegPath,filePath,tsCutPath,m3u8Path,mediaPicPath, keyInfoPath, muPre, fileNameWithoutPath, smallPicPath);
            LogUtil.info("doConvert" + commonSource.getFileID() + " video 切片截图完成 :  time=" + DateUtil.getYearMonthDayHMS(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) );

            if(flag){
                state = "1";
                //转码成功,向数据表中添加该视频信息
                LogUtil.info("doConvert" + commonSource.getFileID() + " video 视频转码成功： commonSource:" + JsonUtils.beanToJson(commonSource));
                //
                this.finishVideo(commonSource, m3u8Path, filePath, mediaPicPath, videoInfoMap);
                LogUtil.info("doConvert" + commonSource.getFileID() + " video 视频转码入库成功： commonSource:" + JsonUtils.beanToJson(commonSource));

                //cdn预热
                try {
//                    String tsPathWithPre = tsCutPath + "/" + fileNameWithoutPath;
//                    tencentApiUtil.pushUrlsCache(tsPathWithPre , cdnDomain);
                } catch (Exception e){

                }
            } else {

                remark = "文件打不开";
                state = "2";
                // 失败处理
                this.convertFail(commonSource, false);
            }
        } catch (Exception e) {
            remark = "文件打不开";
            state = "2";
            LogUtil.error(e, "转码失败 doConvert" + commonSource.getFileID() + "  commonSource:" + JsonUtils.beanToJson(commonSource));
            // 失败处理
            this.convertFail(commonSource, false);
        }
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType())) {
            // 记录转码
            this.saveVideoConvertRecord(commonSource, state, remark);
        }
        if (!ObjectUtils.isEmpty(commonSource.getNeedHashMd5()) && commonSource.getNeedHashMd5().intValue() == 1) {
            // 需修改文件md5
            this.updateFileHashMd5(commonSource);
        }
    }

    public void saveStateConvertIngRecord(Long sourceID, Long fileID, String state){
        Long convertID = commonConvertDao.checkExist(sourceID, ObjectUtils.isEmpty(fileID) ? 0L : fileID);
        if (!ObjectUtils.isEmpty(convertID)){
            try {
                commonConvertDao.updateStatus(convertID, state);
            }catch (Exception e){
                LogUtil.error(e, "记录修改为转码中，修改失败");
            }

        }
    }
    public void saveVideoConvertRecord(CommonSource commonSource, String state, String remark){
        if (ObjectUtils.isEmpty(commonSource.getSourceID())){
            LogUtil.info("saveVideoConvertRecord sourceID is null state=" + state + "，commonSource=" + JsonUtils.beanToJson(commonSource));
            return;
        }
        Long convertID = commonConvertDao.checkExist(commonSource.getSourceID(), commonSource.getFileID());
        String opType = ObjectUtils.isEmpty(commonSource.getOpType()) ? "0" : commonSource.getOpType();
        if (ObjectUtils.isEmpty(convertID)){
            CommonConvert commonConvert = new CommonConvert();
            commonConvert.setSourceID(commonSource.getSourceID());
            commonConvert.setFileID(commonSource.getFileID());
            commonConvert.setUserID(commonSource.getUserID());
            commonConvert.setName(ObjectUtils.isEmpty(commonSource.getName()) ? "" : commonSource.getName());
            commonConvert.setState(state);
            commonConvert.setTenantId(commonSource.getTenantId());
            commonConvert.setFrequencyCount("1".equals(opType) ? 1 : 0);
            commonConvert.setRemark(remark);
            try {
                commonConvertDao.insert(commonConvert.initializefield());
            }catch (Exception e){
                LogUtil.error(e, "记录转码失败");
            }
        }else {

            LogUtil.info("saveVideoConvertRecord convertID=" + convertID + "，opType=" + opType + "，state=" + state);
            try {
                switch (opType){
                    case "0":
                        // 上传
                        commonConvertDao.updateStatus(convertID, state);
                        break;
                    case "1":
                        // 手动执行
                        commonConvertDao.updateStatusAndCount(convertID, commonSource.getUserID(), state, remark);
                        break;
                    case "2":
                        // 定时任务执行
                        commonConvertDao.updateScheduleStatus(convertID, state);
                        break;
                }
            }catch (Exception e){
                LogUtil.error(e, "记录转码失败");
            }
        }
    }

    /**
     * @Description: 失败处理
     * @params:  [courseWare, commonSource]
     * @Return:  void
     * @Modified:
     */
    private void convertFail(CommonSource commonSource, boolean onlyEmail) {

        //
        //发送站内信
        try {
            this.sendNotice(commonSource, onlyEmail);
        } catch (Exception e){
            LogUtil.error(e, "发送转码失败站内信失败");
        }
       /* try {
            String idString = "";
            String code = "cl:";
            idString = code + commonSource.getFileID();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("courseId", idString);
            paramMap.put("time", new Date());
            paramMap.put("onlyEmail", true); //只发邮件了
           // String result = restTemplate.postForEntity("http://SCHOOL/service/sms/sendEncodeFailNotify", paramMap, String.class).getBody();
           // LogUtil.info("转码失败发送短信结果, ", result);
        } catch (Exception e){
            LogUtil.error(e, "请求发送短信失败, commonSource:" + JsonUtils.beanToJson(commonSource));
        }*/
    }

    private void sendNotice(CommonSource commonSource, boolean onlyEmail) {

        // String result = restTemplate.postForEntity("http://COMMON/service/common/sendSpecifiedNotice", sendNoticeServiceDTO, String.class).getBody();
        // LogUtil.info("发送转发失败站内信结果: " + result);
    }

    /**
     * @Description: 转码完成入库处理
     * @params:  [m3u8Path]
     * @Modified:
     */
    private void finishVideo(CommonSource commonSource, String m3u8Path, String mp4FilePath, String mediaPicPath, Map<String, Object>  videoInfoMap) {
        try {
            String checksum = commonSource.getHashMd5();
            Long fileSize = commonSource.getSize();
            //更新文件表
            //获取视频长度
            Integer playLength = VideoUtil.getVideoLength(mp4FilePath, videoInfoMap , false);
            //转码完成的实际长度
            Integer convertedLength = VideoUtil.getConvertedLength(m3u8Path);
            if (Math.abs(playLength - convertedLength) > 10){
                LogUtil.error("转码时长与视频信息时长差距过大, playLength:"
                        + playLength + ", convertedLength:" + convertedLength + ", " + JsonUtils.beanToJson(commonSource));
            }
            commonSource.setIsPreview(1);
            commonSource.setIsM3u8(1);
            commonSource.setPreviewUrl(m3u8Path);
            if (!ObjectUtils.isEmpty(mediaPicPath)) {
                commonSource.setThumb(mediaPicPath);
            }
            commonSource.setSourceLength(playLength);

            commonSource.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
            commonSource.setConvertSize(ObjectUtils.isEmpty(commonSource.getFileConvertSize()) ? 0L : commonSource.getFileConvertSize());
            // 缩略图
            if (!ObjectUtils.isEmpty(commonSource.getThumb())) {
                try {
                    File f = new File(commonSource.getThumb());
                    if (f.exists()) {
                        commonSource.setThumbSize(commonSource.getThumbSize() + f.length());
                    }
                    f = new File(m3u8Path);
                    if (f.exists()) {
                        String fileContent = FileUtil.getFileContent(m3u8Path);
                        if (fileContent != null) {
                            // 转码老域名处理url
                            commonSource.setConvertSize(f.length() + getM3u8LineSize(m3u8Path));
                        }
                    }
                } catch (Exception e) {
                }
            }

            String resolution = "";
            String frame = "";
            //若是视频且分辨率为空时
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType())
                    && StringUtil.isEmpty(commonSource.getResolution())) {
                int[] heightAndWidth = VideoUtil.getHeightAndWidth(commonSource.getPath(), videoInfoMap, false);
                resolution = "1280*720";
                if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
                    resolution = heightAndWidth[1] + "*" + heightAndWidth[0];
                }
            }

            if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType()) &&
                    (StringUtil.isEmpty(commonSource.getResolution()) || StringUtil.isEmpty(commonSource.getFrame()))){
                int[] heightAndWidthAndFrame = VideoUtil.getHeightAndWidthAndFrame(commonSource.getPath(), videoInfoMap, false);
                if (StringUtil.isEmpty(commonSource.getResolution())) {
                    resolution = "1280*720";
                    if (heightAndWidthAndFrame[0] != 0 && heightAndWidthAndFrame[1] != 0) {
                        resolution = heightAndWidthAndFrame[1] + "*" + heightAndWidthAndFrame[0];
                    }
                }
                if (StringUtil.isEmpty(commonSource.getFrame())){
                    frame = heightAndWidthAndFrame[2] + "";
                }
            }
            ioFileDao.updateVideoConvert(commonSource);
            List<Long> fileIDList = new ArrayList<>();
            fileIDList.add(commonSource.getFileID());

            if (!ObjectUtils.isEmpty(checksum) && fileSize > 0) {
                //获取缺失转码信息的相同附件
                List<Long> toUpdateCommonSourceList = ioFileDao.getSameSourceEmptyInfo(checksum, fileSize);

                //更新相同的附件 转码信息 todo
                if (!CollectionUtils.isEmpty(toUpdateCommonSourceList) && toUpdateCommonSourceList.size() > 0) {
                    fileIDList.addAll(toUpdateCommonSourceList);
                    ioFileDao.updateSameFile(commonSource, toUpdateCommonSourceList);
                }
            }


            /** 更新视频其他信息 */
            this.updatePreviewUrlValue2(fileIDList, m3u8Path, mediaPicPath, playLength, resolution, frame);
        } catch (Exception e){
            LogUtil.error(e, "转码入库失败, commonSource:"+ JsonUtils.beanToJson(commonSource));
        }
    }

    public static Long getM3u8LineSize(String path) {
        Long size = 0L;
        List<String> pathList = FileUtil.getM3u8LineList(path);
        if (!CollectionUtils.isEmpty(pathList)){
            File file = null;
            for (String p : pathList){
                String pf = p.replace(GlobalConfig.oldInnerServer, "").replace(GlobalConfig.m3u8ConvertUrlPlaceholder, "");
                file = new File(pf);
                if (file.exists()){
                    size = size + file.length();
                }
            }
        }
        return size;
    }

    private boolean checkConvertFile(String filePath, boolean isAttachment) {
        String key = GlobalConfig.CONVERT_FILE_REDIS_KEY_PRE + filePath;
        String exists = stringRedisTemplate.opsForValue().get(key);
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        Long pStartTime = bean.getStartTime();
        //有转码记录,且项目运行时长超过10分钟
        if (exists != null && System.currentTimeMillis() - pStartTime > GlobalConfig.RUN_TIME_OFFSET){
            LogUtil.info("相同文件重复转码：" + filePath + ", 是否附件" + isAttachment);
            return false;
        } else {
            LogUtil.info("记录转码文件到redis：" + filePath + ", 是否附件" + isAttachment);
            stringRedisTemplate.opsForValue().set(key, isAttachment ? "1" : "0", GlobalConfig.CONVERT_FILE_REDIS_TTL, TimeUnit.SECONDS);
        }
        return true;
    }

    private String buildTsKey(Long id, String filePath, boolean isStrKey) throws Exception {
        String preUrl = filePath.substring(0, filePath.lastIndexOf("."));
        String keyPath = preUrl + ".key";
        String keyInfoPath = preUrl + ".keyinfo";
        String keyUri = VideoUtil.buildKeyUri(id, filePath);
        if (isStrKey){
            VideoUtil.buildTsKey(buildKey2, keyUri, keyPath, keyInfoPath);
        } else {
            VideoUtil.buildTsKey(buildKey, keyUri, keyPath, keyInfoPath);
        }
        return keyInfoPath;
    }

    /**
     * @Description: 文档类转码成功入库
     * @params:  [courseWare, docPath]
     * @Return:  void
     * @Modified:
     */
    private void finishDoc(CommonSource commonSource, String docPath){
        try {
            //更新文件表

            commonSource.setIsPreview(1);
            commonSource.setAppPreview(1);
            commonSource.setAppPreviewUrl(docPath);
            //更新文件表
            ioFileDao.updateDocConvert(commonSource);

            String checksum = commonSource.getHashMd5();
            Long fileSize = commonSource.getSize();
            this.updateSameDoc(checksum, fileSize, docPath, false, commonSource.getFileID());

            LogUtil.info("文档类转码入库成功,  commonsource" + JsonUtils.beanToJson(commonSource) );
        } catch (Exception e){
            LogUtil.error("转码入库失败,  commonsource" + JsonUtils.beanToJson(commonSource));
        }
    }

    private void updateSameDoc(String checksum, Long fileSize, String docPath, Boolean isSwf, Long fileID) {
        //获取缺失转码信息的相同附件
        List<Long> toUpdateCommonSourceList = ioFileDao.getSameSourceEmptyInfoDoc(checksum, fileSize);
        if (CollectionUtils.isEmpty(toUpdateCommonSourceList)){
            // 没有则只更新此文件的 appPreviewUrl
            this.updatePreviewUrlValue(Arrays.asList(fileID), docPath, isSwf);
            return;
        }
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("now", new Date());
        updateMap.put("fileSize", fileSize);
        updateMap.put("docPath", docPath);
        //  app_preview_url = #{appPreviewUrl},
        //更新相同的附件 转码信息
        if (isSwf && toUpdateCommonSourceList.size() > 0){
            // preview_url = #{map.docPath},
            ioFileDao.updateSameFileSwf(updateMap, toUpdateCommonSourceList);
            this.updatePreviewUrlValue(toUpdateCommonSourceList, docPath, isSwf);
            return;
        }
        if (toUpdateCommonSourceList.size() > 0) {
            // app_preview_url = #{map.docPath},
            ioFileDao.updateSameFileDoc(updateMap, toUpdateCommonSourceList);
            this.updatePreviewUrlValue(toUpdateCommonSourceList, docPath, isSwf);
        }
    }

    private void updatePreviewUrlValue(List<Long> fileIDList, String docPath, boolean isSwf){
        if (CollectionUtils.isEmpty(fileIDList)){
            return;
        }
        List<IOFileMeta> list = ioFileDao.getFileUrlValueList(fileIDList);
        if (!CollectionUtils.isEmpty(list)){
            try {
                FileMetaVo fileMetaVo = null;
                for (IOFileMeta meta : list) {
                    if (ObjectUtils.isEmpty(meta.getValue())){
                        fileMetaVo = new FileMetaVo();
                    }else {
                        fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
                    }
                    if (isSwf){
                        fileMetaVo.setViewUrl(docPath);
                    }else {
                        fileMetaVo.setAppViewUrl(docPath);
                    }
                    meta.setValue(JsonUtils.beanToJson(fileMetaVo));
                }
                ioFileDao.updateFileUrlValue(list);
            }catch (Exception e){
                LogUtil.error(e, "updatePreviewUrlValue fileMeta error " + JsonUtils.beanToJson(list));
            }
        }
    }
    private void updatePreviewUrlValue2(List<Long> fileIDList, String docPath, String thumb, Integer convertedLength, String resolution, String frame){
        if (CollectionUtils.isEmpty(fileIDList)){
            return;
        }
        List<IOFileMeta> list = ioFileDao.getFileUrlValueList(fileIDList);
        if (!CollectionUtils.isEmpty(list)){
            try {
                FileMetaVo fileMetaVo = null;
                for (IOFileMeta meta : list) {
                    if (ObjectUtils.isEmpty(meta.getValue())){
                        fileMetaVo = new FileMetaVo();
                    }else {
                        fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
                    }
                    if (!ObjectUtils.isEmpty(convertedLength)) {
                        fileMetaVo.setLength(convertedLength);
                    }
                    if (!ObjectUtils.isEmpty(docPath)) {
                        fileMetaVo.setViewUrl(docPath);
                    }
                    if (ObjectUtils.isEmpty(fileMetaVo.getResolution()) && !ObjectUtils.isEmpty(resolution)) {
                        fileMetaVo.setResolution(resolution);
                    }
                    if (ObjectUtils.isEmpty(fileMetaVo.getFrame()) && !ObjectUtils.isEmpty(frame)) {
                        fileMetaVo.setFrame(frame);
                    }
                    if (!ObjectUtils.isEmpty(thumb)){
                        if (ObjectUtils.isEmpty(fileMetaVo.getThumb())){
                            fileMetaVo.setThumb(thumb);
                        }else {
                            if (!fileMetaVo.getThumb().equals(thumb)){
                                try {
                                    File f = new File(fileMetaVo.getThumb());
                                    if (f.exists()){
                                        //new File(thumb).delete();
                                    }else {
                                        fileMetaVo.setThumb(thumb);
                                    }
                                }catch (Exception e){
                                    LogUtil.error(e, "delete file error f=" +thumb + "，fileMetaVo= " + JsonUtils.beanToJson(fileMetaVo));
                                }
                            }
                        }
                    }
                    meta.setValue(JsonUtils.beanToJson(fileMetaVo));
                }
                ioFileDao.updateFileUrlValue(list);
            }catch (Exception e){
                LogUtil.error(e, "updatePreviewUrlValue2 fileMeta error " + JsonUtils.beanToJson(list));
            }
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void updateYZPreviewUrlValue(List<Long> fileIDList, String urlData, boolean isValue){
        updateYZPreviewUrlValue(fileIDList, urlData);
    }

    private void updateYZPreviewUrlValue(List<Long> fileIDList, String yzViewData){
        if (CollectionUtils.isEmpty(fileIDList)){
            return;
        }
        List<IOFileMeta> list = ioFileDao.getFileUrlValueList(fileIDList);
        if (!CollectionUtils.isEmpty(list)){
            try {
                FileMetaVo fileMetaVo = null;
                for (IOFileMeta meta : list) {
                    if (ObjectUtils.isEmpty(meta.getValue())){
                        fileMetaVo = new FileMetaVo();
                    }else {
                        fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
                    }
                    if (!ObjectUtils.isEmpty(yzViewData)) {
                        fileMetaVo.setYzViewData(yzViewData);
                    }
                    meta.setValue(JsonUtils.beanToJson(fileMetaVo));
                }
                ioFileDao.updateFileUrlValue(list);
            }catch (Exception e){
                LogUtil.error(e, "updateYZPreviewUrlValue fileMeta error " + JsonUtils.beanToJson(list));
            }
        }
    }

    private void updateAudioValue(List<Long> fileIDList, String sourceMp3Path){
        if (CollectionUtils.isEmpty(fileIDList)){
            return;
        }
        Integer length = VideoUtil.getVideoLength(sourceMp3Path);

        List<IOFileMeta> list = ioFileDao.getFileUrlValueList(fileIDList);
        if (!CollectionUtils.isEmpty(list)){
            try {
                FileMetaVo fileMetaVo = null;
                for (IOFileMeta meta : list) {
                    if (ObjectUtils.isEmpty(meta.getValue())){
                        fileMetaVo = new FileMetaVo();
                    }else {
                        fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
                    }
                    if (!ObjectUtils.isEmpty(length) && length > 0){
                        fileMetaVo.setLength(length);
                    }
                    if (!ObjectUtils.isEmpty(sourceMp3Path)) {
                        fileMetaVo.setH264Path(sourceMp3Path);
                    }
                    meta.setValue(JsonUtils.beanToJson(fileMetaVo));
                }
                ioFileDao.updateFileUrlValue(list);
            }catch (Exception e){
                LogUtil.error(e, "updatePreviewUrlValue2 fileMeta error " + JsonUtils.beanToJson(list));
            }
        }
    }

    public void setCheckFileOther(String checksum, Long fileSize, String audioPicPath) {

        //获取缺失转码信息的相同附件
        List<Long> toUpdateCommonSourceList = ioFileDao.getSameSourceEmptyInfo(checksum, fileSize);

        if (!CollectionUtils.isEmpty(toUpdateCommonSourceList) && toUpdateCommonSourceList.size() > 1) {
            /** 更新file其他信息 */
            this.updatePreviewUrlValue2(toUpdateCommonSourceList, "", audioPicPath, null, null, null);
        }
    }


    /**
     * @description: 转换音频方法
     * @param prefix
     * @param commonSource
     * @return void
     */
    private void convertAudioCommon(String prefix, CommonSource commonSource) {
        boolean isAudio = Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(commonSource.getFileType());
        if (isAudio){
            if (commonSource.getIsH264Preview() != null && commonSource.getIsH264Preview().equals(1)){
                return;
            }
            //获得保存文件的路径
            //String basePath = "";
            String sourcePath;
            sourcePath = commonSource.getPath();
            //记录了业务目录pre的,使用pre
            //basePath = this.getPathByBusType(commonSource.getSourceType(), "");
            //文件名
            String fileName = sourcePath.substring(0,sourcePath.lastIndexOf("."));
            //待转码的文件路径
            String filePath = commonSource.getPath();
            //
            if (!this.checkConvertFile(filePath, true)){
                return;
            }
            Long convertSize = 0L;
            //转换后待mp3文件路径
            String mp3FilePath = "";
            //若是mp3，则无需要转码，直接用原有的
            if("mp3".equals(commonSource.getFileType().toLowerCase())) {
                mp3FilePath = commonSource.getPath();
            } else { //转码下 已改
                mp3FilePath = storageService.getDefaultStorageDevicePath(commonSource.getSourceID(),2) + (PropertiesUtil.getUpConfig("audio.savePath") + fileName + ".mp3").replace("/private/cloud/", "");
                List<String> audioPathList = new ArrayList<>();
                audioPathList.add(mp3FilePath);
                //创建目录
                VideoUtil.mkMuDir(audioPathList);

                //转换工具（ffmpeg）的存放路径
                String ffmpegPath = "ffmpeg";
                //
                boolean flag = VideoUtil.convertToMp3(prefix, ffmpegPath, filePath, mp3FilePath);
                if(!flag){
                    //转码失败的处理
                    this.convertFail(commonSource, false);
                    //
                    return;
                }
                try {
                    File m = new File(mp3FilePath);
                    if (m.exists()){
                        convertSize = m.length();
                    }
                }catch (Exception e){
                }

                //转码成功
                LogUtil.info(prefix+ "音频转码成功。commonSource:" + JsonUtils.beanToJson(commonSource));
            }
            //音频转码成功后的处理
            this.finishAudio(prefix, commonSource, mp3FilePath, convertSize);
            //
            LogUtil.info(prefix+ "音频转码入库成功。commonSource:" + JsonUtils.beanToJson(commonSource));
        } else {
            LogUtil.error(prefix + "不支持的文件类型里：" + commonSource.getFileType()
                    + "，参数：" + JsonUtils.beanToJson(commonSource));
        }
        if (!ObjectUtils.isEmpty(commonSource.getNeedHashMd5()) && commonSource.getNeedHashMd5().intValue() == 1) {
            // 需修改文件md5
            this.updateFileHashMd5(commonSource);
        }
    }

    /**
     * @description: 音频转码成功后的处理
     * @param prefix
     * @param commonSource
     * @param sourceMp3Path
     * @return void
     */
    private void finishAudio(String prefix, CommonSource commonSource, String sourceMp3Path, Long convertSize) {

        if (convertSize > 0){
            commonSource.setConvertSize(convertSize);
            try {
                ioSourceDao.updateSourceConvertSize(commonSource.getSourceID(), convertSize);
            }catch (Exception e){
                LogUtil.error(e, "finishAudio updateSourceConvertSize error");
            }
        }

        try {
            //更新文件表
            commonSource.setIsH264Preview(1);
            commonSource.setH264Path(sourceMp3Path);
            //
            this.ioFileDao.updateAudioConvert(commonSource);
            //特定业务的处理

            this.updateAudioValue(Arrays.asList(commonSource.getFileID()), sourceMp3Path);

        } catch (Exception e){
            LogUtil.error(e, prefix + "转码入库失败, commonSource:"+ JsonUtils.beanToJson(commonSource)
                    +  "mp3FilePath:"+ sourceMp3Path);
        }
    }

    public void convertCameraToImgCommon(String prefix, CommonSource commonSource) {
        boolean isCamera = Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType());
        if (isCamera){
            if (commonSource.getIsH264Preview() != null && commonSource.getIsH264Preview().equals(1)){
                return;
            }

            //获得保存文件的路径
            //String basePath = "";
            String sourcePath = commonSource.getPath();
            String suffix = FileUtil.getFileExtension(sourcePath);
            //待转码的文件路径
            String filePath = commonSource.getPath();
            //
            Long convertSize = 0L;
            boolean flag = CameraUtil.cameraToImg(prefix, filePath);
            //转换后待img文件路径
            String finishFilePath = filePath + ".thumb.jpg";
            File f = new File(finishFilePath);
            if (!f.exists()){
                String tempPath = finishFilePath.replace(".thumb.jpg",".thumb.ppm");
                f = new File(tempPath);
                if (!f.exists()){
                    tempPath = tempPath.replace(".thumb.ppm",".ppm");
                    f = new File(tempPath);
                    if (!f.exists()){
                        LogUtil.error("convertCameraToImgCommon .thumb.jpg and .thumb.ppm and .ppm is all exist  sourcePath=" + sourcePath);
                        return;
                    }
                }
                flag = CameraUtil.ppmConvertToJpg(tempPath, finishFilePath);
                if (flag) {
                    try {
                        new File(tempPath).delete();
                    } catch (Exception e) {
                    }
                }
            }


            if(!flag){
                //转码失败的处理
                this.convertFail(commonSource, false);
                //
                return;
            }
            try {
                File m = new File(finishFilePath);
                if (m.exists()){
                    convertSize = m.length();
                }
            }catch (Exception e){
            }

            //转码成功
            LogUtil.info(prefix+ "相机文件成功。commonSource:" + JsonUtils.beanToJson(commonSource));
            //转码成功后的处理
            CameraUtil.finishCameraToImg(prefix, commonSource, finishFilePath, convertSize, ioSourceDao, ioFileDao);
            //

            ImageUtil.createThumb(finishFilePath, "cloud", "", null,
                    "" , stringRedisTemplate, null, false);

            LogUtil.info(prefix+ " 入库成功。commonSource:" + JsonUtils.beanToJson(commonSource));
        } else {
            LogUtil.error(prefix + "不支持的文件类型里：" + commonSource.getFileType()
                    + "，参数：" + JsonUtils.beanToJson(commonSource));
        }

    }


}
