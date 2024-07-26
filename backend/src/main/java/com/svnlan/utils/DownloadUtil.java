package com.svnlan.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class DownloadUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath, long maxLength) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        long contentLengthLong = conn.getContentLengthLong();
        if (contentLengthLong > maxLength){
            throw new SvnlanRuntimeException(CodeMessageEnum.fileTooLarge.getCode());
        }
        //得到输入流
        try (InputStream inputStream = conn.getInputStream()) {
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir + File.separator + fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(getData);

            }
        }
        LogUtil.info("download : " + url + " download success");

    }



    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String getWxImage(String absSrc, String savePath, String cdnDomain, long maxContentLength) {
        //如果是cdn的去掉//
        if (absSrc.startsWith("//")) {
            absSrc = "http://" + absSrc.substring(2);
        }
        String regionUrl = absSrc;
        int k = absSrc.indexOf("?");
        if (k > 0) {
            absSrc = absSrc.substring(0, k);
        }
        int i = absSrc.lastIndexOf("/");
        String suffix = "jpg";
        if (i > 0) {
            String imgName = absSrc.substring(i + 1);
            int j = imgName.indexOf(".");
            if (j > 0) {
                suffix = imgName.substring(j + 1);
            }
        }
        if("image".equals(suffix.toLowerCase())) {
            suffix = "jpg";
        }
        String imgName = RandomUtil.getuuid() + "." + suffix;
        try {
            Boolean success = HtmlDUtil.downloadFile("", regionUrl, savePath, imgName);
            if(!success) {
                return absSrc;
            }
        } catch (Exception e) {
            try {
                DownloadUtil.downLoadFromUrl(absSrc, imgName, savePath, maxContentLength);
            } catch (Exception e1) {
                LogUtil.error(e1, "下载失败, " + absSrc);
                return absSrc;
            }
        }
        return "//" + cdnDomain + savePath + imgName;
    }

}
