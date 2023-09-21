package com.svnlan.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.FileUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Description:
 */
@Component
public class VideoUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger("error");
    private static final String os = System.getProperty("os.name");

    public static void mkMuDir(List<String> muPathList) {
        for (String filePath : muPathList){
            String folderStr = filePath.substring(0, filePath.lastIndexOf("/"));
            File file = new File(folderStr);
            if (!file.exists()){
                if (!file.mkdirs()){
                    LOGGER.error("创建目录失败: " + folderStr);
                }
            }
        }
    }

    /**
     * 视频转mp4
     * @param ffmpegPath    转码工具的存放路径
     * @param mp4FilePath   mp4视频文件路径
     * @return 执行是否成功
     */
    public static boolean changeToMp4(String ffmpegPath, String upFilePath, String mp4FilePath) {
        // 创建一个List集合来保存转换视频文件的命令
        int[] heightAndWidth;
        try {
            heightAndWidth = getHeightAndWidth(upFilePath);
        } catch (Exception e){
            LogUtil.error("获取视频宽高失败, path: " + mp4FilePath + ", e: " + e.getMessage());
            heightAndWidth = new int[]{0, 0};
        }
        List<String> convert = new ArrayList<>();
        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的视频文件的路径
        if (heightAndWidth[0] % 2 != 0 || heightAndWidth[1] %2 != 0){
            convert.add("-vf");
            convert.add("\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
        }
//        convert.add("-y");
        convert.add("-v");
        convert.add("error");
        convert.add("-c:v");
        convert.add("libx264");
        convert.add("-strict");
        convert.add("-2");
        convert.add(mp4FilePath);//转换后的文件路径

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            LOGGER.info("cmd: " + sb.toString());
            builder.command(convert);
            Process process =builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            in.close();
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error("转h264异常, " + errorString);
            }
        } catch (Exception e) {
            mark = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error("关闭流失败");
                }
            }
        }
        return mark;
    }


    /**
     * 视频转码、切片、截图
     * @param ffmpegPath    转码工具的存放路径
     * @param mp4FilePath   mp4视频文件路径
//     * @param tsPath    ts文件路径
     * @param tsCutPath 切片文件路径
     * @param m3u8Path  m3u8文件路径
     * @param mediaPicPath    截图保存路径
     * @param keyInfoPath
     * @param muPre
     * @param fileNameWithoutPath
     * @param smallPicPath
     * @return 执行是否成功
     */
    public static boolean executeCodecs(String ffmpegPath,
                                        String mp4FilePath, String tsCutPath, String m3u8Path, String mediaPicPath,
                                        String keyInfoPath, String muPre, String fileNameWithoutPath, String smallPicPath) {

        // mp4转ts
        List<String> convert = new ArrayList<>();
        Map<String, Object> videoInfo = getVideoInfo(mp4FilePath);
        int[] heightAndWidth;
        //获取视频宽高
        try {
            heightAndWidth = getHeightAndWidth(videoInfo);
        } catch (Exception e){
            LogUtil.error(e, "获取视频宽高失败, path: " + mp4FilePath);
            heightAndWidth = new int[]{0, 0};
        }
        //获取视频帧率
        boolean needSetFrameRate = false;
        try {
            double[] frameRate = getFrameRate(videoInfo);
            LogUtil.info("fps, " + JsonUtils.beanToJson(frameRate) + ", " + mp4FilePath);
            //r_frame_rate, avg_frame_rate 都大于200, 或者 r_frame_rate >=1000, avg_frame_rate 0/0的
            if (frameRate[0] >= 200 && frameRate[1] >= 200 || frameRate[0] >= 1000 && frameRate[1] == -1){
                needSetFrameRate = true;
            }
        } catch (Exception e){
            LogUtil.error(e, "获取视频帧率失败, path: " + mp4FilePath);
        }

        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-y");
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(mp4FilePath); // 添加要转换格式的视频文件的路径
        convert.add("-max_muxing_queue_size");
        convert.add("10000");
        if (heightAndWidth[0] % 2 != 0 || heightAndWidth[1] %2 != 0){
            convert.add("-vf");
            convert.add("\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
        }
        convert.add("-vcodec:v");
        convert.add("libx264");
        convert.add("-v");
        convert.add("error");
        if (!os.toLowerCase().startsWith("win")) { //windows
            convert.add("-acodec");
            convert.add("libfdk_aac");
        }
        convert.add("-pix_fmt");
        convert.add("yuv420p");
        convert.add("-vbsf");
        convert.add("h264_mp4toannexb");
        convert.add("-level");
        convert.add("3");
        convert.add("-flags");
        convert.add("-global_header");
        convert.add("-g");
        convert.add("100");
        convert.add("-force_key_frames");
        convert.add("expr:gte(t,n_forced*2)");
        convert.add("-hls_time");
        convert.add("2");
        convert.add("-hls_list_size");
        convert.add("0");
        convert.add("-segment_list_type");
        convert.add("m3u8");
        if (needSetFrameRate){//设置固定帧率24
            convert.add("-r");
            convert.add("24");
        }
        convert.add("-hls_segment_filename");
        convert.add(tsCutPath);
        if (!StringUtil.isEmpty(keyInfoPath)){
            convert.add("-hls_key_info_file");
            convert.add(keyInfoPath);
        }
        convert.add("-f");
        convert.add("hls");
        convert.add(m3u8Path);

        List<String> cutpic = null;
        if (!ObjectUtils.isEmpty(mediaPicPath)) {
            // 创建一个List集合来保存从视频中截取图片的命令
            cutpic = new ArrayList<>();
            cutpic.add(ffmpegPath);
            cutpic.add("-i");
            cutpic.add(mp4FilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
            cutpic.add("-y");
            cutpic.add("-f");
            cutpic.add("image2");
            cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
            cutpic.add("3"); // 添加起始时间为第3秒
            //cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
           // cutpic.add("0.001"); // 添加持续时间为1毫秒

            int newWidth = 0;
            int newHeight = 0;
            int imgW = heightAndWidth[1] ;
            int imgH = heightAndWidth[0];
            int scaleW = 300;
            int scaleH = 300;
            if (imgH > imgW && imgH > scaleH){
                newHeight = scaleH;
                double a = (double)scaleH / imgH * imgW;
                newWidth = Integer.valueOf(String.valueOf(Math.round(a)));
            }else if (imgW > imgH && imgW > scaleW){
                newWidth = scaleW;
                double a = (double)scaleW / imgW * imgH;
                newHeight = Integer.valueOf(String.valueOf(Math.round(a)));
            }else {
                newWidth = heightAndWidth[1];
                newHeight = heightAndWidth[0];
            }
            if (newWidth != 0 && newHeight != 0) {
                cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
                cutpic.add("" + newWidth + "*" + newHeight);
            } else {
                cutpic.add("-vf");
                cutpic.add("scale=-1:200");
            }

            cutpic.add(mediaPicPath); // 添加截取的图片的保存路径
        }

        //替换m3u8文件内容
        List<String> sed = new ArrayList<>();
        sed.add("sed");
        sed.add("-i");
        sed.add("s#" + fileNameWithoutPath + "-#" + muPre+fileNameWithoutPath +"-#g");
        sed.add(m3u8Path);

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        try {
            //转码
            builder.command(convert);
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            LOGGER.info("cmd: " + sb.toString());
//            builder.redirectErrorStream(true);
            Process process =builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            in.close();
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error("转码异常, " + sb.toString() + ",  error: " + errorString );
            }

            //m3u8文件不存在
            if (!new File(m3u8Path).exists()){
                LogUtil.error("FFmpeg转码失败, 进行重试: " + sb.toString());
                builder.command(convert);
                builder.start();
                if (!new File(m3u8Path).exists()) {
                    LogUtil.error("FFmpeg转码重试失败: " + sb.toString());
                    return false;
                }
            }

            if (!CollectionUtils.isEmpty(cutpic)) {
                //截图
                sb = new StringBuilder();
                for (String a : cutpic) {
                    sb.append(a);
                    sb.append(" ");
                }
                LOGGER.info(sb.toString());
                builder = new ProcessBuilder();
                builder.command(cutpic);
                builder.start();
            }

            sb = new StringBuilder();
            for (String a : sed){
                sb.append(a);
                sb.append(" ");
            }
            LOGGER.info(sb.toString());
            //替换m3u8文件内容
            builder = new ProcessBuilder();
            builder.command(sed);
            builder.start();

/*
            Integer videoLength = getVideoLength(videoInfo);
            //截多图
            List<String> sCutPic = new ArrayList<>();
            sCutPic.add(ffmpegPath);
            sCutPic.add("-i");
            sCutPic.add(mp4FilePath);
            sCutPic.add("-y");
            sCutPic.add("-f");
            sCutPic.add("image2");
            sCutPic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
            sCutPic.add("5"); // 添加起始时间为第5秒
            sCutPic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
            sCutPic.add(videoLength.toString()); // 添加持续时间为视频时长
            sCutPic.add("-r"); // 每秒多少帧
            sCutPic.add("0.1"); // 0.1帧, 即10秒一帧
            sCutPic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
            sCutPic.add("320*180");
            sCutPic.add(smallPicPath); // 添加截取的图片的保存路径
            sb = new StringBuilder();
            for (String a : sCutPic){
                sb.append(a);
                sb.append(" ");
            }
            LOGGER.info(sb.toString());
            //替换m3u8文件内容
            builder = new ProcessBuilder();
            builder.command(sCutPic);
            builder.start();*/
        } catch (Exception e) {
            mark = false;
            LogUtil.error(e, "转码失败" + mp4FilePath);
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error("关闭流失败");
                }
            }
        }
        return mark;
    }


    public static int getVideoFrameRate(String orgPath) {
        return getVideoFrameRate(orgPath, getVideoInfo(orgPath), false);
    }
    public static int getVideoFrameRate(String orgPath, Map<String, Object> videoInfo , boolean check) {
        if (check) {
            Map<String, Object> videoInfo1 = getVideoInfo(orgPath);
            videoInfo.putAll(videoInfo1);
        }
        // ffmpeg 默认 24 帧
        int frame = 24;
        //获取视频帧率
        try {
            double[] frameRate = getFrameRate(videoInfo);
            LogUtil.info("getVideoFrameRate fps, " + JsonUtils.beanToJson(frameRate) + ", " + orgPath);
            //r_frame_rate, avg_frame_rate 都大于200, 或者 r_frame_rate >=1000, avg_frame_rate 0/0的
            if (frameRate[0] >= 200 && frameRate[1] >= 200 || frameRate[0] >= 1000 && frameRate[1] == -1){

            }else if (frameRate[0] > 0){
                frame = (int)frameRate[0];
            }
        } catch (Exception e){
            LogUtil.error(e, "getVideoShearList 获取视频帧率失败, path: " + orgPath);
        }

        return frame;
    }

    /** 获得视频帧率 */
    public static double[] getFrameRate(Map<String, Object> videoInfoMap) {
        if (videoInfoMap.get("streams") == null){
            return new double[]{0, 0};
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        for (Object o : streamArray) {
            JSONObject jsonObject = (JSONObject) o;
            if (!jsonObject.get("codec_type").equals("video")){
                continue;
            }
            String rFrameRate = jsonObject.get("r_frame_rate").toString();
            String[] frameArr = rFrameRate.split("/");
            double f1 = Double.valueOf(frameArr[0]);
            double f2 = Double.valueOf(frameArr[1]);
            double fps = f1 / f2;

            String avgFrameRate = jsonObject.get("avg_frame_rate").toString();
            String[] avgFrameArr = avgFrameRate.split("/");
            double af1 = Double.valueOf(avgFrameArr[0]);
            double af2 = Double.valueOf(avgFrameArr[1]);
            double avgFps = af2 == 0 ? -1 : af1 / af2;
            return new double[]{fps, avgFps};
        }
        return new double[]{0, 0};
    }

    /**
       * @Description: 获取视频高和宽
       * @params:  [sourcePath]
       * @Return:  int[]
       * @Modified:
       */
    public static int[] getHeightAndWidth(String sourcePath) {
        return getHeightAndWidth(sourcePath, getVideoInfo(sourcePath), false);
    }
    public static int[] getHeightAndWidth(String sourcePath, Map<String, Object> videoInfoMap, boolean check) {
        if (check){
            Map<String, Object> videoInfoMap1 = getVideoInfo(sourcePath);
            videoInfoMap.putAll(videoInfoMap1);
        }

        if (videoInfoMap.get("streams") == null){
            return new int[]{0, 0};
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        Integer height = 0;
        Integer width = 0;

        for (Object o : streamArray){
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("height") != null) {
                height = Integer.parseInt(jsonObject.get("height").toString());
            }
            if (jsonObject.get("width") != null){
                width = Integer.parseInt(jsonObject.get("width").toString());
            }
            if (height > 0 && width > 0){
                break;
            }
        }
        return new int[]{height, width};
    }
    public static int[] getHeightAndWidthAndFrame(String sourcePath) {
        return getHeightAndWidthAndFrame(sourcePath, getVideoInfo(sourcePath), false);
    }
    public static int[] getHeightAndWidthAndFrame(String sourcePath, Map<String, Object> videoInfoMap, boolean check) {
        if (check){
            Map<String, Object> videoInfoMap1 = getVideoInfo(sourcePath);
            videoInfoMap.putAll(videoInfoMap1);
        }

        if (videoInfoMap.get("streams") == null){
            return new int[]{0, 0};
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        Integer height = 0;
        Integer width = 0;

        for (Object o : streamArray){
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("height") != null) {
                height = Integer.parseInt(jsonObject.get("height").toString());
            }
            if (jsonObject.get("width") != null){
                width = Integer.parseInt(jsonObject.get("width").toString());
            }
            if (height > 0 && width > 0){
                break;
            }
        }

        // ffmpeg 默认 24 帧
        int frame = 24;
        //获取视频帧率
        try {
            double[] frameRate = getFrameRate(videoInfoMap);
            //r_frame_rate, avg_frame_rate 都大于200, 或者 r_frame_rate >=1000, avg_frame_rate 0/0的
            if (frameRate[0] >= 200 && frameRate[1] >= 200 || frameRate[0] >= 1000 && frameRate[1] == -1){

            }else if (frameRate[0] > 0){
                frame = (int)frameRate[0];
            }
        } catch (Exception e){
            LogUtil.error(e, "getHeightAndWidthAndFrame 获取视频帧率失败, path: " + sourcePath);
        }

        return new int[]{height, width, frame};
    }

    private static int[] getHeightAndWidth(Map<String, Object> videoInfoMap) {
        if (videoInfoMap.get("streams") == null){
            return new int[]{0, 0};
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        Integer height = 0;
        Integer width = 0;

        for (Object o : streamArray){
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("height") != null) {
                height = Integer.parseInt(jsonObject.get("height").toString());
            }
            if (jsonObject.get("width") != null){
                width = Integer.parseInt(jsonObject.get("width").toString());
            }
            if (height > 0 && width > 0){
                break;
            }
        }
        return new int[]{height, width};
    }

    /**
       * @Description: 获取视频信息并存到request
       * @params:  [sourcePath]
       * @Return:  java.util.Map<java.lang.String,java.lang.Object>
       * @Modified:
       */
    public static Map<String, Object> getVideoInfo(String sourcePath){
        List<String> command = new ArrayList<>();
        // ffprobe -v quiet -print_format json -show_format -show_streams
        command.add("ffprobe");
        command.add("-v");
        command.add("quiet");
        command.add("-print_format");
        command.add("json");
        command.add("-show_format");
        command.add("-show_streams");
        command.add(sourcePath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.redirectErrorStream(true);
        Map<String, Object> videoInfoMap = new HashMap<>();
        InputStream in = null;
        try {
            Process process = builder.start();
            in = process.getInputStream();
            String videoInfoStr = IOUtils.toString(in, StandardCharsets.UTF_8);

            LogUtil.info("getVideoInfo videoInfoStr=== " + videoInfoStr);
            videoInfoMap = JsonUtils.jsonToMap(videoInfoStr);
            if (videoInfoMap == null){
                LOGGER.error("解析视频json失败，info:"+ videoInfoStr);
                return null;
            }
            IOUtils.closeQuietly(in);
            in.close();
        } catch (Exception e){
            LogUtil.error(e, "获取视频信息失败，sourcePath："+ sourcePath);
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(e, "关闭流错误" + sourcePath);
                }
            }
        }

        return videoInfoMap;
    }

    /** 获取音频视频其他信息  视频帧率、视频采样率、音频声道、视频编码名称 等*/
    public static Map<String, String> getVideoBasicInfoMap(String sourcePath){
        return getVideoBasicInfoMap(sourcePath, getVideoInfo(sourcePath), false);
    }
    public static Map<String, String> getVideoBasicInfoMap(String sourcePath, Map<String, Object> videoInfoMap, boolean check){
        Map<String, String> basicMap = new HashMap<>(4);
        try {
            if (check){
                Map<String, Object> videoInfoMap1 = getVideoInfo(sourcePath);
                videoInfoMap.putAll(videoInfoMap1);
            }
            Map<String, Object> format = (Map<String, Object>) videoInfoMap.get("format");
            String durationString = format.get("duration").toString();
            durationString = durationString.contains(".") ? durationString.substring(0, durationString.indexOf(".")) : durationString;
            basicMap.put("duration", durationString);
            JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
            for (Object o : streamArray) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.get("codec_type").equals("video")){
                    // 帧率
                    String rFrameRate = jsonObject.get("r_frame_rate").toString();
                    String[] frameArr = rFrameRate.split("/");
                    double f1 = Double.valueOf(frameArr[0]);
                    double f2 = Double.valueOf(frameArr[1]);
                    double fps = f1 / f2;

                    String avgFrameRate = jsonObject.get("avg_frame_rate").toString();
                    String[] avgFrameArr = avgFrameRate.split("/");
                    double af1 = Double.valueOf(avgFrameArr[0]);
                    double af2 = Double.valueOf(avgFrameArr[1]);
                    double avgFps = af2 == 0 ? -1 : af1 / af2;

                    // 视频编码名称
                    basicMap.put("codecName", jsonObject.get("codec_long_name").toString());
                    // 平均帧率
                    basicMap.put("avgFrameRate", avgFps+"");
                    // 实时帧率
                    basicMap.put("frameRate", fps+"");

                }else if (jsonObject.get("codec_type").equals("audio")){
                    // 音频包的采样率
                    basicMap.put("sampleRate", jsonObject.get("sample_rate").toString());
                    // 音频声道
                    basicMap.put("channels", jsonObject.get("channels").toString());
                    // 音频编码名称
                    basicMap.put("audioCodecName", jsonObject.get("codec_long_name").toString());
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "获取视频基本信息失败失败" + sourcePath);
        }
        return basicMap;
    }


    public static Integer getVideoLength(String sourcePath){
        return getVideoLength(sourcePath, getVideoInfo(sourcePath), false);
    }
    public static Integer getVideoLength(String sourcePath, Map<String, Object> videoInfoMap, boolean check){
        try {
            if (check){
                Map<String, Object> videoInfoMap1 = getVideoInfo(sourcePath);
                videoInfoMap.putAll(videoInfoMap1);
            }
            Map<String, Object> format = (Map<String, Object>) videoInfoMap.get("format");
            String durationString = format.get("duration").toString();
            durationString = durationString.contains(".") ? durationString.substring(0, durationString.indexOf(".")) : durationString;
            return Integer.valueOf(durationString);
        } catch (Exception e) {
            LogUtil.error(e, "获取视频长度失败1" + sourcePath);
        }
        return 0;
    }
    public static Integer getVideoLength(Map<String, Object> videoInfoMap){
        try {
            Map<String, Object> format = (Map<String, Object>) videoInfoMap.get("format");
            String durationString = format.get("duration").toString();
            durationString = durationString.contains(".") ? durationString.substring(0, durationString.indexOf(".")) : durationString;
            return Integer.valueOf(durationString);
        } catch (Exception e) {
            LogUtil.error(e, "获取视频长度失败" + JsonUtils.beanToJson(videoInfoMap));
        }
        return 0;
    }

    public static String buildKeyUri(Long id, String filePath) {
        String preUrl = filePath.substring(0, filePath.lastIndexOf("."));
        String keyUrl = preUrl + ".key";
        String keyInfo = id + GlobalConfig.M3U8_KEY_INFO_SEPARATOR + keyUrl;
        try {
            keyInfo = AESUtil.encrypt(keyInfo, GlobalConfig.M3U8_AES_PASSWORD);
            keyInfo = URLEncoder.encode(keyInfo, "UTF-8");
        } catch (Exception e){
            LOGGER.error("base64失败, " + e.getMessage());
            throw new SvnlanRuntimeException("500", "失败");
        }
        return "/api/disk/mu/key?key=" + keyInfo;
    }

    public static void buildTsKey(String buildKey, String keyUri, String keyPath, String keyInfoPath) throws Exception{
        List<String> command = new ArrayList<>();
        command.add(buildKey);
        if (buildKey.indexOf(".bat") > 0) {
            command.add("\"" + keyUri + "\""); //windows bat
        } else {
            command.add(keyUri);
        }
        command.add(keyPath);
        command.add(keyInfoPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        String commandStr = "";
        for (String c : command){
            commandStr = commandStr + " " + c;
        }
        LOGGER.info(commandStr);
        Process p = builder.start();
        p.waitFor();
        File file = new File(keyPath);
        if (!file.exists() || file.length() <= 0){
            throw new SvnlanRuntimeException("500", "key文件生成失败");
        }
    }

    /**
       * @Description: 是否是h264格式
       * @params:  [sourcePath]
       * @Return:  boolean
       * @Modified:
       */
    public static boolean checkH264(String sourcePath){
        Map<String, Object> videoInfoMap = getVideoInfo(sourcePath);
        if (videoInfoMap.get("streams") == null){
            return true;
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        JSONObject jsonObject = (JSONObject) streamArray.get(0);
        String codecName = jsonObject.get("codec_name") == null ? "" : jsonObject.get("codec_name").toString();
        if (codecName.toLowerCase().equals("h264")){
            return true;
        }
        return false;
    }

    public static Integer getConvertedLength(String m3u8Path) {
        //m3u8内容
        String m3u8String = FileUtil.getFileContent(m3u8Path);
        if (StringUtil.isEmpty(m3u8String)){
            return 0;
        }
        //查找时间xx.xx秒
        Pattern pattern = Pattern.compile("EXTINF:(\\d*?\\.\\d*?),");
        Matcher m = pattern.matcher(m3u8String);
        Double seconds = 0D;
        while (m.find()){
            seconds += Double.parseDouble(m.group(1));
        }
        return seconds.intValue();
    }
    public static String getVideoPic(String path, String picPath, String resolution) {

        int[] heightAndWidth = null;
        if (!ObjectUtils.isEmpty(resolution)){
            try {
                if (resolution.indexOf("*") >= 0){
                    List<Integer> resList = Arrays.asList(resolution.split("\\*")).stream().map(Integer::valueOf).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(resList) && resList.size() == 2){
                        heightAndWidth = new int[]{resList.get(1), resList.get(0)};
                    }
                }else if (resolution.indexOf("x") >= 0){
                    List<Integer> resList = Arrays.asList(resolution.split("x")).stream().map(Integer::valueOf).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(resList) && resList.size() == 2){
                        heightAndWidth = new int[]{resList.get(1), resList.get(0)};
                    }
                }
            }catch (Exception e){
            }
        }

        if (ObjectUtils.isEmpty(heightAndWidth)) {
            //获取视频宽高
            try {
                heightAndWidth = getHeightAndWidth(path);
            } catch (Exception e) {
                LogUtil.error(e, "获取视频宽高失败, getVideoPic00 path: " + path);
                heightAndWidth = new int[]{0, 0};
            }
        }

        int newWidth = 0;
        int newHeight = 0;
        int imgW = heightAndWidth[1] ;
        int imgH = heightAndWidth[0];
        int scaleW = 300;
        int scaleH = 300;
        if (imgH > imgW && imgH > scaleH){
            newHeight = scaleH;
            double a = (double)scaleH / imgH * imgW;
            newWidth = Integer.valueOf(String.valueOf(Math.round(a)));
        }else if (imgW > imgH && imgW > scaleW){
            newWidth = scaleW;
            double a = (double)scaleW / imgW * imgH;
            newHeight = Integer.valueOf(String.valueOf(Math.round(a)));
        }else {
            newWidth = heightAndWidth[1];
            newHeight = heightAndWidth[0];
        }


        List<String> cutpic = new ArrayList<>();
        cutpic.add("ffmpeg");
        cutpic.add("-i");
        cutpic.add(path); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add("2" ); // 添加起始时间为第3秒
       // cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
       // cutpic.add("0.001"); // 添加持续时间为1毫秒

        if (newHeight != 0 && newHeight != 0) {
            cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
            cutpic.add("" + newWidth + "*" + newHeight);
        } else {
            cutpic.add("-vf");
            cutpic.add("scale=-1:200");
        }
        cutpic.add("-q:v");
        cutpic.add("5");

        cutpic.add("-loglevel");
        cutpic.add("quiet");
        cutpic.add(picPath); // 添加截取的图片的保存路径
        ProcessBuilder builder;
        StringBuilder sb = new StringBuilder();
        try {
            File picFile = new File(picPath);
            if (!picFile.getParentFile().exists()){
                picFile.getParentFile().mkdirs();
            }
            sb = new StringBuilder();
            for (String a : cutpic){
                sb.append(a);
                sb.append(" ");
            }
            LogUtil.info(sb.toString());
            builder = new ProcessBuilder();
            builder.command(cutpic);
            Process process = builder.start();
            process.waitFor();
        } catch (Exception e){
            LogUtil.error("获取视频截图失败" + sb.toString());
        }
        return "";
    }
    public static boolean getAudioPic(String path, String picPath) {
        RandomAccessFile file = null;
        boolean check = false;
        try {
            File picFile = new File(picPath);
            if (!picFile.getParentFile().exists()){
                picFile.getParentFile().mkdirs();
            }
            Mp3File mp3file = new Mp3File(path);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                byte[] albumImageData = id3v2Tag.getAlbumImage();
                if (albumImageData != null) {
                    LogUtil.info("getAudioPic 专辑插图长度: " + albumImageData.length + " bytes"+
                            "， 专辑插图类型: " + id3v2Tag.getAlbumImageMimeType()
                    + "， picPath: " + picPath);
                    file = new RandomAccessFile(picPath, "rw");
                    file.write(albumImageData);
                    check = true;
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "getAudioPic error  path=" + path);
        }finally {
            if (file != null){
                try {
                    file.close();
                } catch (Exception e){
                    LogUtil.error(e, " getAudioPic 关闭流错误" + path);
                }
            }
        }
        return check;
    }
    public static int[] getHeightAndWidthAndduration(String sourcePath) {
        Map<String, Object> videoInfoMap = getVideoInfo(sourcePath);
        if (videoInfoMap.get("streams") == null){
            return new int[]{0, 0, 0};
        }
        JSONArray streamArray = (JSONArray)videoInfoMap.get("streams");
        Integer height = 0;
        Integer width = 0;
        Integer rotation = 0;
        Integer duration = 0;

        for (Object o : streamArray){
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("height") != null) {
                height = Integer.parseInt(jsonObject.get("height").toString());
            }
            if (jsonObject.get("width") != null){
                width = Integer.parseInt(jsonObject.get("width").toString());
            }
            if (jsonObject.get("duration") != null){
                String durationString = jsonObject.get("duration").toString();
                durationString = durationString.contains(".") ? durationString.substring(0, durationString.indexOf(".")) : durationString;
                duration = Integer.parseInt(durationString);
            }
            if (height > 0 && width > 0){
                try {
                    if (jsonObject.get("side_data_list") != null){
                        JSONArray sideList = (JSONArray)jsonObject.get("side_data_list");
                        if (!CollectionUtils.isEmpty(sideList)){
                            JSONObject sideObject = (JSONObject) sideList.get(0);
                            if (!ObjectUtils.isEmpty(sideObject.get("rotation"))){
                                rotation = Integer.parseInt(sideObject.get("rotation").toString());
                                rotation = Math.abs(rotation);
                            }
                        }
                    }
                }catch (Exception e){
                    LogUtil.error(e, " getHeightAndWidthAndduration error ");
                }
                break;
            }
        }
        if (rotation == 90 || rotation == 270){
            // 转角度则交换宽高
            return new int[]{width, height, duration};
        }
        return new int[]{height, width, duration};
    }

    /**
     * @description: 转码成mp3文件
     * @param prefix
     * @param ffmpegPath
     * @param upFilePath
     * @param mp3FilePath
     * @return boolean
     */
    public static boolean convertToMp3(String prefix, String ffmpegPath, String upFilePath, String mp3FilePath) {
        // 创建一个List集合来保存转换文件的命令
        List<String> convert = new ArrayList<>();
        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的视频文件的路径
        convert.add("-acodec");
        convert.add("mp3");
        convert.add(mp3FilePath);//转换后的文件路径
        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            String cmdStr = sb.toString();
            LogUtil.info(prefix + " cmd: " + cmdStr);
            builder.command(convert);
            Process process =builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            in.close();
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error(prefix + "转mp3异常, " + errorString + cmdStr);
            }
        } catch (Exception e) {
            mark = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(prefix + "关闭流失败");
                }
            }
        }
        return mark;
    }
}
