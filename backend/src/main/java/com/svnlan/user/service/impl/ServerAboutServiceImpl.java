package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.service.MsgWarningScheduleService;
import com.svnlan.user.service.ServerAboutService;
import com.svnlan.user.tools.ShellExecTool;
import com.svnlan.user.tools.SystemInfoUtil;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.IpUtil;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import oshi.SystemInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class ServerAboutServiceImpl implements ServerAboutService {


    @Resource
    MsgWarningScheduleService msgWarningScheduleService;

    @Override
    public JSONObject getServerInfoBasic(HttpServletRequest request){
        JSONObject object = SystemInfoUtil.defaultSystemBasicInfo();
        try {
            String ua = request.getHeader("user-agent").toLowerCase();
            object.put("ua",ua);
            object.put("ip",IpUtil.getIp(request) );
            Locale locale=request.getLocale();
            object.put("language",locale.getLanguage());
            SystemInfoUtil.getSystemBasicInfo(object);
        }catch (Exception e){
            LogUtil.error(e, "获取服务器基本信息失败");
        }

        return object;
    }

    @Override
    public JSONObject getServerInfo(){
        JSONObject object = SystemInfoUtil.defaultSystemInfo();
        try {
            // 系统基本信息
            //SystemInfoUtil.getSystemBasicInfo(object);
            // cup
            SystemInfoUtil.getCpuInfo(object);
            // 内存
            SystemInfoUtil.getMemoryInfo(object);

        }catch (Exception e){
            LogUtil.error(e, "获取服务器信息失败");
        }
        Double doRate = msgWarningScheduleService.getDiskUsedRate(object);
            // 网盘存储空间使用率
        object.put("duRate", doRate);
        return object;
    }


    // 弃用 不一定都是用linux系统
    public JSONObject getServerInfoOld(){
        JSONObject object = new JSONObject();

        Set<String> ipList = new HashSet<>();
        String serverName = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            serverName = localHost.getHostName();
            InetAddress[] addressList = InetAddress.getAllByName(localHost.getHostName());
            for (InetAddress address : addressList) {
                ipList.add(address.getHostAddress());
            }

        } catch (UnknownHostException e) {
            LogUtil.error(e, "获取服务器信息失败");
        }

        // 服务器名
        //List<String> serverNameList = ShellExecTool.callShellByExec("cat /proc/cpuinfo | grep name | cut -f2 -d: ");
        object.put("computerName", ObjectUtils.isEmpty(serverName) ? "-" : serverName);
        // 服务器IP
        //List<String> ipList = ShellExecTool.callShellByExec("hostname -I | sed 's/ //g'");
        object.put("serverIp", CollectionUtils.isEmpty(ipList) ? "-" : String.join(",", ipList));

        // 服务器时间
        object.put("serverTime", DateUtil.DateToString(new Date(), DateUtil.yyyy_MM_dd_HH_mm_ss));

        // 服务器系统
        //List<String> infoList = ShellExecTool.callShellByExec("uname -a");
        object.put("serverInfo", System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getenv("PROCESSOR_ARCHITECTURE"));
        // 服务器持续运行时间
        List<String> uptimeList = ShellExecTool.callShellByExec("cat /proc/uptime");
        object.put("uptime", ShellExecTool.jxUptimeValue(uptimeList));
        // 服务器软件
        object.put("serverSoftware", "");
        // JAVA版本
        List<String> jdkList = ShellExecTool.callShellByExec("java -version");
        object.put("javaVersion", CollectionUtils.isEmpty(jdkList) ? "-" : jdkList.get(0));

        // CPU使用率
        object.put("cpuRate", ShellExecTool.jxCpuRateValue());
        // 内存使用率
        object.put("memoryRate", ShellExecTool.jxMemoryRateValue());
        // 服务器系统存储空间使用率
        object.put("storageRate", ShellExecTool.jxStorageRateValue());
        // 网盘存储空间使用率
        object.put("duRate", msgWarningScheduleService.getDiskUsedRate(object));

        return object;
    }



}
