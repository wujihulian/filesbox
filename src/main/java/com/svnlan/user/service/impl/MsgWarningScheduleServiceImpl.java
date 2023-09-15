package com.svnlan.user.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.Email;
import com.svnlan.user.domain.PostContent;
import com.svnlan.user.service.*;
import com.svnlan.user.tools.DingTalkDUtil;
import com.svnlan.user.tools.ShellExecTool;
import com.svnlan.user.tools.WeComPushUtil;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.DateDUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/7 17:12
 */
@Service
public class MsgWarningScheduleServiceImpl implements MsgWarningScheduleService {

    @Resource
    SystemSettingService systemSettingService;
    @Resource
    UserService userService;
    @Resource
    GroupService groupService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemOptionDao systemOptionDao;
    @Resource
    MailService mailService;

    // 磁盘总大小
    @Value("${disk.default.size}")
    private Long diskDefaultSize;

    @Override
    public String sendMsgWarming(PluginListVo pluginListVo){
        StringBuilder sb = new StringBuilder();
        LogUtil.info("sendMsgWarming pluginListVo=" + JsonUtils.beanToJson(pluginListVo));

        /*
        {"name": "msgWarning","status": 1,"id": 6,"enable": "0","warnType": "cpu,mem,du","useRatio": "80","useTime": "20","sendType": "","target": "","dingUrl": "","wechatUrl": "","createTime": 1686187149,"modifyTime": 1686187149}
         */
        if (ObjectUtils.isEmpty(pluginListVo) || ObjectUtils.isEmpty(pluginListVo.getEnable()) || !"1".equals(pluginListVo.getEnable())
                || ObjectUtils.isEmpty(pluginListVo.getWarnType())|| ObjectUtils.isEmpty(pluginListVo.getUseRatio()) || ObjectUtils.isEmpty(pluginListVo.getUseTime())
                ){
            return "";
        }
        if (ObjectUtils.isEmpty(pluginListVo.getDingUrl()) && ObjectUtils.isEmpty(pluginListVo.getWechatUrl()) && ObjectUtils.isEmpty(pluginListVo.getEmail())){
            return "";
        }

        Long time = System.currentTimeMillis();
        String timeStr = DateDUtil.getYearMonthDayHMS(new Date(), "yyyy-MM-dd HH:mm");

        Double useTime = Double.valueOf(pluginListVo.getUseTime()) * 60;
        Double numD = useTime / 20;
        int num = numD.intValue();
        if (useTime % 20 != 0){
            num ++;
        }

        DecimalFormat df = new DecimalFormat("#.00");
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String systemName = systemOptionDao.getSystemConfigByKey("systemName");

        List<String> warnTypeList = Arrays.asList(pluginListVo.getWarnType().split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());

        Double useRatio = Double.valueOf(pluginListVo.getUseRatio());
        // cpu
        if (warnTypeList.contains("cpu")){
            /*
                     procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
             r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
             0 21      0 5512068      4 15969484    0    0     1    20    0    0  8  3 41 48  0
               */
            List<String> cpuList = ShellExecTool.callShellByExec("vmstat 1 1");
            String freeCpu = ShellExecTool.jxCpuValue(cpuList);
            Double useCup0 = 100 - Double.valueOf(freeCpu);
            String warningKey = GlobalConfig.mesWarning_cpu_key;
            Double useCup = Double.valueOf(df.format(useCup0));
            if (useCup >= useRatio){
                // 达到预警条件
                String warningValue = valueOperations.get(warningKey);
                if (!ObjectUtils.isEmpty(warningValue)){
                    warningValue = warningValue + "," + time + "-" + useRatio + "-" + useCup;
                    valueOperations.set(warningKey, warningValue);
                    LogUtil.info("sendMsgWarming cpu预警 warningValue" + warningValue);
                    List<String> warningList = Arrays.asList(warningValue.split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
                    if (warningList.size() >= num){
                        // 发送预警信息
                        sendWarningMsg(pluginListVo, systemName, useCup + "%", timeStr, "CPU");
                        stringRedisTemplate.delete(warningKey);
                    }
                }else {
                    valueOperations.set(warningKey, time + "-" + useRatio + "-" + useCup);
                }

            }else {
                stringRedisTemplate.delete(warningKey);
            }
        }

        // mem 内存
        if (warnTypeList.contains("mem")){
        /*
                          total        used        free      shared  buff/cache   available
            Mem:             47          26           4           0          15          18
            Swap:             0           0           0
         */
            List<String> memoryList = ShellExecTool.callShellByExec("free -g");
            Double freeMemoryRate = ShellExecTool.jxMemoryValue(memoryList, null);
            Double useMemoryRate0 = (1-freeMemoryRate) * 100;
            String warningKey = GlobalConfig.mesWarning_mem_key;
            Double useMemoryRate = Double.valueOf(df.format(useMemoryRate0));
            if (useMemoryRate >= useRatio){
                // 达到预警条件
                String warningValue = valueOperations.get(warningKey);
                if (!ObjectUtils.isEmpty(warningValue)){
                    warningValue = warningValue + "," + time + "-" + useRatio + "-" + useMemoryRate;
                    valueOperations.set(warningKey, warningValue);
                    List<String> warningList = Arrays.asList(warningValue.split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
                    LogUtil.info("sendMsgWarming 内存预警 warningValue" + warningValue);
                    if (warningList.size() >= num){
                        // 发送预警信息
                        sendWarningMsg(pluginListVo, systemName, useMemoryRate + "%", timeStr, "内存");
                        stringRedisTemplate.delete(warningKey);
                    }
                }else {
                    valueOperations.set(warningKey, time + "-" + useRatio + "-" + useMemoryRate);
                }


            }else {
                stringRedisTemplate.delete(warningKey);
            }
        }

        // du 文件存储
        if (warnTypeList.contains("du")){
            // 空间
            // 用户使用的空间总和
            Long userUsedTotal = userService.sumUserSpaceUsed();
            // 组织机构使用的空间总和
            Long groupUsedTotal = groupService.sumGroupSpaceUsed();
            // 总使用数
            Long totalSpaceUsed = userUsedTotal + groupUsedTotal;
            Double duRate0 = (totalSpaceUsed.doubleValue() / (diskDefaultSize * 1024 * 1024 * 1024) ) * 100;

            Double duRate = Double.valueOf(df.format(duRate0));

            LogUtil.info("文件存储 duRate=" + duRate);
            String warningKey = GlobalConfig.mesWarning_du_key;
            if (duRate >= useRatio){
                // 达到预警条件
                String warningValue = valueOperations.get(warningKey);
                if (!ObjectUtils.isEmpty(warningValue)){
                    warningValue = warningValue + "," + time + "-" + useRatio + "-" + duRate;
                    valueOperations.set(warningKey, warningValue);
                    List<String> warningList = Arrays.asList(warningValue.split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
                    LogUtil.info("sendMsgWarming 文件存储 warningValue" + warningValue);
                    if (warningList.size() >= num){
                        // 发送预警信息
                        sendWarningMsg(pluginListVo, systemName, duRate + "%", timeStr, "文件存储");
                        stringRedisTemplate.delete(warningKey);
                    }
                }else {
                    valueOperations.set(warningKey, time + "-" + useRatio + "-" + duRate);
                }
            }else {
                stringRedisTemplate.delete(warningKey);
            }
        }

        return sb.toString();
    }

    /** 【消息预警】2023/6/19 18:22:32 FilesBox CPU使用占比超过70%，请注意！（您可在管理后台>消息预警，关闭此提醒） */
    private void sendWarningMsg(PluginListVo pluginListVo, String systemName, String rate, String time, String type){
        String msg = String.format(GlobalConfig.mesWarning_send_temp_key, time, systemName, type, rate);
        List<String> sendTypeList = Arrays.asList(pluginListVo.getSendType().split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
        if (sendTypeList.contains("ding")) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "消息预警");
            map.put("text", msg);

            PostContent postContent = new PostContent(pluginListVo.getDingUrl(), JsonUtils.beanToJson(map), "markdown");
            try {
                DingTalkDUtil.sendMsg(postContent);
            }catch (Exception e){
                LogUtil.error(e, "DingTalkDUtil sendMsg");
            }

        }
        if (sendTypeList.contains("wechat")) {
            try {
                WeComPushUtil.sendGroup(pluginListVo.getWechatUrl(), msg);
            }catch (Exception e){
                LogUtil.error(e, "WeComPushUtil sendGroup");
            }

        }
        if (sendTypeList.contains("email") && !ObjectUtils.isEmpty(pluginListVo.getEmail())) {
            List<String> emailList = Arrays.asList(pluginListVo.getEmail().split(",")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
            String emailTitle = "【" + systemName + "】";
            Email email = null;
            for (String emailStr : emailList){
                try {
                    email = new Email();
                    email.setEmail(emailStr);
                    email.setSubject(emailTitle);
                    email.setContent(emailTitle + msg);
                    mailService.send(email);
                } catch (Exception e) {
                    LogUtil.error(e, "sendWarningMsg 发送邮箱error");
                }
            }
        }
    }
}
