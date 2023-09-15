package com.svnlan.home.utils.office;

import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import com.svnlan.utils.StringUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: libreoffice操作工具类
 */
public class LibreOfficeDUtil {

    /**
     * @description: libreOffice转换命令执行
     * @param prefix
     * @param libreOfficeVersion
     * @param targetType
     * @param sourceFilePath
     * @param targetDir
     * @return java.lang.Boolean
     */
    public static Boolean libreOfficeCommand(String prefix, String libreOfficeVersion, String targetType, String sourceFilePath, String targetDir) {
        prefix += String.format("libreOfficeCommand(%s)>>>", RandomUtil.getuuid());
        Boolean success = Boolean.TRUE;

        List<String> convert = new ArrayList<>();
        convert.add(libreOfficeVersion);
        convert.add("--headless");
        convert.add("--convert-to");
        convert.add(targetType);
        convert.add(sourceFilePath);
        convert.add("--outdir");
        convert.add(targetDir);

        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        int runningStatus = 0;
        try {
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            LogUtil.info(prefix + "转换命令: " + sb.toString());
            builder.command(convert);
            Process process = builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error(prefix + "转换返回的错误信息" + errorString);
            }
            try {
                runningStatus = process.waitFor();
            } catch (InterruptedException e) {
                success = false;
                LogUtil.error(e, prefix + String.format("转换process.waitFor()中断异常"));
            }
        } catch (Exception e) {
            LogUtil.error(e, prefix + "转换异常的返回信息");
            success = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(prefix + "关闭流失败");
                }
            }
        }
        if(0 != runningStatus) {
            success = false;
            LogUtil.error(prefix + String.format("转换异常，错误码:%d", runningStatus));
        }
        return success;
    }

    /**
     * @description: 调用shell脚本执行转换为docx文件
     * @param prefix
     * @param shellFileDirectory
     * @param shellFileName
     * @param sourceFilePath
     * @param targetDir
     * @return java.lang.Boolean
     */
    public static Boolean libreOfficeToDocxByShell(String prefix, String shellFileDirectory, String shellFileName,
                                                   String sourceFilePath, String targetDir) {
        prefix += String.format("libreOfficeToDocxByShell(%s)>>>", RandomUtil.getuuid());
        Boolean success = Boolean.TRUE;

        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        int runningStatus = 0;
        try {
            LogUtil.info(prefix + "转换命令: " + shellFileDirectory + " " + "./" + shellFileName + " " + sourceFilePath + " " + targetDir);
            builder.command("./" + shellFileName, sourceFilePath, targetDir);
            builder.directory(new File(shellFileDirectory));
            Process process = builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error(prefix + "转换返回的错误信息" + errorString);
            }
            try {
                runningStatus = process.waitFor();
            } catch (InterruptedException e) {
                success = false;
                LogUtil.error(e, prefix + String.format("转换process.waitFor()中断异常"));
            }
        } catch (Exception e) {
            LogUtil.error(e, prefix + "转换异常的返回信息");
            success = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(prefix + "关闭流失败");
                }
            }
        }
        if(0 != runningStatus) {
            success = false;
            LogUtil.error(prefix + String.format("转换异常，错误码:%d", runningStatus));
        }
        return success;
    }

}