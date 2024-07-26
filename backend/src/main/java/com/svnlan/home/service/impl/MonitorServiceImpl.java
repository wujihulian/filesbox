package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.service.MonitorService;
import com.svnlan.user.domain.Email;
import com.svnlan.user.service.MailService;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@Service
public class MonitorServiceImpl implements MonitorService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RestTemplate restTemplate;
    @Resource
    MailService mailService;
    @Value("${mail.encode.fail}")
    private String convertFailMail;


    /**
       * @Description: 转码时长监控
       * @params:  [resultMap]
       * @Return:  java.lang.String
       * @Modified:
       */
    @Override
    public String videoConvertTime(Map<String, Object> resultMap) {
        long nowTimestamp = System.currentTimeMillis();
        long startTimestamp = nowTimestamp - 7200 * 1000;
        Set<String> resultSet = stringRedisTemplate.opsForZSet().rangeByScore(GlobalConfig.CONVERT_TIME_KEY, startTimestamp, nowTimestamp);
        if (CollectionUtils.isEmpty(resultSet)){
            return "最近2小时发起的转码, 没有还在继续的";
        }
        //40分钟以上的
        List<String> fortyMinutesList = new ArrayList<>();
        //一小时以上的
        List<String> hourList = new ArrayList<>();
        //一个半小时以上的
        List<String> hourAndHalfList = new ArrayList<>();
        List<String> otherList = new ArrayList<>();
        for (String result : resultSet){
            String timeStr = result.substring(result.length() - 13);
            long timestamp = Long.valueOf(timeStr);
            if (nowTimestamp - timestamp > 5400 * 1000){//1个半小时
                hourAndHalfList.add(result);
            } else if (nowTimestamp - timestamp > 3600 * 1000){//1个小时
                hourList.add(result);
            } else if (nowTimestamp - timestamp > 2400 * 1000){//40分钟
                fortyMinutesList.add(result);
            } else {
                otherList.add(result);
            }
        }
        resultMap.put("fortyMinutesList", fortyMinutesList);
        resultMap.put("hourList", hourList);
        resultMap.put("hourAndHalfList", hourAndHalfList);
        resultMap.put("otherList", otherList);

        if (!ObjectUtils.isEmpty(convertFailMail)){
            try {//发送邮件
                if (fortyMinutesList.size() > 0 || hourList.size() > 0 || hourAndHalfList.size() > 0){

                    String text = "转码时长报表: 1小时, " + JsonUtils.beanToJson(hourList)
                            + ",  1个半小时, " + JsonUtils.beanToJson(hourAndHalfList) + ", 40分钟, " + JsonUtils.beanToJson(fortyMinutesList);

                    String time = DateUtil.getYearMonthDayHMS(new Date(), "yyyy-MM-dd HH:mm:ss");

                    Email email = new Email();
                    email.setSubject("【转码异常】");
                    email.setContent("视频ID:  " + text + "  转码失败，失败时间:  " + time);

                    try {
                        mailService.sendEncodeFailNotify(email);
                        LogUtil.info("videoConvertTime sendEncodeFailNotify 转码时长报表发送成功, paramMap=" + JsonUtils.beanToJson(email));
                    } catch (Exception e) {
                        LogUtil.error(e, "videoConvertTime sendEncodeFailNotify=>邮箱发送异常 paramMap=" + JsonUtils.beanToJson(email));
                    }
                }
            } catch (Exception e){
                LogUtil.error(e, "videoConvertTime 转码时长报表邮件失败");
            }
        }

        return "转码时长报表 40分钟 " + fortyMinutesList.size() + "个" + ", "
                + "1小时 " + hourList.size() + "个" + ", "
                + "1个半小时 " + hourAndHalfList.size() + "个" + ", "
                + "其他 " + otherList.size() + "个";
    }
}
