package com.svnlan.home.utils;

import com.google.common.base.Splitter;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CloudFile;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.imageio.stream.FileImageOutputStream;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    //写入文件
    public static boolean putFileContent(String path, String content) {
        try (OutputStream out = new FileOutputStream(path)) {
            IOUtils.write(content, out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage() + ", 写入文件失败, path: " + path, ", content: " + content);
            return false;
        }
        return true;
    }

    //读取文件
    public static String getFileContent(String path) {
        try (InputStream in = new FileInputStream(path)) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("读取文件失败, path: " + path);
        }
        return "";
    }

    //读取文件
    public static String getFileContent(File File) {
        try (InputStream in = new FileInputStream(File)) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("读取文件失败, getAbsolutePath: " + File.getAbsolutePath());
        }
        return "";
    }

    /**
     * @Description: 日期路径
     * @params: []
     * @Return: java.lang.String
     * @Modified:
     */
    public static String getDatePath() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "_" + month + "/" + day + "/";
    }

    public static final List<String> specificExtList = Arrays.asList("tar.gz");

    /**
     * @Description: 获取文件扩展名
     * @params: [fileName]
     * @Return: java.lang.String
     * @Modified:
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        //点号位置
        int indexOfDot = fileName.lastIndexOf(".");
        if (indexOfDot == -1) {
            return "";
        }
        for (String specific : specificExtList) {
            if (fileName.endsWith(specific)) {
                return specific;
            }
        }
        String fileExt = fileName.substring(indexOfDot);
        return StringUtil.ltrim(fileExt, 1).toLowerCase();
    }

    /**
     * 磁盘上实际的文件名称
     *
     * @param userId 用户id
     * @param ext    文件后缀
     */
    public static String getRealFileName(Long userId, String ext) {
        return RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + userId + "." + ext;
    }

    /**
     * @Description:
     * @params: [fileName]
     * @Return: java.lang.String
     * @Modified:
     */
    public static String insertBeforeExtension(String fileName, String insert) {
        if (fileName == null) {
            return null;
        }
        //点号位置
        Integer indexOfDot = fileName.lastIndexOf(".");
        if (indexOfDot == -1) {
            return fileName + insert;
        }
        return fileName.substring(0, indexOfDot) + insert + fileName.substring(indexOfDot, fileName.length());
    }

    public static void responseFile(HttpServletResponse response, String finalFilePath, String fileName, Boolean isSwf) throws Exception {
        responseFile(response, finalFilePath, fileName, isSwf, true);
    }

    public static void responseFile(HttpServletResponse response, String finalFilePath, String fileName, Boolean isSwf, Boolean isDown) throws Exception {
        if (isSwf) {
            response.setHeader("Content-Type", "application/x-shockwave-flash");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        } else {
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename* = UTF-8''" + fileName);
        }
        // if (isDown) {
        response.setHeader("X-Accel-Redirect", finalFilePath);
        // }
        //response.getWriter().close();
    }

    public static void responseMP4(HttpServletResponse response, String finalFilePath, String fileName) {

        response.setHeader("Content-Type", "video/mp4");
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);

        response.setHeader("X-Accel-Redirect", finalFilePath);
    }

    /**
     * @Description: 生成下载key
     * @params: []
     * @Return: java.lang.String
     * @Modified:
     */
    public static String getDownloadKey() {
        HttpServletRequest request = HttpUtil.getRequest();
        String token = request.getHeader("token");
        String key = AESUtil.encrypt(token + GlobalConfig.ATTACHMENT_KEY_SEPARATOR + System.currentTimeMillis(),
                GlobalConfig.ATTACHMENT_AES_KEY, true);
        try {
            key = URLEncoder.encode(key, "UTF-8");
        } catch (Exception e) {
            LogUtil.error(e, "下载key, encode失败");
        }
        return key;
    }
    public static String getVideoImgDownloadKey(String value) {
        return getVideoImgDownloadKey(value, "0");
    }
    public static String getVideoImgDownloadKey(String value, String sourceKey) {
        String key = AESUtil.encrypt(value + GlobalConfig.ATTACHMENT_KEY_SEPARATOR + sourceKey,
                GlobalConfig.ATTACHMENT_AES_KEY, true);
        try {
            key = URLEncoder.encode(key, "UTF-8");
        } catch (Exception e) {
            LogUtil.error(e, "下载key, encode失败");
        }
        return key;
    }

    public static String getDownloadKey(String token) {
        if (ObjectUtils.isEmpty(token)) {
            token = "";
        }
        String key = AESUtil.encrypt(token + GlobalConfig.ATTACHMENT_KEY_SEPARATOR + System.currentTimeMillis(),
                GlobalConfig.ATTACHMENT_AES_KEY, true);
        try {
            key = URLEncoder.encode(key, "UTF-8");
        } catch (Exception e) {
            LogUtil.error(e, "下载key, encode失败");
        }
        return key;
    }


    public static String encodeDownloadUrl(String sourceName) {
        return sourceName.replaceAll("\\[", "%5B")
                .replaceAll("]", "%5D")
                .replaceAll("%", "%25")
                .replaceAll("#", "%23")
                .replaceAll(";", "%3B")
                .replaceAll("\\?", "")
                .replaceAll("/", "")
                .replaceAll("\\\\", "")
                ;
    }


    public static String getM3u8Key() {
        HttpServletRequest request = HttpUtil.getRequest();
        String token = request.getHeader("token");
        String key = AESUtil.encrypt(token + GlobalConfig.M3U8_PLAY_KEY_SEPARATOR + System.currentTimeMillis(),
                GlobalConfig.M3U8_PLAY_AES_KEY, true);
        try {
            key = URLEncoder.encode(key, "UTF-8");
        } catch (Exception e) {
            LogUtil.error(e, "m3u8 Key, encode失败");
        }
        return key;
    }

    /**
     * @Description: 转码进度
     * @params: [m3u8Path, videoLength, needProgress]
     * @Return: java.lang.Integer
     * @Modified:
     */
    public static Integer getConvertedLength(String m3u8Path, Integer videoLength, Boolean needProgress) {
        if (videoLength.equals(0)) {
            return 0;
        }
        //m3u8内容
        String m3u8String = FileUtil.getFileContent(m3u8Path);
        if (StringUtil.isEmpty(m3u8String)) {
            return 0;
        }
        //查找时间xx.xx秒
        Pattern pattern = Pattern.compile("EXTINF:(\\d*?\\.\\d*?),");
        Matcher m = pattern.matcher(m3u8String);
        Double seconds = 0D;
        while (m.find()) {
            seconds += Double.parseDouble(m.group(1));
        }
        return Math.min(100, (int) Math.ceil(seconds * 100 / videoLength));
    }

    /**
     * @Description: 文件非法字符替换
     * @params: [fileName]
     * @Return: java.lang.String
     * @Modified:
     */
    public static String replaceIllegalSymbol(String fileName) {
        return fileName.replaceAll("/", "丿")
                .replaceAll("\\\\", "乀")
                .replaceAll(":", "：")
                .replaceAll("\\?", "？")
                .replaceAll("\\|", "丨")
                .replaceAll("\\*", "※")
                .replaceAll("\"", "”")
                .replaceAll("<", "＜")
                .replaceAll(">", "＞")
                .replaceAll("~", "_")
                ;
    }

    public static String removeIllegalSymbol(String fileName) {
        return fileName.replaceAll("/", "")
                .replaceAll("\\\\", "")
                .replaceAll(":", "")
                .replaceAll("\\?", "")
                .replaceAll("\\|", "")
                .replaceAll("\\*", "")
                .replaceAll("\"", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("~", "_")
                ;
    }

    /**
     * @Description: 部分业务返回文件地址
     * @params: [sourcePath, busType, commonSource]
     * @Return: void
     * @Modified:
     */
    public static String returnPath(String sourcePath, String busType, String thumb) {
        return returnPath(sourcePath, busType, thumb, true);
    }

    public static String returnPath(String sourcePath, String busType, String thumb, boolean needShowPath) {
        String path;
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(busType);
        String showPath = needShowPath ? PropertiesUtil.getUpConfig(busTypeEnum.getBusType() + ".showPath") : "";
        String suffix;
        switch (busTypeEnum) {
            case IMAGE:
            case AVATAR:
            case HOMEPAGE_IMAGE:
            case HOMEPAGE_ATTACHMENT:
            case HOMEPAGE_VIDEO:
            case BROCHURE_ATTACHMENT:
            case DESIGN_THUMB:
            case DESIGN_LOGO:
                path = sourcePath;
                break;
            case CLOUD:
                path = thumb;
                String firstPath = getFirstStorageDevicePath(path);
                suffix = FileUtil.getFileExtension(sourcePath);
                if (StringUtil.isEmpty(path) && Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix)) {
                    path = (showPath + sourcePath)
                            .replace(firstPath + "/doc/", firstPath + "/common/doc/")
                            .replace(firstPath + "/attachment/", firstPath + "/common/attachment/")
                            .replace(firstPath + "/private/", firstPath + "/common/");
                }
                break;
            default:
                path = null;
        }
        return path;
    }

    public static String getFirstStorageDevicePath(String path){
        if (ObjectUtils.isEmpty(path) || path.indexOf("/") < 0){
            return "";
        }
        path = path.substring(1, path.length());
        return "/" + path.substring(0, path.indexOf("/"));

    }

    public static String getUseSourceNewPath(CloudFile cloudFile, LoginUser loginUser, String busType) {
        String newPath = FileUtil.getDatePath() + System.currentTimeMillis()
                + "_" + loginUser.getUserID() + "_" + busType + cloudFile.getFileId()
                + "." + cloudFile.getSuffix();
        return newPath;
    }

    public static void responseJson(HttpServletResponse response, String finalFilePath, String fileName) {
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Content-Disposition", "inline; filename* = UTF-8''" + fileName);
        response.setHeader("X-Accel-Redirect", finalFilePath);
    }

    public static String[] getFilePathAndName(String path) {
        int lastSlash = path.lastIndexOf("/");
        return new String[]{path.substring(0, lastSlash), path.substring(lastSlash + 1, path.length())};
    }

    public static Map<String, String> extractParams(String url) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        return Splitter.on("&").withKeyValueSeparator("=").split(params);
    }

    /**
     * @param prefix
     * @param is
     * @param fileName
     * @return void
     * @description: 将InputStream写入文件
     */
    public static Boolean generateFile(String prefix, InputStream is, String fileName) {
        Boolean success = Boolean.TRUE;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            int len = -1;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            success = Boolean.FALSE;
            LogUtil.error(e, prefix + "将InputStream写入文件发生异常");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static File getFile(String url) {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("net_url", fileName);
            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return org.apache.commons.codec.binary.Base64.encodeBase64String(data);
    }


    //判断是否UTF-8
    public static void checkUTF8(File file) {
        try (BufferedInputStream bin = new BufferedInputStream(
                new FileInputStream(file))) {
            int p = (bin.read() << 16) + (bin.read() << 8) + bin.read();
            boolean isUTF8 = p == 0xefbbbf; //UTF8 BOM
            if (!isUTF8) {//判断UTF8无BOM
                try (Reader reader = new FileReader(file)) {
                    byte[] bytes = IOUtils.toByteArray(reader, "UTF-8");
                    int a = ((bytes[0] & 0xff) << 16) + ((bytes[1] & 0xff) << 8) + (bytes[2] & 0xff);
                    if (a != p) {
                        // 文件必须是UTF-8编码
                        throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError.getCode());
                    }
                }

            }
        } catch (Exception e) {
            LogUtil.error(e, "导入文件文件编码获取失败");
            // , "文件必须是UTF-8编码"
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError.getCode());
        }
    }


    /**
     * @Description: 文件是否存在
     * @params: [fileName]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    public static Boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * @Description: 写文件
     * @params: [file, fileBytes]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    public static Boolean writeFile(File file, byte[] fileBytes) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);) {
            out.write(fileBytes);
            out.flush();
        } catch (IOException e) {
            LogUtil.error(e, " util 写入文件失败, " + file.getAbsolutePath());
            return false;
        }
        return true;
    }
    public static Boolean createNewFile(File file) {
        try  {
            file.createNewFile();
        } catch (IOException e) {
            LogUtil.error(e, " util createNewFile 文件失败, " + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    public static String textData(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return "";
        } else {
            String charset = EncodingDetects.getJavaEncode(filePath);
            if ("ASCII".equals(charset)) {
                charset = StandardCharsets.US_ASCII.name();
            }

            StringBuilder result = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line).append("\r\n");
                }
                br.close();
                return result.toString();
            } catch (IOException e) {
                LogUtil.error(e, "textData io error");
            } catch (Exception e) {
                LogUtil.error(e, "textData error");
            }
            return "";
        }
    }

    //读取文件
    public static String getFileContent(String path, Charset encoding) {
        BufferedReader br = null;
        StringBuilder str = new StringBuilder();
        File f = null;
        try {
            f = new File(path);
            if (!f.exists()) {
                return "";
            }
            // 在FileInputStream中指定编码格式为"GBK"
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
            String t = null;
            while ((t = br.readLine()) != null) {
                str.append(t + "\n");

            }
            return str.toString();
        } catch (FileNotFoundException e) {
            LogUtil.error(e, "读取文件失败 notFound, path: " + path);
        } catch (IOException e) {
            LogUtil.error(e, "读取文件失败 IOError, path: " + path);
        } catch (Exception e) {
            LogUtil.error(e, "读取文件失败 error, path: " + path);
        } finally {
            // 关闭流
            if (br == null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.error(e, "读取文件失败 close , path: " + path);
                } catch (Exception e) {
                    LogUtil.error(e, "读取文件失败 close error, path: " + path);
                }
            }
        }
        return "";
    }

    /**
     * 十六进制字节转字节数组
     *
     * @param s 实例
     *          String str2="ffd8ffe000104a46494600010100000100010000f****ee3fdf4ff00d0857927f0cbf43fccd483";
     *          byte[] t=fromHexString(str2);
     *          byte2image(t, "d://1.jpg");//转成保存图片
     */
    public static byte[] fromHexString(String s) {
        int length = s.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((Character.digit(s.charAt(i * 2), 16) << 4) | Character
                    .digit(s.charAt((i * 2) + 1), 16));
        }
        return bytes;
    }

    /**
     * 实现字节数组向十六进制的转换方法一
     *
     * @param fieldData
     * @return
     */
    public static String toHexString(byte[] fieldData) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldData.length; i++) {
            int v = (fieldData[i] & 0xFF);
            if (v <= 0xF) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    //byte数组转图片
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            LogUtil.info("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            LogUtil.error("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    /****** *图片转十六进制* ***********/
    public static String getImageContent(String path) {
        try {
            //StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream(path);
            //BufferedInputStream bis = new BufferedInputStream(fis);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            // 得到图片的字节数组
            byte[] result = bos.toByteArray();
            // 字节数组转成十六进制

            return toHexString(result);
            //String str = byte2HexStr(result);
            /*
             * 将十六进制串保存到txt文件中
             */
           /* PrintWriter pw = new PrintWriter(
                    new FileWriter("qgc88.txt"));
            pw.println(str);
            pw.close();*/
        } catch (IOException e) {
            LogUtil.error(e, "读取图片失败！");
        }
        return "";
    }

    /****** *读取doc内容* ***********/
    public static String getDocxContent(String path) {
        try {
            FileInputStream fis1 = new FileInputStream(path);
            XWPFWordExtractor docx = new XWPFWordExtractor(new XWPFDocument(fis1));
            return docx.getText();

        } catch (IOException e) {
            LogUtil.error(e, "读取docx失败！");
        }
        return "";
    }


    public static Boolean writeDocFile(File file, String content) {
        try {
            XWPFDocument doc = new XWPFDocument();

            XWPFParagraph paragraphX = doc.createParagraph();
            XWPFRun runX = paragraphX.createRun();
            runX.setText(content);

            OutputStream os = new FileOutputStream(file);
            //把doc输出到输出流
            doc.write(os);
            os.close();
            doc.close();
        } catch (IOException e) {
            LogUtil.error(e, " writeDocFile 写入文件失败, " + file.getAbsolutePath());
            return false;
        }
        return true;
    }


    /**
     * 先创建一个XSSFWorkbook对象
     *
     * @param
     * @return
     */
    public static boolean writeExcelFile(File file) {
        XSSFWorkbook workbook = null;
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            workbook = new XSSFWorkbook();
            XSSFSheet xssfSheet = workbook.createSheet("Sheet");
            XSSFRow xssfRow = xssfSheet.createRow(1); // 行
            XSSFCell xssfCell = xssfRow.createCell(1); // 列
            // 4、设置单元格内容
            xssfCell.setCellValue("");
            workbook.write(outputStream);
        } catch (Exception e) {
            LogUtil.error(e, " writeExcelFile 写入文件失败, " + file.getAbsolutePath());
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static Boolean writePptFile(File file, String content) {
        XMLSlideShow ppt = null;
        OutputStream os = null;
        try {
            ppt = new XMLSlideShow();
            XSLFSlide blankSlide = ppt.createSlide();
            // 构建一个文本框
            XSLFTextBox shape = blankSlide.createTextBox();
            // 设置文本
            shape.setText(content);

            os = new FileOutputStream(file);
            //输出到输出流
            ppt.write(os);
            os.close();
            ppt.close();
        } catch (IOException e) {
            LogUtil.error(e, " writePptFile 写入文件失败, " + file.getAbsolutePath());
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ppt != null) {
                try {
                    ppt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static Boolean writeSvgFile(File file, byte[] fileBytes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //输出到输出流
            fos.write(fileBytes);
            fos.close();
        } catch (IOException e) {
            LogUtil.error(e, " writeSvgFile 写入文件失败, " + file.getAbsolutePath());
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static Boolean writeHTMLFile(File file, String htmlStr) {
        if (ObjectUtils.isEmpty(htmlStr)) {
            htmlStr = "<!DOCTYPE html>"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "    <head>"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
                    + "        <title></title>"
                    + "    </head>"
                    + "    <body>"
                    + "    </body>"
                    + "</html>"
            ;
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bufferedWriter.write(htmlStr);
            bufferedWriter.newLine();//换行
            /* * 刷新该流的缓冲。
             * 关键的一行代码。如果没有加这行代码。数据只是保存在缓冲区中。没有写进文件。
             * 加了这行才能将数据写入目的地。 * */
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            LogUtil.error(e, " writeHTMLFile 写入文件失败, " + file.getAbsolutePath());
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 从文件加载模板
     *
     * @return Document
     */
    public static String parseDocumentFromFile(String filePath) {
        String html = "";
        File input = new File(filePath);
        if (input == null) {
            return "文档不存在";
        }
        Document doc = null;
        try {
            //从文件加载Document文档
            doc = Jsoup.parse(input, "UTF-8");
            html = doc.outerHtml();
        } catch (IOException e) {
            LogUtil.error(e, "parseDocumentFromFile error");
        }
        return html;
    }

    public static String getJsFileContent(String filename) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String ret = "";
        FileReader reader = null;
        try {

            reader = new FileReader(filename); // 执行指定脚本
            engine.eval(reader);
            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                ret = (String) invoke.invokeFunction("encrypt", "ABCD");  //方法名和参数
                //System.out.println("s = " + s);
            }
        } catch (ScriptException e) {
            LogUtil.error(e, "getJsFileContent ScriptException error");
        } catch (NoSuchMethodException e) {
            LogUtil.error(e, "getJsFileContent NoSuchMethodException error");
        } catch (IOException e) {
            LogUtil.error(e, "getJsFileContent IOException error");
        } catch (Exception e) {
            LogUtil.error(e, "getJsFileContent Exception error");
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (Exception e) {
                    LogUtil.error(e, "getJsFileContent reader close error");
                }
            }
        }
        return ret;
    }

    public static boolean isMessyCode(String strName) {
        //去除字符串中的空格 制表符 换行 回车
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("").replaceAll("\\+", "").replaceAll("#", "").replaceAll("&", "");
        //去除字符串中的标点符号
        String temp = after.replaceAll("\\p{P}", "");
        //处理之后转换成字符数组
        char[] ch = temp.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            //判断是否是数字或者英文字符
            if (!judge(c)) {
                //判断是否是中日韩文
                if (!isChinese(c)) {
                    //如果不是数字或者英文字符也不是中日韩文则表示是乱码返回true
                    return true;
                }
            }
        }
        //表示不是乱码 返回false
        return false;
    }

    /**
     * 判断是否是中日韩文字
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean judge(char c) {
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) {
            return true;
        }
        return false;
    }

    public static String getUtf8String(String str) {
        if (str != null && str.length() > 0) {
            String needEncodeCode = "ISO-8859-1";
            String neeEncodeCode = "ISO-8859-2";
            String gbkEncodeCode = "GBK";
            try {
                if (Charset.forName(needEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(str.getBytes(needEncodeCode), StandardCharsets.UTF_8);
                }
                if (Charset.forName(neeEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(str.getBytes(neeEncodeCode), StandardCharsets.UTF_8);
                }
                if (Charset.forName(gbkEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(getUTF8BytesFromGBKString(str), StandardCharsets.UTF_8);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    /****** *图片转Blob* ***********/
    public static Object getImageContentBlob(String path) {
        try {
            // 得到图片的字节数组
            byte[] result = getFileByte(path);
            // 字节数组转成十六进制

            return toBlobString(result);
        } catch (Exception e) {
            LogUtil.error(e, "读取图片失败！");
        }
        return "";
    }

    /****** *图片转Blob* ***********/
    public static byte[] getFileByte(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            // 得到图片的字节数组
            byte[] result = bos.toByteArray();
            // 字节数组转成十六进制

            return result;
        } catch (IOException e) {
            LogUtil.error(e, "读取图片失败！");
        }
        return null;
    }

    /**
     * 实现字节数组向十六进制的转换方法一
     *
     * @param fieldData
     * @return
     */
    public static Blob toBlobString(byte[] fieldData) {
        Blob blob = new Blob() {
            @Override
            public long length() throws SQLException {
                return 0;
            }

            @Override
            public byte[] getBytes(long pos, int length) throws SQLException {
                return new byte[0];
            }

            @Override
            public InputStream getBinaryStream() throws SQLException {
                return null;
            }

            @Override
            public long position(byte[] pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public long position(Blob pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
                return 0;
            }

            @Override
            public OutputStream setBinaryStream(long pos) throws SQLException {
                return null;
            }

            @Override
            public void truncate(long len) throws SQLException {

            }

            @Override
            public void free() throws SQLException {

            }

            @Override
            public InputStream getBinaryStream(long pos, long length) throws SQLException {
                return null;
            }
        };
        try {
            blob.setBytes(0, fieldData);
            return blob;
        } catch (Exception e) {
            LogUtil.error(e, "二进制转blob错误");
        }
        return null;
    }

    /**
     * @param prefix
     * @param buffer
     * @param filePath
     * @return void
     * @description: 字节数组转存文件
     */
    public static void bytesToFile(String prefix, byte[] buffer, final String filePath) {
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
        } catch (FileNotFoundException e) {
            LogUtil.error(e, prefix + "文件不存在：" + filePath);
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        } catch (IOException e) {
            LogUtil.error(e, prefix + "文件IO异常：" + filePath);
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        } finally {
            if (null != bufferedOutput) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    LogUtil.error(e, prefix + "出现IO异常：" + filePath);
                }
            }
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    LogUtil.error(e, prefix + "出现IO异常：" + filePath);
                }
            }
        }
    }
    public static List<String> getM3u8LineList(String path) {
        BufferedReader br = null;
        List<String> pathList = new ArrayList<>();
        File f = null;
        try {
            f = new File(path);
            if (!f.exists()) {
                return pathList;
            }
            // 在FileInputStream中指定编码格式为"GBK"
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
            String t = null;
            while ((t = br.readLine()) != null) {
                if (t.endsWith(".ts")){
                    pathList.add(t);
                }
            }
            return pathList;
        } catch (FileNotFoundException e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 notFound, path: " + path);
        } catch (IOException e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 IOError, path: " + path);
        } catch (Exception e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 error, path: " + path);
        } finally {
            // 关闭流
            if (br == null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.error(e, "getM3u8LineList 读取文件失败 close , path: " + path);
                } catch (Exception e) {
                    LogUtil.error(e, "getM3u8LineList 读取文件失败 close error, path: " + path);
                }
            }
        }
        return pathList;
    }

    /** 文本未替换成功时的补救
     *
        /tencent_data/private/cloud/2023_6/27/38d3272c136843a897004b84ce956ba51687833456540_1.m3u8
        m3u8_convert_server_url_placeholder/tencent_data/mu/m3u8/2023_6/27/38d3272c136843a897004b84ce956ba51687833456540_1/38d3272c136843a897004b84ce956ba51687833456540_1
     */
    public static String getM3u8LineContent(String path, String key) {
        BufferedReader br = null;

        File f = null;
        try {
            f = new File(path);
            if (!f.exists()) {
                return "";
            }
            StringBuilder result = new StringBuilder();
            // 在FileInputStream中指定编码格式为"GBK"
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
            String t = null;
            int i = 0;
            while ((t = br.readLine()) != null) {
                if (t.endsWith(".ts")){
                    if (t.indexOf(GlobalConfig.m3u8ConvertUrlPlaceholder) >= 0) {
                        result.append(GlobalConfig.m3u8ConvertUrlPlaceholder + "/api/disk/video/img/"+ ( key + "_"+ i)+".ts?showPreview=1&key="
                                + FileUtil.getVideoImgDownloadKey(t.replace(GlobalConfig.m3u8ConvertUrlPlaceholder,""), key)).append("\r\n");
                    }else if (t.indexOf("/mu/") < 0){
                        String tsMuName = path.substring(path.lastIndexOf("/")+1).replace(".m3u8", "");
                        String replaceUrl = path.replace("/private/cloud/", "/mu/m3u8/").replace(".m3u8", "")+ "/" + tsMuName;
                        result.append(GlobalConfig.m3u8ConvertUrlPlaceholder + "/api/disk/video/img/"+( key + "_"+ i)+".ts?showPreview=1&key="
                                + FileUtil.getVideoImgDownloadKey(t.replace(tsMuName, replaceUrl), key)).append("\r\n");
                    }else if (t.indexOf(GlobalConfig.oldInnerServer) >= 0){
                        result.append(GlobalConfig.oldInnerServer + "/api/disk/video/img/"+( key + "_"+ i)+".ts?showPreview=1&key="
                                + FileUtil.getVideoImgDownloadKey(t.replace(GlobalConfig.oldInnerServer,""), key)).append("\r\n");
                    }
                    i ++;
                }else {
                    result.append(t).append("\r\n");
                }
            }
            br.close();
            return result.toString();
        } catch (FileNotFoundException e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 notFound, path: " + path);
        } catch (IOException e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 IOError, path: " + path);
        } catch (Exception e) {
            LogUtil.error(e, "getM3u8LineList 读取文件失败 error, path: " + path);
        } finally {
            // 关闭流
            if (br == null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.error(e, "getM3u8LineList 读取文件失败 close , path: " + path);
                } catch (Exception e) {
                    LogUtil.error(e, "getM3u8LineList 读取文件失败 close error, path: " + path);
                }
            }
        }
        return "";
    }

    public static String getShowAvatarUrl(String path, String name){
        if (path.indexOf("http") == 0){
            return path;
        }
        return getShowImageUrl(path, "avatar_" + name + ".jpg");
    }
    public static String getShowImageUrl(String path, String name){
        String viewKey = getVideoImgDownloadKey(path);
        return GlobalConfig.show_img_api_key + name + "?showPreview=1&key=" + viewKey;
    }
    public static String getShowImageUrlDown(String path, String name){
        String viewKey = getVideoImgDownloadKey(path, "");
        return GlobalConfig.show_img_api_key + name + "?showPreview=1&isDown=1&key=" + viewKey;
    }

    public static String replaceIllegalSymbolAll(String fileName) {
        //替换非法字符
        fileName = replaceIllegalSymbol(fileName);
        //url encode
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replaceAll("\\+", "%20");
        } catch (Exception e) {
            LogUtil.error(e, "下载文件，编码失败");
        }
        return fileName;
    }
}
