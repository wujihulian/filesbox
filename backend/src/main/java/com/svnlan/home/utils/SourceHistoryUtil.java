package com.svnlan.home.utils;

import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/25 13:30
 */
@Component
public class SourceHistoryUtil {

    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;


    public void changeCheckSourceHistory(IoSourceHistory newHistory , IoSourceHistory ioSourceHistory){
        IoSourceHistory org = ioSourceHistoryDao.getHistoryInfoByFileId(ioSourceHistory.getSourceID(), ioSourceHistory.getFileID());
        // 添加历史记录
        try {
            if (!ObjectUtils.isEmpty(org)){
                String detail = ObjectUtils.isEmpty(ioSourceHistory.getDetail()) ? "" : ioSourceHistory.getDetail();

                ioSourceHistoryDao.updateSize(org.getId(), ioSourceHistory.getSize(),detail);
            }else {
                ioSourceHistoryDao.insert(ioSourceHistory);
            }
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 ioSourceHistory=" + JsonUtils.beanToJson(ioSourceHistory));
        }

        if (ObjectUtils.isEmpty(newHistory) || ObjectUtils.isEmpty(newHistory.getFileID())){
            return;
        }
        newHistory.setSize(ObjectUtils.isEmpty(newHistory.getSize()) ? 0L : newHistory.getSize());
        try {
            ioSourceHistoryDao.insert(newHistory.initializefield());
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 newHistory=" + JsonUtils.beanToJson(ioSourceHistory));
        }
    }

    public String getRepeatMd5SourceId(Long startTime, StringRedisTemplate stringRedisTemplate, String uploadSourceIdKey){
        String md5SourceId = "0";

        long diff = DateUtil.getTimeDiffSeconds(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()), LocalDateTime.now());
        int retryIndexCheck = 0;
        //失败重试30次
        while(diff < 295 || retryIndexCheck < 150) {
            md5SourceId = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
            retryIndexCheck++;
            diff = DateUtil.getTimeDiffSeconds(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()), LocalDateTime.now());
            if (!ObjectUtils.isEmpty(md5SourceId) && !"0".equals(md5SourceId)){
                break;
            }
            //若失败，则等待2m后再重试
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return md5SourceId;
    }
}
