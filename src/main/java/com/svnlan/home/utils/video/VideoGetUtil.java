package com.svnlan.home.utils.video;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.MarkUrlDto;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.VideoCutDto;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/17 13:14
 */
@Component
public class VideoGetUtil {

    private static final Logger log = LoggerFactory.getLogger("error");

    /**
     * 开启线程处理Ffmpeg处理流
     *
     * @param process
     */
    private static void dealStream(Process process) {
        if (process == null) {
            return;
        }
        // 处理InputStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
                        LogUtil.info(  "处理InputStream的线程 warn  output: " + line);
                    }
                } catch (IOException e) {
                    LogUtil.error(e, "处理InputStream的线程 error");
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LogUtil.error(e, "处理InputStream的线程 error");
                    }
                }
            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
                        LogUtil.info(  "处理ErrorStream的线程 warn  output: " + line);
                    }
                } catch (IOException e) {
                    LogUtil.error(e, "处理ErrorStream的线程 error");
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        LogUtil.error(e, "处理ErrorStream的线程 error");
                    }
                }
            }
        }.start();
    }
    /**
     * 截取一帧作为图片
     *
     * @param path
     *            ffmpeg -i /Users/jinx/Downloads/test.mp4 -y -f image2 -ss
     *            00:00:01 -vframes 1 /Users/jinx/Downloads/test.jpeg
     * @return
     */
    public static boolean covPic( String path, String time, String outName, String size) {

        int[] heightAndWidth;
        String resolution = "";
        try {
            heightAndWidth = VideoUtil.getHeightAndWidthAndduration(path);
        } catch (Exception e){
            LogUtil.error("获取视频宽高失败, path: " + path + ", e: " + e.getMessage());
            heightAndWidth = new int[]{0, 0, 0};
        }
        int duration = 3;
        try {
            if (heightAndWidth[2] != 0 && heightAndWidth[2] < 3) {
                duration = 1;
            }
        }catch (Exception e){
            LogUtil.error("获取视频宽高失败2, path: " + path + ", e: " + e.getMessage());
        }

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(path);//文件输入源
        command.add("-y");
        command.add("-f");
        command.add("image2");
        command.add("-ss");
        command.add(time);//截取的时间点格式为 00:00:01
        command.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
        command.add("0.001"); // 添加持续时间为1毫秒
        command.add("-vframes");
        command.add("1");
        command.add("-s");
        if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
            command.add("" + heightAndWidth[1] + "*" + heightAndWidth[0]);
        } else {
            command.add("1280*720"); //截取的图片大小，格式为：1920x1080
        }
        command.add(outName);//文件输出路径，格式为 11.jpg


        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("covPic  cmd:" + sb.toString());

        return execCommand(command);
    }

    /**
     * 视频截图
     * @param	tempVideoPath	视频路径
     * @param	beginN		第几帧开始
     * @param	beginN		第几帧结束
     * @param	tempImgPath	从视频中截取多张图片
     * @param	frameInterval	间隔多少帧取图片
     *                          多个-vf 会覆盖前面的
     */
    public static boolean covPicBatch( String tempVideoPath, String tempImgPath, Integer beginN, Integer endN, Integer frameInterval) {

        LogUtil.info("covPicBatch beginN=" + beginN + "，endN=" + endN);
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(tempVideoPath);//文件输入源
        command.add("-vf");
        command.add("select='between\\(n\\,"+(beginN)+"\\,"+ (endN) +"\\)*not\\(mod\\(n\\,"+ frameInterval +"\\)\\)',scale=-1:64");
        command.add("-vsync");
        command.add("2");
        command.add("-f");
        command.add("image2");
        command.add(tempImgPath);//文件输出路径，格式为 11.jpg

        /*
        ffmpeg -i /uploads/private/cloud/2023_5/25/3925493832a24b67a16cb94ffb139dd21684998268922_1.mp4 -q 20 -vf select='between\(n\,0\,3288\)*not\(mod\(n\,24\)\)',scale=-1:32   -vsync 0  /uploads/common/down_temp/private/cloud/2023_5/25/3925493832a24b67a16cb94ffb139dd21684998268922_1/25d23d8b950b4df1b2532967fb4e3384/2_137_2/3925493832a24b67a16cb94ffb139dd21684998268922_1_mp4_cut_%02d.jpg

         */
        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("covPicBatch  cmd:" + sb.toString());
        return execCommand(command);
    }

    /**
     * 视频截图
     * @param	tempVideoPath	视频路径
     * @param	beginN		开始时间
     * @param	tempImgPath	从视频中截取图片
     */
    public static boolean covPicBatch(String tempVideoPath, String tempImgPath, String beginN) {
        return covPicBatch(tempVideoPath, tempImgPath, beginN, true);
    }
    public static boolean covPicBatch(String tempVideoPath, String tempImgPath, String beginN, boolean isSetResolution) {
        LogUtil.info("covPicBatch begin进入流程 : beginN"+ beginN + "，time=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS) );
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-ss");
        command.add(beginN);
        command.add("-i");
        command.add(tempVideoPath);//文件输入源
        // 是否设置分辨率
        if (isSetResolution) {
            command.add("-vf");
            command.add("scale=-1:100");
        }
        command.add("-f");
        command.add("image2");
        command.add(tempImgPath);//文件输出路径，格式为 11.jpg
        /*
        ffmpeg -ss 00:02:06 -i test1.flv -f image2 -y test1.jpg
         */
        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("covPicBatch  cmd:" + sb.toString());
        LogUtil.info("covPicBatch end : time=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS) );
        return execCommand(command);
    }

    /**
       * @Description:
       * @params:  [tempVideoPath, tempImgPath, beginN, endN, frameInterval]
       * @Modified:
       */
    public static boolean covPicBatch(String tempVideoPath, String tempImgPath, String  beginN, String endN, String frameInterval) {
        /**   ffmpeg -ss 5  -i test.mp4 -t 20 t.mp4
         * 截取视频的 5-25秒
         -ss 开始时间
         -t截取时间
         -r每秒取多少帧
         */
        LogUtil.info("covPicBatch begin beginN=" + beginN + "，endN=" + endN);
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-ss");
        command.add(beginN);
        command.add("-i");
        command.add(tempVideoPath);//文件输入源
        command.add("-t");
        command.add(endN);
        command.add("-r");
        command.add(frameInterval);
        command.add("-vf");
        command.add("scale=-1:64");
        command.add("-f");
        command.add("image2");
        command.add(tempImgPath);//文件输出路径，格式为 11.jpg
        /*
       ffmpeg -i input.mp4 -ss 00:00:20 -t 10 -r 1 -q:v 2 -f image2 pic-%03d.jpeg

         */
        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("covPicBatch  end  beginN=" + beginN + "，endN=" + endN + "  cmd:" + sb.toString());
        return execCommand(command);
    }

    /**
     * 视频截图
     * @param	tempVideoPath	原视频路径
     * @param	timeVoList		截取时间段列表
     * @param	tempOutPath	从视频中截取多张图片
     */
    public static List<String> covCutBatch( String tempVideoPath, String tempOutPath, List<VideoCutDto> timeVoList, VideoCommonDto videoCommonDto, ThreadPoolTaskExecutor executor) {

        LogUtil.info("covCutBatch tempVideoPath=" + tempVideoPath + "，tempOutPath=" + tempOutPath + "，timeVo=" + JsonUtils.beanToJson(timeVoList));

        List<String> cutList = new ArrayList<>();


        int i = 1;
        boolean check = true;

        String tempOrgPath = "";
        // 校验是否需要设置
        boolean settingCheck = checkIsSettingVideo(videoCommonDto) ;
        Boolean settingResult = false;
        if (settingCheck){
            // 原文件
            videoCommonDto.setVideoPathOrg(tempVideoPath);

            tempOrgPath = tempOutPath.replace("_%02d." , "_temp.");
            videoCommonDto.setFinalFilePath(tempOrgPath);
            // 先设置再切
            settingResult =  configVideoMerge(videoCommonDto);
        }
        //使用Future方式执行多任务
        //生成一个集合
        List<Future> futures = new ArrayList<>();
        for (VideoCutDto cutDto : timeVoList){
            String tempOut = String.format(tempOutPath, i);
            cutDto.setPath(tempOut);

            Future<?> future = executor.submit(() -> {
                LogUtil.info("covCutBatch slipt future tempOut=" + tempOut + ", beginTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS));
                List<String> command = new ArrayList<>();
                /* ffmpeg -ss 2:05 -i input.mp4 -t 20 -c:v copy -c:a copy output.mp4
                这里把-ss 2:05 放在前面，与原来的区别是，这样会先跳转到第2：05开始解码，而原来的会从开始开始解码，会将2：05前的结果丢掉
                -t 持续时间
                -c:v 和 -c:a 分别指视频编码格式和音频编码格式
                -c:v copy 和 -c:v copy 指的是视频的编码格式不变，直接复制，从而加快速度 */
                command.add("ffmpeg");
                command.add("-ss");
                command.add(cutDto.getBeginTime() + "");
                command.add("-i");
                command.add(tempVideoPath);//文件输入源
                command.add("-t");
                command.add((cutDto.getDuration()) + "");
                command.add("-c:v");
                command.add("copy");
                command.add("-c:a");
                command.add("copy");
                command.add(tempOut);//文件输出路径

                StringBuilder sb = new StringBuilder();
                for (String a : command){
                    sb.append(a);
                    sb.append(" ");
                }
                LogUtil.info("covPicBatch  cmd:" + sb.toString());
                try {
                    Process videoProcess = new ProcessBuilder(command).start();
                    dealStream(videoProcess);
                    videoProcess.waitFor();
                    cutList.add(tempOut);
                } catch (Exception e) {
                    LogUtil.error(e, "covPicBatch error");
                }
                LogUtil.info("covCutBatch slipt future tempOut=" + tempOut + ", endTime=" + DateDUtil.getYearMonthDayHMS(new Date(), DateDUtil.YYYY_MM_DD_HH_MM_SS));
            });
            futures.add(future);
            i ++;
        }

        if (!CollectionUtils.isEmpty(futures)){
            try {
                //查询任务执行的结果
                for (Future<?> future : futures) {
                    while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                        if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。

                            LogUtil.info("covCutBatch 任务i=" + future.get() + "获取完成!" + new Date());
                            break;//当前future获取结果完毕，跳出while
                        } else {
                            Thread.sleep(1);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                        }
                    }
                }
            }catch (Exception e){
                LogUtil.error(e, "covCutBatch error ");
            }
        }

        if (settingResult && !ObjectUtils.isEmpty(tempOrgPath)){
            try {
                File dampFile = new File(tempOrgPath);
                if (dampFile.exists()){
                    dampFile.delete();
                }
            }catch (Exception e){
                LogUtil.error(e, "covCutBatch configVideoMerge delete error tempOrgPath=" + tempOrgPath);
            }
        }

        if (!check){
            return new ArrayList<>();
        }
        return cutList;
    }

    /**
     * @Description: 异步合并剪切的视频
     * @params:  cutList 剪切的视频路径列表
     * @params:  finishPath 合并完成的最终视频
     * @params:  suffix 文件后缀
     * @params:  tempPath temp路径
     */
    public static Boolean cutVideoMerge(List<String> cutList, String finishPath, String suffix, String tempPath,List<String> tsList) {
        /*
        ffmpeg -i 1.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 1.ts
ffmpeg -i 2.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 2.ts
ffmpeg -i "concat:1.ts|2.ts" -acodec copy -vcodec copy -absf aac_adtstoasc output.mp4

         */
        Long time = System.currentTimeMillis();

        Process videoProcess = null;
        List<String> command = null;
        StringBuilder sb = null;
        StringBuilder sbtxt = new StringBuilder();
        String tempTxt = cutList.get(0).replace("." + suffix, time + "_fileList.txt");
        tsList.add(tempTxt);

        int i = 1;
        boolean check = true;
        LogUtil.info("cutVideoMerge finishPath=" + finishPath
                + "，suffx=" + suffix
                + "，tempPath=" + tempPath
                + "， cutList=" + JsonUtils.beanToJson(cutList));
        for (String path : cutList){
            command = new ArrayList<>();
            command.add("ffmpeg");
            command.add("-i");
            command.add(path);
            command.add("-vcodec");
            command.add("copy");
            command.add("-acodec"); //
            command.add("copy");
            command.add("-vbsf");
            command.add("h264_mp4toannexb");
            String tsPath = path.replace("." + suffix, time+ "_" + i + ".ts");
            command.add(tsPath);//文件输入源
            if (i == 1) {
                sbtxt.append("file '" + tsPath +"'");
            }else {
                sbtxt.append("\nfile '" + tsPath +"'");
            }
            // ffmpeg -i 01.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 01.ts
            tsList.add(path.replace("." + suffix, ".ts"));
            sb = new StringBuilder();
            for (String a : command){
                sb.append(a);
                sb.append(" ");
            }
            LogUtil.info("cutVideoMerge  sbtxt=" + sbtxt.toString());
            LogUtil.info("cutVideoMerge  cmd:" + sb.toString());
            try {
                videoProcess = new ProcessBuilder(command).start();
                dealStream(videoProcess);
                videoProcess.waitFor();
            } catch (Exception e) {
                check = false;
                LogUtil.error(e, "cutVideoMerge error");
                break;
            }
            i ++;
        }
        if (!check){
            return false;
        }
        File finishTxtFile = new File(tempTxt);
        byte[] size = sbtxt.toString().getBytes(StandardCharsets.UTF_8);
        FileInputStream fis = null;
        try {
            FileUtil.writeFile(finishTxtFile, size);
            fis = new FileInputStream(finishTxtFile);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " cutVideoMerge 创建文件失败 tempTxt=" + tempTxt);
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }

        // ffmpeg -f concat -safe 0 -i mylist.txt -c copy output.wav
        command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-i");
        command.add(tempTxt);
        command.add("-c");
        command.add("copy");
        command.add(finishPath);//文件输入源
        sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("cutVideoMerge  cmd:" + sb.toString());
        try {
            videoProcess = new ProcessBuilder(command).start();
            dealStream(videoProcess);
            videoProcess.waitFor();
        } catch (Exception e) {
            check = false;
            LogUtil.error(e, "cutVideoConcate error");
        }

        return check;
    }

    /**
     * @Description: 转码视频
     * @params:  path 原视频路径
     * @params:  finishPath 合并完成的最终视频
     * @params:  resolution 分辨率
     * @params:  frameRate 帧率
     */
    public static Boolean convertVideoMerge(String path, String finishPath, String resolution, Integer frameRate) {

        boolean check = true;
        Process videoProcess = null;
        List<String> command = null;
        StringBuilder sb = null;
        LogUtil.info("convertVideoMerge finishPath=" + finishPath
                + "， path=" + path);
        /* 【参数说明】 -s 1920x1080：调整分辨率 -r 24：调整帧率  */
        // ffmpeg -i input.mp4  -s 1920x1080 -r 24  output.mp4  -vcodec h264 -b:v 0


        int[] heightAndWidth;
        try {
            heightAndWidth = VideoUtil.getHeightAndWidthAndduration(path);
        } catch (Exception e){
            LogUtil.error("获取视频宽高失败, path: " + path + ", e: " + e.getMessage());
            heightAndWidth = new int[]{0, 0, 0};
        }
        int duration = 3;
        try {
            if (heightAndWidth[2] != 0 && heightAndWidth[2] < 3) {
                duration = 1;
            }
        }catch (Exception e){
            LogUtil.error("获取视频宽高失败2, path: " + path + ", e: " + e.getMessage());
        }
        LogUtil.info("convertVideoMerge ----------------------duration=" + duration + "， heightAndWidth=" + JsonUtils.beanToJson(heightAndWidth));

        int width = heightAndWidth[1];
        int height = heightAndWidth[0];

        int w = 0;
        int h = 0;
        //视频宽:resolution
        if (resolution.indexOf("*") >= 0){
            List<Integer> whList = Arrays.asList(resolution.split("\\*")).stream().map(Integer::valueOf).collect(Collectors.toList());
            w = whList.get(0);
            h = whList.get(1);
        }else if (resolution.indexOf("x") >= 0){
            List<Integer> whList = Arrays.asList(resolution.split("x")).stream().map(Integer::valueOf).collect(Collectors.toList());
            w = whList.get(0);
            h = whList.get(1);
        }
        BigDecimal widthRatio = new BigDecimal(width).divide(new BigDecimal(h),1,BigDecimal.ROUND_UP);
        BigDecimal heightReal = new BigDecimal(height).divide(widthRatio,BigDecimal.ROUND_UP);
        //BigDecimal value = new BigDecimal(w).subtract(heightReal).divide(new BigDecimal(2),BigDecimal.ROUND_UP);



        command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(path);
        command.add("-vf");
        command.add("scale=" + h +":"+heightReal.intValue());
       // command.add("scale=" + h +":"+heightReal.intValue()+",pad=" + h+":" + w +":0:"+value.intValue()+":black");
        command.add("-r");
        command.add(frameRate + "");
        command.add("-vcodec");
        command.add("h264");
        command.add("-b:v");
        command.add("0");

        command.add(finishPath);//文件输出源

        sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("convertVideoMerge  cmd:" + sb.toString());
        try {
            videoProcess = new ProcessBuilder(command).start();
            dealStream(videoProcess);
            videoProcess.waitFor();
        } catch (Exception e) {
            check = false;
            LogUtil.error(e, "convertVideoMerge error");
        }
        return check;
    }

    /**
     * @Description: 设置视频
     * @params:  path 原视频路径
     * @params:  finishPath 合并完成的最终视频
     * @params:  resolution 分辨率
     * @params:  frameRate 帧率
     */
    public static Boolean configVideoMerge(VideoCommonDto checkFileDTO) {


        String path = checkFileDTO.getVideoPathOrg();
        String finishPath = checkFileDTO.getFinalFilePath();
        String dampingTempPath = path.substring(path.lastIndexOf("/") + 1).replace(".","_")+ "_transforms.trf" ;
        //String dampingTempPath = path.substring(0, finishPath.lastIndexOf("/")+1 )  + dampingTempName;

        String coverUrl = "";
        if (!ObjectUtils.isEmpty(checkFileDTO.getCoverImg()) && !ObjectUtils.isEmpty(checkFileDTO.getCoverName())){
            if (checkFileDTO.getCoverImg().indexOf(GlobalConfig.private_replace_key) >= 0){
                coverUrl =  checkFileDTO.getCoverImg().replace(GlobalConfig.private_replace_key, "/private/") + checkFileDTO.getCoverName();
            }else {
                coverUrl = GlobalConfig.default_disk_path_pre + "/private" + checkFileDTO.getCoverImg() + checkFileDTO.getCoverName();
            }
            File coverFile = new File(coverUrl);
            if (!coverFile.exists()){
                coverUrl = null;
            }
        }
        // 防抖、减震
        if (!ObjectUtils.isEmpty(checkFileDTO.getDampingShakiness())){
            exeDamping(path, dampingTempPath, checkFileDTO.getDampingShakiness(), checkFileDTO.getDampingAccuracy());
        }
        boolean check = true;
        Process videoProcess = null;
        List<String> command = null;
        StringBuilder sb = null;
        boolean c = false;
        StringBuilder settingB = new StringBuilder();
        LogUtil.info("configVideoMerge finishPath=" + finishPath
                + "， path=" + path);
        /* 【参数说明】 -s 1920x1080：调整分辨率 -r 24：调整帧率  */
        // ffmpeg -i input.mp4  -s 1920x1080 -r 24  output.mp4
        command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(path);

        if (!ObjectUtils.isEmpty(coverUrl)){
            command.add("-i");
            command.add(coverUrl);
        }

        // 帧率
        if (!ObjectUtils.isEmpty(checkFileDTO.getFrameRate())) {
            command.add("-r");
            command.add(checkFileDTO.getFrameRate() + "");
        }
        // 音量
        if (!ObjectUtils.isEmpty(checkFileDTO.getVolume()) || !ObjectUtils.isEmpty(checkFileDTO.getShifting())){
            command.add("-af");
            StringBuilder sbShifting = new StringBuilder();
            boolean a = false;
            if (!ObjectUtils.isEmpty(checkFileDTO.getVolume()) ) {
                sbShifting.append("volume=" + checkFileDTO.getVolume());
                a = true;
            }
            if (!ObjectUtils.isEmpty(checkFileDTO.getShifting()) && !ObjectUtils.isEmpty(checkFileDTO.getVolumeShifting())
                    && StringUtil.isNumericDecimal(checkFileDTO.getVolumeShifting())){

                double p = Double.valueOf(checkFileDTO.getVolumeShifting());
                if (p > 2){
                    sbShifting.append((a ? "," : ""));
                    int n = 0;
                    while (p > 0){
                        if (n > 0){
                            sbShifting.append(",");
                        }
                        if (p > 2){
                            sbShifting.append("atempo=2.0" );
                        }else {
                            sbShifting.append("atempo=" + p );
                        }
                        p = p - 2;
                        n ++;
                    }
                }else {
                    sbShifting.append((a ? "," : "")+"atempo=" + (0.5 + p * 0.5));
                }
            }
            command.add(sbShifting.toString());
        }


        if (!CollectionUtils.isEmpty(checkFileDTO.getMarkUrlList())){
            /*
ffmpeg -i /uploads/private/cloud/2023_6/14/479dbf93ebe44f61b624aa55fcf5e8021686724044186_30074.mp4 -i /uploads/private/cloud/2023_6/15/9f68848570c6494bb13adf8a90d8cff61686806004019_30074.png -i /uploads/private/cloud/2023_6/15/9f68848570c6494bb13adf8a90d8cff61686806004019_30074.png -i /uploads/private/cloud/2023_6/15/9f68848570c6494bb13adf8a90d8cff61686806004019_30074.png -filter_complex [1:v]scale=15:15,format=yuva444p,colorchannelmixer=aa=0.1[v1],[0:v][v1]overlay=30:10[vp],[2:v]scale=20:20,format=yuva444p,colorchannelmixer=aa=0.5[s],[vp][s]overlay=5:5[vo],[3:v]scale=16:10,format=yuva444p,colorchannelmixer=aa=0.9[s2],[vo][s2]overlay=10:30     -vcodec h264 -b:v 0 /uploads/private/cloud/2023_6/15/d1276bfc6b704328a3642bc14f7fcaff1686813747745_1.mp4
             */
            for (MarkUrlDto markUrl : checkFileDTO.getMarkUrlList()){
                command.add("-i");
                if (markUrl.getUrl().indexOf(GlobalConfig.private_replace_key) >= 0){
                    command.add( markUrl.getUrl().replace(GlobalConfig.private_replace_key, "/private/") + markUrl.getName());
                }else {
                    command.add( GlobalConfig.default_disk_path_pre + "/private" + markUrl.getUrl() + markUrl.getName());
                }

            }
            command.add("-max_muxing_queue_size");
            command.add("1024");
            command.add("-filter_complex");
            StringBuilder sbui = new StringBuilder();
            int i = 0;
            for (MarkUrlDto markUrl : checkFileDTO.getMarkUrlList()){
                /*
                [1:v]scale=15:15,format=yuva444p,colorchannelmixer=aa=0.1[v1],[0:v][v1]overlay=30:10[vp]
                ,[2:v]scale=20:20,format=yuva444p,colorchannelmixer=aa=0.5[s],[vp][s]overlay=5:5[vo]
                ,[3:v]scale=16:10,format=yuva444p,colorchannelmixer=aa=0.9[s2],[vo][s2]overlay=10:30
                 */
                if (i > 0){
                    sbui.append(",");
                }
                sbui.append("[" + (i+1)+":v]scale="+markUrl.getScale());
                if (!ObjectUtils.isEmpty(markUrl.getFormat())){
                    sbui.append(",format="+markUrl.getFormat());
                }
                if (!ObjectUtils.isEmpty(markUrl.getColorchannelmixer())){
                    sbui.append(",colorchannelmixer="+markUrl.getColorchannelmixer());
                }
                sbui.append("[img" + (i+1) +"]");

                sbui.append(",");
                if (i > 0){
                    sbui.append("[" + (i-1) +":oe]");
                }else {
                    sbui.append("[" + (i) +":v]");
                }
                sbui.append("[img" + (i+1) +"]");
                sbui.append("overlay="+markUrl.getPosition());
                if ((i+1) < checkFileDTO.getMarkUrlList().size()){
                    sbui.append("[" + (i) +":oe]");
                }


                i++;
            }

            command.add(sbui.toString());
        }

        // 防抖、减震
        if (!ObjectUtils.isEmpty(checkFileDTO.getDampingShakiness())){
            // ffmpeg -i 三山五园绿道.MOV -vf vidstabdetect=stepsize=32:shakiness=10:accuracy=10:result=transform_vectors.trf -f null -
            // ffmpeg -i 三山五园绿道.MOV -y -vf vidstabtransform=input=transform_vectors.trf:zoom=0:smoothing=10 三山五园绿道稳定版.MOV
            // zoom 设置缩放百分比。 正值将导致放大效果，缩小效果中的负值。 默认值为0（无缩放）
            // smoothing 设置用于低通滤波相机移动的帧数（value*2 + 1）。 默认值为10。
            command.add("-y");
            settingB.append((c ? "," : "") + "vidstabtransform=input=" + dampingTempPath +":zoom=0:smoothing=10");
            c = true;
        }


        // 裁剪
        if (!ObjectUtils.isEmpty(checkFileDTO.getCrop())) {
            settingB.append((c ? "," : "") + "crop=" + checkFileDTO.getCrop());
            c = true;
        }
        // 变速
        if (!ObjectUtils.isEmpty(checkFileDTO.getShifting())){
            settingB.append((c ? "," : "") + "setpts=" + checkFileDTO.getShifting() +"*PTS");
            c = true;
        }
        // 分辨率
        if (!ObjectUtils.isEmpty(checkFileDTO.getResolution())) {


            int[] heightAndWidth;
            try {
                heightAndWidth = VideoUtil.getHeightAndWidthAndduration(path);
            } catch (Exception e){
                LogUtil.error("获取视频宽高失败, path: " + path + ", e: " + e.getMessage());
                heightAndWidth = new int[]{0, 0, 0};
            }
            int duration = 3;
            try {
                if (heightAndWidth[2] != 0 && heightAndWidth[2] < 3) {
                    duration = 1;
                }
            }catch (Exception e){
                LogUtil.error("获取视频宽高失败2, path: " + path + ", e: " + e.getMessage());
            }
            LogUtil.info("convertVideoMerge ----------------------duration=" + duration + "， heightAndWidth=" + heightAndWidth);

            int width = heightAndWidth[1];
            int height = heightAndWidth[0];

            int w = 0;
            int h = 0;
            String resolution = checkFileDTO.getResolution();
            //视频宽:resolution
            if (resolution.indexOf("*") >= 0){
                List<Integer> whList = Arrays.asList(resolution.split("\\*")).stream().map(Integer::valueOf).collect(Collectors.toList());
                w = whList.get(0);
                h = whList.get(1);
            }else if (resolution.indexOf("x") >= 0){
                List<Integer> whList = Arrays.asList(resolution.split("x")).stream().map(Integer::valueOf).collect(Collectors.toList());
                w = whList.get(0);
                h = whList.get(1);
            }
            BigDecimal widthRatio = new BigDecimal(width).divide(new BigDecimal(h),1,BigDecimal.ROUND_UP);
            BigDecimal heightReal = new BigDecimal(height).divide(widthRatio,BigDecimal.ROUND_UP);
            //BigDecimal value = new BigDecimal(w).subtract(heightReal).divide(new BigDecimal(2),BigDecimal.ROUND_UP);

            //settingB.append((c ? "," : "") + "scale=" + w + ":" + h +",pad=" + w+":" + h +":0:"+value.intValue()+":black");
            settingB.append((c ? "," : "") + "scale=" + w + ":" + h );
            c = true;
        }
        // 水印
        if (!CollectionUtils.isEmpty(checkFileDTO.getMarkTextList())){
            for (String markTest : checkFileDTO.getMarkTextList()){
                settingB.append((c ? "," : "") + "drawtext=" + markTest);
                c = true;
            }
        }

        // 字幕
        if (!ObjectUtils.isEmpty(checkFileDTO.getCaptions()) && !ObjectUtils.isEmpty(checkFileDTO.getCaptionsUrl())){
            if (checkFileDTO.getCaptions().lastIndexOf(".srt") >= 0){
                settingB.append((c ? "," : "") + "subtitles=" + checkFileDTO.getCaptionsUrl().replace(GlobalConfig.private_replace_key, "/private/") + checkFileDTO.getCaptions());
                c = true;

            }else if (checkFileDTO.getCaptions().lastIndexOf(".ass") >= 0){
                settingB.append((c ? "," : "") + "ass=" + checkFileDTO.getCaptionsUrl().replace(GlobalConfig.private_replace_key, "/private/") + checkFileDTO.getCaptions());
                c = true;
            }
        }
        // 旋转  1 左转 2 右转
        if (!ObjectUtils.isEmpty(checkFileDTO.getRotateType()) && !ObjectUtils.isEmpty(checkFileDTO.getRotate())){
            if (checkFileDTO.getRotate().intValue() == 90){
                settingB.append((c ? "," : "") + "transpose=" + ("2".equals(checkFileDTO.getRotateType()) ? "1" : "2" ));
                c = true;
            }else if (checkFileDTO.getRotate().intValue() == 180){
                settingB.append((c ? "," : "") + "transpose=" + ("2".equals(checkFileDTO.getRotateType()) ? "1" : "2" )+ ",transpose=" + ("2".equals(checkFileDTO.getRotateType()) ? "1" : "2" ));
                c = true;
            }else {
                command.add("-metadata:s:v:0");
                command.add("transpose=" + ("2".equals(checkFileDTO.getRotateType()) ? checkFileDTO.getRotate() : (360 - checkFileDTO.getRotate()) ));
            }
        }
        // 翻转  1 水平 2 垂直
        if (!ObjectUtils.isEmpty(checkFileDTO.getOverturnType())){
            if ("1".equals(checkFileDTO.getOverturnType())){
                settingB.append((c ? "," : "") + "hflip");
                c = true;
            }else {
                settingB.append((c ? "," : "") + "vflip");
                c = true;
            }
        }

        // 多个-vf逗号隔开
        if (c){
            command.add("-vf");
            command.add(settingB.toString());
        }
        command.add("-vcodec");
        command.add("h264");
        command.add("-b:v");
        command.add("0");
        if (!ObjectUtils.isEmpty(coverUrl)) {
            // 设置封面
            command.add("-map 1 -map 0 -c copy -disposition:0 attached_pic");
        }


        command.add(finishPath);//文件输出源

        sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("configVideoMerge  cmd:" + sb.toString());
        try {
            videoProcess = new ProcessBuilder(command).start();
            dealStream(videoProcess);
            videoProcess.waitFor();
        } catch (Exception e) {
            check = false;
            LogUtil.error(e, "configVideoMerge error");
        }
        /*try {
            File dampFile = new File(dampingTempPath);
            if (dampFile.exists()){
                dampFile.delete();
            }
        }catch (Exception e){
            LogUtil.error(e, "configVideoMerge delete error dampingTempPath=" + dampingTempPath);
        }*/
        return check;
    }

    public static Boolean checkIsSettingVideo(VideoCommonDto checkFileDTO) {

        boolean check = false;

        if (ObjectUtils.isEmpty(checkFileDTO)){
            return check;
        }
        // 分辨率
        if (!ObjectUtils.isEmpty(checkFileDTO.getResolution())) {
            check = true;
        }else
        // 帧率
        if (!ObjectUtils.isEmpty(checkFileDTO.getFrameRate())) {
            check = true;
        }else
        // 音量
        if (!ObjectUtils.isEmpty(checkFileDTO.getVolume())){
            check = true;
        }else
        // 水印
        if (!CollectionUtils.isEmpty(checkFileDTO.getMarkTextList())){
            check = true;
        }else
        if (!CollectionUtils.isEmpty(checkFileDTO.getMarkUrlList())){
            check = true;
        }else
        // 字幕
        if (!ObjectUtils.isEmpty(checkFileDTO.getCaptions())){
            check = true;
        }else
        // 变速
        if (!ObjectUtils.isEmpty(checkFileDTO.getShifting())){
            check = true;
        }else
        // 防抖、减震
        if (!ObjectUtils.isEmpty(checkFileDTO.getDampingShakiness())){
            check = true;
        }else
        // 旋转  1 左转 2 右转
        if (!ObjectUtils.isEmpty(checkFileDTO.getRotateType()) && !ObjectUtils.isEmpty(checkFileDTO.getRotate())){
            check = true;
        }else
        // 翻转  1 水平 2 垂直
        if (!ObjectUtils.isEmpty(checkFileDTO.getOverturnType())){
            check = true;
        }
        return check;
    }

    /* 防抖、减震
    shakiness 设置视频的抖动程度以及相机的速度。它接受1-10范围内的整数，值1表示少量抖动，值10表示强烈颤抖。默认值为5。
    accuracy 设置检测过程的准确性。它必须是1-15范围内的值。值1表示精度低，值15表示高精度。默认值为15。
     */
    public static Boolean exeDamping(String path, String dampingTempPath, String shakiness, String accuracy) {


        boolean check = true;
        Process videoProcess = null;
        StringBuilder sb = null;
        LogUtil.info("configVideoMerge exeDamping dampingTempPath=" + dampingTempPath
                + "， path=" + path);
        /* ffmpeg -i 三山五园绿道.MOV -vf   */
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(path);//
        command.add("-vf");
        command.add("vidstabdetect=shakiness=" + shakiness + ":accuracy=" + accuracy +":result=" + dampingTempPath );
        command.add("-f");
        command.add("null");
        command.add("-");


        sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("configVideoMerge exeDamping  cmd:" + sb.toString());
        try {
            videoProcess = new ProcessBuilder(command).start();
            dealStream(videoProcess);
            videoProcess.waitFor();
        } catch (Exception e) {
            check = false;
            LogUtil.error(e, "configVideoMerge exeDamping error");
        }
        return check;
    }

    // 执行命令
    public static Boolean execCommand(List<String> command){
        try {
            Process videoProcess = new ProcessBuilder(command).start();
            dealStream(videoProcess);
            videoProcess.waitFor();
            return true;
        } catch (Exception e) {
            LogUtil.error(e, "execCommand error");
        }
        return false;
    }


    /**
     * 视频复制音频
     * @param	tempVideoPath	视频路径
     */
    public static boolean copyAudioBatch(String tempVideoPath, String tempImgPath) {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(tempVideoPath);//文件输入源
        command.add("-vn");
        command.add("-c:a");
        command.add("copy");
        command.add(tempImgPath);//文件输出路径，格式为 11.jpg
        /*
        // ffmpeg -i input.mp4 -vn -c:a copy output.aac
         */
        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("copyAudioBatch  cmd:" + sb.toString());
        return execCommand(command);
    }

}
