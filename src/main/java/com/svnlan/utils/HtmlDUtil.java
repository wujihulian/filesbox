package com.svnlan.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: html处理
 * @author
 * @date
 */
public class HtmlDUtil {

    /**
     * 替换指定标签的属性和值
     * @param str 需要处理的字符串
     * @param tag 标签名称
     * @param tagAttrib 要替换的标签属性值
     * @param startTag 新标签开始标记
     * @param endTag  新标签结束标记
     * @return
     */
    public static String replaceHtmlTag(String str, String tag, String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + tag + "\\s+([^>]*)\\s*" ;
        String regxpForTagAttrib = tagAttrib + "=\\s*\"([^\"]+)\"" ;
        Pattern patternForTag = Pattern.compile (regxpForTag,Pattern. CASE_INSENSITIVE );
        Pattern patternForAttrib = Pattern.compile (regxpForTagAttrib,Pattern. CASE_INSENSITIVE );
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer( "<"+tag+" ");
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()) {
                String attributeStr = matcherForAttrib.group(1);
                matcherForAttrib.appendReplacement(sbreplace, startTag + attributeStr + endTag);
            }
            matcherForAttrib.appendTail(sbreplace);
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    /**
     * @description: 分析网页代码，找到匹配的网页图片地址
     * @param content
     * @return java.util.LinkedHashMap<java.lang.String,java.lang.String>
     */
    public static LinkedHashMap<String, String> getPictureLinkByContent(
            String content) {

        // String searchImgReg =
        // "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";//
        // 用于在网页代码Content中查找匹配的图片链接。
        String searchImgReg2 = "(?x)(src|SRC|background|BACKGROUND)=('|\")((http:|https:)?//([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(bmp|BMP|jpg|JPG|jpeg|JPEG|png|PNG|gif|GIF)))('|\")";

        LinkedHashMap lhm = new LinkedHashMap<String, String>();
        Pattern pattern = Pattern.compile(searchImgReg2);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            lhm.put(matcher.group(3), "0");
        }
        return lhm;
    }

    /**
     * @description: 下载url文件到本地
     * @param prefix
     * @param url
     * @param targetDirectory
     * @param fileName
     * @return Boolean
     */
    public static Boolean downloadFile(String prefix, String url, String targetDirectory, String fileName) {
        Boolean success = Boolean.TRUE;
        CloseableHttpClient httpClient = null;
        InputStream inputStream = null;
        File file = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet method = new HttpGet(url);
            HttpResponse result = httpClient.execute(method);
            result.getEntity().getContentType();
            inputStream = result.getEntity().getContent();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inputStream);

            File targetDirectoryFile = new File(targetDirectory);
            if(!targetDirectoryFile.exists()) {
                targetDirectoryFile.mkdirs();
            }
            String targetFilePath = targetDirectory + fileName;
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(targetFilePath);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
        } catch (IOException e) {
            success = Boolean.FALSE;
            LogUtil.error(e, prefix + "下载url文件失败");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * @description: 读取输入流写入内存
     * @param inStream
     * @return byte[]
     */
    public static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static void main(String[] args) {
        StringBuffer content = new StringBuffer();
        content.append("<ul class=\"imgBox\"><li><img id=\"160424\" src=\"uploads/allimg/160424/1-160424120T1-50.jpg\" class=\"src_class\"></li>");
        content.append("<li><img id=\"150628\" src=\"uploads/allimg/150628/1-15062Q12247.jpg\" class=\"src_class\"></li></ul>");
        System.out.println("原始字符串为:"+content.toString());
        String newStr = replaceHtmlTag(content.toString(), "img", "src", "src=\"http://junlenet.com/", "\"");
        System.out.println("       替换后为:"+newStr);
    }

}
