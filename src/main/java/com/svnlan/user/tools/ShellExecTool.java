package com.svnlan.user.tools;

import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 */
public class ShellExecTool {

    /**
     * 使用 exec 调用shell脚本
     * @param shellString
     */
    public static List<String> callShellByExec(String shellString) {
        List<String> rspList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec(shellString);
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                LogUtil.error("call shell failed. error code is :" + exitValue);
            }
            // 返回值
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                LogUtil.info("返回结果:" + line);
                rspList.add(line);
            }
        } catch (Throwable e) {
            LogUtil.error(e,"call shell failed. " );
        }
        return rspList;
    }


    public static List<String> executeNewFlow(List<String> commands) {
        List<String> rspList = new ArrayList<>();
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec("/bin/bash", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                LogUtil.info("执行命令:" + line);
                out.println(line);
            }
            // 这个命令必须执行，否则in流不结束。
            out.println("exit");
            String rspLine = "";
            LogUtil.info("返回结果:BufferedReader" + in.readLine());
            while ((rspLine = in.readLine()) != null) {
                System.out.println(rspLine);
                LogUtil.info("返回结果:" + rspLine);
                rspList.add(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();
        } catch (IOException e) {
            LogUtil.error(e);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return rspList;
    }

    public static void main(String[] args) {


        List<String> cpuList = new ArrayList<>();
        cpuList.add("procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----");
        cpuList.add(" r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st");
        cpuList.add(" 4  0 3299704 1430940   1856 33414020    0    0   118     7    0    0 10  5 85  0  0");
        List<String> memoryList = new ArrayList<>();
        memoryList.add("              total        used        free      shared  buff/cache   available");
        memoryList.add("Mem:             62          29           1           0          31          31");
        memoryList.add("Swap:            29           3          26");

        String freeCpu = jxCpuValue(cpuList);
        Map<String, Object> reMap = new HashMap<>(3);
        Double freeMemoryRate = jxMemoryValue(memoryList, reMap);

        LogUtil.info(freeCpu);
        LogUtil.info(freeMemoryRate+"");


/*
       // List<String> l = callShellByExec("vmstat 1 1");
        List<String> l = callShellByExec("free -h");


        //执行该命令
        List<String> commands= new ArrayList<>();
        commands.add("vmstat 1 1");
       // commands.add("cd /home/mysql_bak");
       // commands.add("cat bak.log");
        List<String> list = executeNewFlow(commands);
        LogUtil.info("得到备份执行结果数据:" + list);*/
    }


    public static Double jxMemoryValue(List<String> list, Map<String, Object> reMap){
        Double rate = 0d;
        if (!CollectionUtils.isEmpty(list) && list.size() > 2){
            List<String> tList = Arrays.asList(list.get(1).split(" ")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(tList)){
                List<Double> valueList = new ArrayList<>();
                for (String value : tList){
                    if (StringUtil.isNumeric(value)){
                        valueList.add(Double.valueOf(value));
                    }
                }
                if (!CollectionUtils.isEmpty(valueList) && valueList.size() == 6){
                    Double total = valueList.get(0);
                    Double available = valueList.get(valueList.size() - 1);
                    if (!ObjectUtils.isEmpty(reMap)) {
                        reMap.put("memoryTotal", total);
                        reMap.put("memoryFree", available);
                    }
                    String format = new DecimalFormat("#.000").format(available/total);
                    rate = Double.valueOf(format);
                }
            }

        }
        return rate;
    }
    public static String jxCpuValue(List<String> list){
        String cpu = "0";
        if (!CollectionUtils.isEmpty(list) && list.size() > 2){
            List<String> tList = Arrays.asList(list.get(1).split(" ")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
            List<String> vList = Arrays.asList(list.get(2).split(" ")).stream().filter(n->!ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(tList) && !CollectionUtils.isEmpty(vList) && tList.size() == vList.size()){
                int i = 0;
                for (String title : tList){
                    if ("id".equals(title)){
                        cpu = vList.get(i);
                        break;
                    }
                    i ++;
                }
            }
        }
        return cpu;
    }

}
