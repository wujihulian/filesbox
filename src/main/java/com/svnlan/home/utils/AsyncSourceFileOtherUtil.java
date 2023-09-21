package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.MetaEnum;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceEventDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.*;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.vo.FileMetaVo;
import com.svnlan.utils.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/7 13:42
 */
@Component
public class AsyncSourceFileOtherUtil {

    @Resource
    BusTypeHandleService busTypeHandleService;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    IoSourceEventDao ioSourceEventDao;

    @Async(value = "asyncTaskExecutor")
    public void asyncAddFileMeta(CommonSource commonSource, IOFile ioFile) {

        IOFileMeta fileMeta = new IOFileMeta();
        fileMeta.setFileID(ioFile.getFileID());
        fileMeta.setKey("fileInfoMore");
        FileMetaVo fileMetaVo = new FileMetaVo();
        fileMetaVo.setThumb(ObjectUtils.isEmpty(commonSource.getThumb()) ? "" : commonSource.getThumb());
        fileMetaVo.setAppViewUrl(ObjectUtils.isEmpty(commonSource.getAppPreviewUrl()) ? "" : commonSource.getAppPreviewUrl());
        fileMetaVo.setH264Path(ObjectUtils.isEmpty(commonSource.getH264Path()) ? "" : commonSource.getH264Path());
        fileMetaVo.setResolution(ObjectUtils.isEmpty(commonSource.getResolution()) ? "" : commonSource.getResolution());
        fileMetaVo.setViewUrl(ObjectUtils.isEmpty(commonSource.getPreviewUrl()) ? "" : commonSource.getPreviewUrl());
        fileMetaVo.setLength(ObjectUtils.isEmpty(commonSource.getSourceLength()) ? 0 : commonSource.getSourceLength());
        if (Arrays.asList(GlobalConfig.AUDIO_VIDEO_SHOW_TYPE_ARR).contains(commonSource.getFileType().toLowerCase())
                && !ObjectUtils.isEmpty(commonSource.getPath())) {
            File f = new File(commonSource.getPath());
            if (f.exists()) {
                if (StringUtil.isEmpty(fileMetaVo.getThumb()) && Arrays.asList(1, 2).contains(commonSource.getTargetType())) {
                    UploadDTO uploadDTO = new UploadDTO();
                    uploadDTO.setBusType("cloud");
                    busTypeHandleService.doForWareAndAttachment(uploadDTO, false, commonSource);
                    fileMetaVo.setThumb(ObjectUtils.isEmpty(commonSource.getThumb()) ? "" : commonSource.getThumb());
                }
            }
            // 其他的异步执行
        }
        fileMeta.setValue(!ObjectUtils.isEmpty(fileMetaVo) ? JsonUtils.beanToJson(fileMetaVo) : "");
        try {
            ioFileDao.insertMeta(fileMeta);
        } catch (Exception e) {
            LogUtil.error(e, " setUserDefaultSource insertMeta error");
        }


        if (Arrays.asList(GlobalConfig.AUDIO_VIDEO_SHOW_TYPE_ARR).contains(commonSource.getFileType().toLowerCase())
                && !ObjectUtils.isEmpty(commonSource.getPath())) {
            asyncUpdateH264InfoOperate(commonSource, fileMeta);
        }

    }

    @Async(value = "asyncTaskExecutor")
    public void asyncUpdateH264InfoOperate(CommonSource commonSource, IOFileMeta meta ) {

        FileMetaVo fileMetaVo = null;
        if (!ObjectUtils.isEmpty(meta) && !ObjectUtils.isEmpty(meta.getValue())){
            fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
        }
        if (ObjectUtils.isEmpty(fileMetaVo)){
            fileMetaVo = new FileMetaVo();
        }
        boolean check = false;

        File file = new File(commonSource.getPath());
        if (file.exists()) {
            if (StringUtil.isEmpty(fileMetaVo.getThumb()) && Arrays.asList(1, 2).contains(commonSource.getTargetType())) {
                UploadDTO uploadDTO = new UploadDTO();
                uploadDTO.setBusType("cloud");
                busTypeHandleService.doForWareAndAttachment(uploadDTO, false, commonSource);
                fileMetaVo.setThumb(ObjectUtils.isEmpty(commonSource.getThumb()) ? "" : commonSource.getThumb());
            }
            check = true;
            Map<String, Object> videoInfoMap = VideoUtil.getVideoInfo(commonSource.getPath());
            Map<String, String> basicMap = VideoUtil.getVideoBasicInfoMap(commonSource.getPath(), videoInfoMap, false);
            if (!ObjectUtils.isEmpty(basicMap)) {
                if (basicMap.containsKey("codecName")) {
                    fileMetaVo.setCodecName(basicMap.get("codecName"));
                }
                if (basicMap.containsKey("avgFrameRate")) {
                    fileMetaVo.setAvgFrameRate(basicMap.get("avgFrameRate"));
                }
                if (basicMap.containsKey("frameRate")) {
                    fileMetaVo.setFrameRate(basicMap.get("frameRate"));
                }
                if (basicMap.containsKey("sampleRate")) {
                    fileMetaVo.setSampleRate(basicMap.get("sampleRate"));
                }
                if (basicMap.containsKey("channels")) {
                    fileMetaVo.setChannels(basicMap.get("channels"));
                }
                if (basicMap.containsKey("audioCodecName")) {
                    fileMetaVo.setAudioCodecName(basicMap.get("audioCodecName"));
                }
            }
            if (ObjectUtils.isEmpty(fileMetaVo.getResolution())){
                int[] heightAndWidth = VideoUtil.getHeightAndWidth(commonSource.getPath(), videoInfoMap, false);
                String resolution = "1280*720";
                if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
                    resolution = heightAndWidth[1] + "*" + heightAndWidth[0];
                }
                commonSource.setResolution(resolution);
                fileMetaVo.setResolution(resolution);
            }
            Integer length = VideoUtil.getVideoLength(videoInfoMap);
            fileMetaVo.setLength(length);
        }

