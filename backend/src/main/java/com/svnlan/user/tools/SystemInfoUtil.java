package com.svnlan.user.tools;

import com.alibaba.fastjson.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SystemInfoUtil {

    public static JSONObject defaultSystemInfo(){

        JSONObject o = new JSONObject();
        // cpu使用率
        o.put("cpuRate","0.0");
        // 内存使用率
        o.put("memoryRate","0.0");
        // 服务器系统存储空间使用率
        //o.put("storageRate","0.0");
        // 网盘存储空间使用率
        o.put("duRate","0.0");
        return o;
    }

    public static JSONObject defaultSystemBasicInfo(){

        JSONObject o = new JSONObject();

        o.put("ip","");
        o.put("language","");
        o.put("ua","");
        // 操作系统
        o.put("computerName","");
        o.put("serverInfo","");
        // 持续启动时间
        o.put("uptime","");
        // 服务器ip
        o.put("serverIp","");
        // 服务器时间
        o.put("serverTime","");
        // Java 版本
        o.put("javaVersion","");
        o.put("javaInfo","{}");
        return o;
    }
    public static void getSystemBasicInfo(JSONObject object) throws UnknownHostException {

        Map map = System.getenv();
        Object computerName = map.get("COMPUTERNAME");
        String javaVersion  = System.getProperty("java.version");
        // 操作系统
        object.put("computerName", ObjectUtils.isEmpty(computerName) ? "-" : computerName);
        object.put("serverInfo", System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
        object.put("javaVersion",javaVersion);
        object.put("serverTime", DateUtil.DateToString(new Date(), DateUtil.yyyy_MM_dd_HH_mm_ss));
        //long start = ManagementFactory.getRuntimeMXBean().getStartTime();
        //long t = System.currentTimeMillis();
        // 持续时间
        object.put("uptime", DateUtil.msToStringTime(ManagementFactory.getRuntimeMXBean().getUptime()));
            /* String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
            System.err.println("程序启动时间:" + format);
            */

        Set<String> ipList = new HashSet<>();
        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getHostName();
        InetAddress[] addressList = InetAddress.getAllByName(localHost.getHostName());
        for (InetAddress address : addressList) {
            ipList.add(address.getHostAddress());
        }
        object.put("hostName", hostName);
        object.put("serverIp", CollectionUtils.isEmpty(ipList) ? "-" : String.join(",", ipList));

        JSONObject o = new JSONObject();
        o.put("javaVersion", javaVersion);
        o.put("maxExecutionTime", "300");
        o.put("maxInputTime", "300");
        o.put("memoryLimit", "500M");
        o.put("postMaxSize", "512M");
        o.put("uploadMaxFilesize", "512M");
        object.put("javaInfo", JsonUtils.beanToJson(o));
    }

    public static JSONObject getMemoryInfo(JSONObject object)  {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize();
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        long usedMemory = totalMemorySize - freePhysicalMemorySize;

        /*
            String totalMemorySize = new DecimalFormat("#.##")
                    .format(osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024 / 1024) + "G";
            // 剩余的物理内存
            String freePhysicalMemorySize = new DecimalFormat("#.##")
                    .format(osmxb.getFreePhysicalMemorySize() / 1024.0 / 1024 / 1024) + "G";
            // 已使用的物理内存
            String usedMemory = new DecimalFormat("#.##").format(
                    (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024.0 / 1024 / 1024)
                    + "G";
        */
        DecimalFormat df = new DecimalFormat("#.00");
        object.put("memoryRate", Double.valueOf(df.format((usedMemory * 1.0 / totalMemorySize))));
        object.put("totalMemory", new DecimalFormat("#.##")
                .format(totalMemorySize / 1024.0 / 1024 / 1024) + "G");

        object.put("freeMemory", new DecimalFormat("#.##")
                .format(freePhysicalMemorySize / 1024.0 / 1024 / 1024) + "G");

        object.put("usedMemory", new DecimalFormat("#.##").format(
                (usedMemory) / 1024.0 / 1024 / 1024)
                + "G");
        return object;
    }


    public static JSONObject getCpuInfo(JSONObject object) throws InterruptedException {
        SystemInfo systemInfo = new SystemInfo();
        return getCpuInfo(systemInfo, object);
    }
    public static JSONObject getCpuInfo(SystemInfo systemInfo, JSONObject object) throws InterruptedException {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        TimeUnit.SECONDS.sleep(1);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        DecimalFormat df = new DecimalFormat("#.##");
        object.put("cpuRate", Double.valueOf(df.format(cSys * 1.0 / totalCpu)));

        /*
            System.err.println("cpu核数:" + processor.getLogicalProcessorCount());
            System.err.println("cpu系统使用率:" + new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
            System.err.println("cpu用户使用率:" + new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
            System.err.println("cpu当前等待率:" + new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
            System.err.println("cpu当前空闲率:" + new DecimalFormat("#.##%").format(idle * 1.0 / totalCpu));
            /// 低版本这两个方法是无参方法,高版本中是需要有参数的
            long[] ticksArray = {1, 2, 3, 4, 5, 6, 7, 8};
            System.err.format("CPU load: %.1f%% (counting ticks)%n",
                    processor.getSystemCpuLoadBetweenTicks(ticksArray) * 100);
            System.err.format("CPU load: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad(1L) * 100);


            System.err.format("CPU load: %.1f%% (counting ticks)%n",
                    processor.getSystemCpuLoadBetweenTicks(ticksArray) * 100);
            System.err.format("CPU load: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad(1L) * 100);
        */
        return object;
    }

    public static String getFileSize(Long fileSize){
        Double aDouble = Double.valueOf(fileSize);
        if(aDouble>=1024){
            aDouble=aDouble/1024;
            if(aDouble>=1024){
                aDouble=aDouble/1024;
                if(aDouble>=1024){
                    aDouble=aDouble/1024;
                    return getDoubleString(aDouble)+" GB";
                }else{
                    return getDoubleString(aDouble)+" MB";
                }
            }else{
                return getDoubleString(aDouble)+" KB";
            }
        }else{
            return getDoubleString(aDouble)+" Byte";
        }
    }
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }

}
