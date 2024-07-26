package com.svnlan.utils;

import com.svnlan.home.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;

public class DesignTemplatesInitUtils {

    public DesignTemplatesInitUtils() {
        this.init();
    }


    public void init() {
        LogUtil.info("复制装扮模板");
        try {

            String targetDirPath = "/uploads/design/template";
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resource = resolver.getResources("classpath:static/design/*.html");
            if (resource == null || resource.length == 0) {
                LogUtil.info("模板为空====");
                return;
            }

            for (Resource s : resource) {
                try {
                    LogUtil.info("获取文件下装扮模板 path=" + s.getURI() + "，name=" + s.getFilename()
                            + "， targetPath=" + (targetDirPath + "/" + s.getFilename()));
                    String templateMd5 = org.springframework.util.DigestUtils.md5DigestAsHex(s.getInputStream());
                    File targetFile = new File(targetDirPath + "/" + s.getFilename());
                    String targetMd5 = "";
                    boolean targetExist = false;
                    if (targetFile.exists()){
                        targetMd5 = getHtmlMd5(targetFile);
                        targetExist = true;
                    }
                    if (!templateMd5.equals(targetMd5)){
                        if (targetExist){
                            targetFile.delete();
                        }
                        LogUtil.info("写入模板文件 path=" + targetDirPath + "/" + s.getFilename());
                        FileUtil.generateFile("写入模板文件 ", s.getInputStream(), targetDirPath + "/" + s.getFilename());
                    }
                } catch (Exception e) {
                    LogUtil.error(e,"获取文件下装扮模板");
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "copy design template 复制装扮模板到目标文件 error");
        }
    }

    public String getHtmlMd5(File f){
        String md5 = "";
        if (!f.exists()){
            return md5;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            md5 = org.springframework.util.DigestUtils.md5DigestAsHex(fis);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " 获取模板md5失败 失败 f.getPath()=" + f.getPath());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        return md5;
    }

    public static void main(String[] args) {
    }
}