        if (check && !ObjectUtils.isEmpty(fileMetaVo)) {
            ioFileDao.updateOneFileUrlValue(commonSource.getFileID(), JsonUtils.beanToJson(fileMetaVo));
        }
    }


    @Async(value = "asyncTaskExecutor")
    public void asyncUpdateH264InfoOperateList(List<CommonSource> list) {
        Map<String, Object> videoInfoMap = VideoUtil.getVideoInfo(list.get(0).getPath());

        for (CommonSource commonSource:list) {

            boolean check = false;
            IOFileMeta meta = ioFileDao.getFileValue(commonSource.getSourceID());
            FileMetaVo fileMetaVo = null;
            if (!ObjectUtils.isEmpty(meta) && !ObjectUtils.isEmpty(meta.getValue())) {
                fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
            }
            if (ObjectUtils.isEmpty(fileMetaVo)) {
                fileMetaVo = new FileMetaVo();
            }
            File file = new File(commonSource.getPath());
            if (file.exists()) {
                if (StringUtil.isEmpty(fileMetaVo.getThumb()) && Arrays.asList(1, 2).contains(commonSource.getTargetType())) {
                    UploadDTO uploadDTO = new UploadDTO();
                    uploadDTO.setBusType("cloud");
                    busTypeHandleService.doForWareAndAttachment(uploadDTO, false, commonSource);
                    fileMetaVo.setThumb(ObjectUtils.isEmpty(commonSource.getThumb()) ? "" : commonSource.getThumb());
                }
                check = true;
                Map<String, String> basicMap = VideoUtil.getVideoBasicInfoMap(commonSource.getPath(), videoInfoMap, false);
                if (!ObjectUtils.isEmpty(basicMap)) {
                    if (basicMap.containsKey("codecName")) {
                        fileMetaVo.setCodecName(basicMap.get("codecName"));
                    }
                    if (basicMap.containsKey("avgFrameRate")) {
                        fileMetaVo.setAvgFrameRate(basicMap.get("avgFrameRate"));
                    }
                    if (basicMap.containsKey("frameRate")) {
                        fileMetaVo.setFrameRate(basicMap.get("frameRate"));
                    }
                    if (basicMap.containsKey("sampleRate")) {
                        fileMetaVo.setSampleRate(basicMap.get("sampleRate"));
                    }
                    if (basicMap.containsKey("channels")) {
                        fileMetaVo.setChannels(basicMap.get("channels"));
                    }
                    if (basicMap.containsKey("audioCodecName")) {
                        fileMetaVo.setAudioCodecName(basicMap.get("audioCodecName"));
                    }
                }
                if (ObjectUtils.isEmpty(fileMetaVo.getResolution())) {
                    int[] heightAndWidth = VideoUtil.getHeightAndWidth(commonSource.getPath(), videoInfoMap, false);
                    String resolution = "1280*720";
                    if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
                        resolution = heightAndWidth[1] + "*" + heightAndWidth[0];
                    }
                    commonSource.setResolution(resolution);
                    fileMetaVo.setResolution(resolution);
                }
            }

            if (check && !ObjectUtils.isEmpty(fileMetaVo)) {
                ioFileDao.updateOneFileUrlValue(commonSource.getFileID(), JsonUtils.beanToJson(fileMetaVo));
            }
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void asyncAddSourceMeta(CommonSource source) {
        List<IOSourceMeta> pyList = new ArrayList<>();
        pyList.add(new IOSourceMeta(source.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(source.getName())));
        pyList.add(new IOSourceMeta(source.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(source.getName())));

        if (!CollectionUtils.isEmpty(pyList)) {
            // 拼音
            try {
                ioSourceMetaDao.batchInsert(pyList);
            } catch (Exception e) {
                LogUtil.error(e, " setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
            }
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void asyncAddSourceEvent(IoSourceEvent event) {
        try {
            ioSourceEventDao.insert(event);
        } catch (Exception e) {
            LogUtil.error(e, " addSourceEvent error paramList=" + JsonUtils.beanToJson(event));
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void asyncAddSourceEventList(List<IoSourceEvent> list) {
        try {
            ioSourceEventDao.batchInsert(list);
        } catch (Exception e) {
            LogUtil.error(e, " addSourceEventList error paramList=" + JsonUtils.beanToJson(list));
        }
    }
}
