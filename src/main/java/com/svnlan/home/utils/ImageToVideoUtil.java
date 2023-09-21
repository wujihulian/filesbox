package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.VideoCutDto;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.HttpUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/7 11:32
 */

@Component
public class ImageToVideoUtil {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    IoSourceDao ioSourceDao;

    @Async(value = "asyncTaskExecutor")
    public void execImageToVideo(String outPath, VideoCommonDto checkFileDTO, String redisKey, String fileType, String serverUrl, LoginUser loginUser, String redisProgressKey) {

        stringRedisTemplate.opsForValue().set(redisProgressKey, "10", 60, TimeUnit.SECONDS);
        boolean check = imageToVideo(outPath, checkFileDTO, redisProgressKey);
        if (!check){
            stringRedisTemplate.opsForValue().set(redisKey, "2", 1, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(redisProgressKey, "100", 60, TimeUnit.SECONDS);
            LogUtil.error("图片合成执行失败");
            return;
        }
        CommonSource parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
        // 查询文件夹下的文件、解析是否要重命名
        List<String> sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
        Integer targetType = parentSource.getTargetType();
        String fileName = checkFileDTO.getName();
        CommonSource fileSource = new CommonSource();
        fileSource.setName(fileOptionTool.checkRepeatName(fileName, fileName, fileType, sourceNameList, 1));
        fileSource.setParentID(parentSource.getSourceID());
        fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
        fileSource.setTargetType(targetType);
        fileSource.setFileType(fileType);
        fileSource.setPath(outPath);
        fileSource.setDomain(serverUrl);
        fileSource.setResolution(checkFileDTO.getResolution().replace("x","*"));

        Long size = 0L;
        //最终文件
        File finalFile = new File(outPath);

        try {
            size = finalFile.length();
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), " imagesToVideo 获取文件大小 失败 commonSource=" + JsonUtils.beanToJson(fileSource));
        }
        fileSource.setSize(size);
        fileSource.setHashMd5("");
        fileSource.setNeedHashMd5(1);
        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);

        stringRedisTemplate.opsForValue().set(redisKey, "1", 1, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(redisProgressKey, "100", 60, TimeUnit.SECONDS);

        /** 图片转视频-转码 */
        ConvertDTO convertDTO = new ConvertDTO();
        convertDTO.setBusId(fileSource.getSourceID());
        convertDTO.setBusType("cloud");
        convertDTO.setOtherType("imagesToVideo");
        fileSource.setDomain(serverUrl);
        convertDTO.setDomain(serverUrl);

        convertUtil.doConvertMain(convertDTO, fileSource);

    }
    public boolean imageToVideoOne(String outPath, VideoCommonDto videoCommonDto, String redisProgressKey, String setdar, int imgW, int imgH) {
        // ffmpeg -loop 1 -t 8 -i images/001.jpg -stream_loop -1 -i 002225.mp3 -acodec aac -shortest -c:v libx264 -profile:v main -pix_fmt yuv420p -preset fast -y 98200.mp4
        List<String> command = new ArrayList<>();
        String outPathTemp = outPath + "_temp.mp4";
        VideoCutDto dto = videoCommonDto.getCutList().get(0);
        double length = dto.getLength() + dto.getDuration();
        command.add("ffmpeg");
        command.add("-loop");
        command.add("1");
        command.add("-t");
        command.add("" + length);
        command.add("-i");
        String videoPathOrg  = getOrgPath(dto.getpUrl(), dto.getFileName());
        command.add(videoPathOrg);

        /*

ffmpeg -loop 1 -t 8 -i img/01.png -stream_loop -1 -i 002225.mp3 -filter_complex
 "[0]scale=min(iw*720/ih\,1080):min(720\,ih*1080/iw),pad=1080:720:(1080-iw)/2:(720-ih)/2:#000000,setdar=1920/1080[v]"  -map "[v]" -map "1:a" -acodec aac -shortest -c:v libx264 -profile:v main  -pix_fmt yuv420p -preset fast -y be4267977fd431785b7ecd1691652970440_30074.mp4
         */

        command.add("-filter_complex");
        command.add("\"[0]scale=min(iw*"+imgH+"/ih\\,"+imgW+"):min("+imgH+"\\,ih*"+imgW+"/iw),pad="+imgW+":"+imgH+":("+imgW+"-iw)/2:("+imgH+"-ih)/2:"+dto.getBackground()+",setdar="+setdar+"[v]\"");
        command.add("-map");
        command.add("\"[v]\"");
        //command.add("-map");
        //command.add("\"1:a\"");
        command.add("-shortest");
        command.add("-c:v");
        command.add("libx264");
        command.add("-profile:v");
        command.add("main");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-preset");
        command.add("fast");
        command.add("-y");
        command.add(outPathTemp);//文件输出路径，格式为 11.jpg

        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("covPic  cmd:" + sb.toString());

        String shellPath = outPath+".sh";
        File f = new File(shellPath);
        try {
            if (f.exists()){
                f.delete();
            }
            f.createNewFile();
        }catch (Exception e){
            LogUtil.error(e, "imageToVideoOne创建shell文件失败！");
            return false;
        }

        List<String> cmd = cmdHB(outPathTemp, outPath, videoCommonDto, length);

        StringBuilder sb2 = new StringBuilder();
        for (String a : cmd){
            sb2.append(a);
            sb2.append(" ");
        }

        LogUtil.info("合成音频视频  cmd:" + sb2.toString());


        try {
            // 创建一个PrintWriter对象，用于写入shell脚本到文件中
            PrintWriter writer = new PrintWriter(new FileWriter(shellPath));

            // 编写shell脚本
            writer.println("#!/bin/sh");
            writer.println(sb.toString());
            writer.println(sb2.toString());

            // 关闭PrintWriter对象
            writer.close();

            // 输出提示信息，表示shell脚本写入成功
            LogUtil.info("imageToVideoOne Shell script written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 创建一个ProcessBuilder对象，指定shell脚本文件的路径和名称
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", shellPath);

            // 启动进程并执行shell脚本
            Process process = pb.start();
            dealStream(process);
            process.waitFor();

            // 输出提示信息，表示shell脚本执行成功
            LogUtil.info("imageToVideoOne Shell script executed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                File ff = new File(shellPath);
                File ffTmp = new File(outPathTemp);
                if (ff.exists()){
                    ff.delete();
                }
                if (ffTmp.exists()){
                    ffTmp.delete();
                }
            }catch (Exception e){

            }
        }

        stringRedisTemplate.opsForValue().set(redisProgressKey, "90", 60, TimeUnit.SECONDS);
        return true;
    }

    public List<String> cmdHB(String outPathTemp, String outPath, VideoCommonDto videoCommonDto, double length) {
        List<String> cmd = new ArrayList<>();
// ffmpeg -i /uploads/private/cloud/2023_8/10/750f98111826435d8199ccd6ccaee27b1691657681851_30074.mp4
// -stream_loop -1 -i /Cheerfulness.mp3 -t 5 -c:v copy -c:a aac -strict experimental /uploads/private/cloud/2023_8/10/750f98111826435d8199ccd6ccaee27b1691657681851_3007499999.mp4
        cmd.add("ffmpeg");
        cmd.add("-i");
        cmd.add(outPathTemp);
        cmd.add("-stream_loop");
        cmd.add("-1");
        cmd.add("-i");
        if (videoCommonDto.getAudio().startsWith("/mp3/")){
            cmd.add(videoCommonDto.getAudio().replaceFirst("/mp3/", "/"));
        }else {
            cmd.add(getOrgPath(videoCommonDto.getAudio()));
        }
        cmd.add("-t");
        cmd.add("" + length);
        cmd.add("-c:v");
        cmd.add("copy");
        cmd.add("-c:a");
        cmd.add("aac");
        cmd.add("-strict");
        cmd.add("experimental");

        cmd.add(outPath);//文件输出路径，格式为 11.jpg

        return cmd;
    }

    public boolean imageToVideo(String outPath, VideoCommonDto videoCommonDto, String redisProgressKey) {

        int size = videoCommonDto.getCutList().size();
        String outPathTemp = outPath + "_temp.mp4";
        double length = 0;
        int imgW = 0;
        int imgH = 0;
        String resolution = videoCommonDto.getResolution();
        if (ObjectUtils.isEmpty(resolution)){
            VideoCutDto cutDto =  videoCommonDto.getCutList().get(0);
            String filePath  = getOrgPath(cutDto.getpUrl(), cutDto.getFileName());
            File imageFile = new File(filePath);
            if (!imageFile.exists()){
                LogUtil.error("ImageToVideo 缩略图处理失败，原图不存在" + filePath);
                return false;
            }
            resolution = ImageUtil.getResolution(filePath);
        }
        List<Integer> whList = null;
        if (resolution.indexOf("x")>=0){
            whList = Arrays.asList(resolution.split("x")).stream().map(Integer::valueOf).collect(Collectors.toList());
        }else {
            whList = Arrays.asList(resolution.split("\\*")).stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        imgW = whList.get(0);
        imgH = whList.get(1);
        if (imgW <= 0){
            imgW = 1280;
        }
        if (imgH <= 0){
            imgH = 720;
        }
        if(imgW%2==1){
            imgW = imgW-1;
        }
        if(imgH%2==1){
            imgH = imgH-1;
        }
        String setdar = "1/1";
        if (imgH != imgW){
            setdar = imgW+"/"+imgH;
        }

        // ffmpeg -f image2 -i %d.jpeg output.mp4
        if (videoCommonDto.getCutList().size() == 1){
            return imageToVideoOne(outPath, videoCommonDto, redisProgressKey, setdar, imgW, imgH );
        }
        /*
            ffmpeg \
            -loop 1 -t 2 -i 1.jpg \
            -loop 1 -t 2 -i 2.jpg \
            -loop 1 -t 2 -i 3.jpg \
            -loop 1 -t 2 -i 4.jpg \
            -loop 1 -t 2 -i 5.jpg \
            -stream_loop -1 -i 1.mp3 -acodec aac \
            -filter_complex "[0:v]xfade=transition=circlecrop:duration=1:offset=1[v0]; [1:v]xfade=transition=circlecrop:duration=1:offset=1[v1]; [2:v]xfade=transition=circlecrop:duration=1:offset=1[v2]; [3:v]xfade=transition=circlecrop:duration=1:offset=1[v3]; [v0][v1][v2][v3]concat=n=4:v=1:a=0,format=yuv420p[v]" -map "[v]" \
            -map "5:a" \
            -shortest \
            -c:v libx264 \
            -y circlecrop.mp4
            注释：
            -stream_loop
            输入流循环的次数，0 表示无循环，-1 表示无限循环，即音乐循环播放。
            -acodec aac
            设置音频编解码为 acc 模式
            -map “[v]”
            将合成的视频输入流 v 指定为输出文件的源
            -map “5:a”
            将第6个文件作为视频音频文件
            -shortest
            最短的输入流结束时，完成编码。
            -c:v libx264
            输出视频编码格式
            -pix_fmt yuv420p
            设置像素格式为 yuv420p
            xfade transition 转场 duration 特效持续时间 offset =(loop-duration)

            ffmpeg -loop 1 -t 4 -i images/001.jpg -loop 1 -t 4 -i images/002.jpg -loop 1 -t 3 -i images/003.jpg   -stream_loop -1 -i 002225.mp3 -acodec aac
             -filter_complex "[0]scale=min(iw*720/ih\,400):min(720\,ih*400/iw),pad=400:720:(400-iw)/2:(720-ih)/2:blue[p0];[1]scale=min(iw*720/ih\,400):min(720\,ih*400/iw),pad=400:720:(400-iw)/2:(720-ih)/2:blue[p1];[2]scale=min(iw*720/ih\,400):min(720\,ih*400/iw),pad=400:720:(400-iw)/2:(720-ih)/2:blue[p2];[p0]split[v_sp_0_0][v_sp_0_1];[v_sp_0_0]trim=0:3[v_tr_0_0];[v_sp_0_1]trim=3:4[v_tr_0_1];[v_tr_0_1]setpts=PTS-STARTPTS[v_st_0];[p1]split[v_sp_1_0][v_sp_1_1];[v_sp_1_0]trim=0:3[v_tr_1_0];[v_sp_1_1]trim=3:4[v_tr_1_1];[v_tr_1_1]setpts=PTS-STARTPTS[v_st_1];[v_st_0][v_tr_1_0]xfade=transition=circlecrop:duration=1[v0];[v_st_1][p2]xfade=transition=circlecrop:duration=1[v1];[v_tr_0_0][v0][v1]concat=n=3[v]" -map "[v]" -map "3:a" -t 8 -shortest -c:v libx264 -profile:v main -pix_fmt yuv420p -preset fast -y out.mp4
         */
        String shellPath = outPath+".sh";
        File f = new File(shellPath);
        try {
            if (f.exists()){
                f.delete();
            }
            f.createNewFile();
        }catch (Exception e){
            LogUtil.error(e, "创建shell文件失败！");
            return false;
        }



        List<VideoCutDto> cutList = videoCommonDto.getCutList();
        List<String> command = new ArrayList<>();
        StringBuilder scaleConmplex = new StringBuilder();
        command.add("ffmpeg");
        int k = 0;
        for (VideoCutDto dto : cutList){
            scaleConmplex.append("["+k+"]scale=min(iw*"+imgH+"/ih\\,"+imgW+"):min("+imgH+"\\,ih*"+imgW+"/iw),pad="+imgW+":"+imgH+":("+imgW+"-iw)/2:("+imgH+"-ih)/2:"+dto.getBackground()+",setdar="+setdar+"[p"+k+"];");
            command.add("-loop");
            command.add("1");
            command.add("-t");
            command.add("" + (dto.getLength() + dto.getDuration() ));
            if (k == size-1){
                length = length + (dto.getLength());
            }else{
                length = length + (dto.getLength() + dto.getDuration() );
            }

            command.add("-i");
            String videoPathOrg  = getOrgPath(dto.getpUrl(), dto.getFileName());
            command.add(videoPathOrg);
            k++;
        }
       /* command.add("-stream_loop");
        command.add("-1");
        command.add("-i");
        if (videoCommonDto.getAudio().startsWith("/mp3/")){
            command.add(videoCommonDto.getAudio().replaceFirst("/mp3/", "/"));
        }else {
            command.add(getOrgPath(videoCommonDto.getAudio()));
        }
        command.add("-acodec");
        command.add("aac");*/
        command.add("-filter_complex");
        StringBuilder filterComplex = new StringBuilder();
        StringBuilder vComplex = new StringBuilder();
        StringBuilder xfadeComplex = new StringBuilder();
        int i = 0;

        for (VideoCutDto dto : cutList){
            if (ObjectUtils.isEmpty(dto.getTransition())){
                dto.setTransition("rectcrop");
            }
            Double totalLength = (dto.getLength() + dto.getDuration() );
            if (i < size-1){
                if (i==0){
                    filterComplex.append(scaleConmplex.toString());
                }else{
                    vComplex.append("[v"+(i-1)+"]");
                    xfadeComplex.append("[v_st_"+(i-1)+"][v_tr_"+(i)+"_0]xfade=transition="+ dto.getTransition() + ":duration="+dto.getLength()+"[v"+(i-1)+"];");
                }
                filterComplex.append("[p"+i+"]split[v_sp_"+i+"_0][v_sp_"+i+"_1];");
                filterComplex.append("[v_sp_"+i+"_0]trim=0:"+dto.getLength()+"[v_tr_"+i+"_0];");
                filterComplex.append("[v_sp_"+i+"_1]trim="+dto.getLength()+":"+totalLength+"[v_tr_"+i+"_1];");
                filterComplex.append("[v_tr_"+i+"_1]setpts=PTS-STARTPTS[v_st_"+i+"];");
            }else {
                vComplex.append("[v"+(i-1)+"]");
                xfadeComplex.append("[v_st_"+(i-1)+"][p"+(i)+"]xfade=transition="+ dto.getTransition() + ":duration="+dto.getLength()+"[v"+(i-1)+"];");
                filterComplex.append(xfadeComplex.toString());
                filterComplex.append("[v_tr_0_0]"+vComplex.toString() + "concat=n="+size+"[v]");
            }
            i++;
        }
        command.add("\"" + filterComplex.toString() +"\"");
        command.add("-map");
        command.add("\"[v]\"");
        /*command.add("-map");
        command.add("\"" + size +":a\"");*/
        command.add("-shortest");
        command.add("-c:v");
        command.add("libx264");
        command.add("-profile:v");
        command.add("main");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-preset");
        command.add("fast");
        command.add("-y");
        command.add(outPathTemp);//文件输出路径，格式为 11.jpg

        StringBuilder sb = new StringBuilder();
        for (String a : command) {
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("ImageToVideo  cmd:" + sb.toString());

        List<String> cmd = cmdHB(outPathTemp, outPath, videoCommonDto, length);

        StringBuilder sb2 = new StringBuilder();
        for (String a : cmd){
            sb2.append(a);
            sb2.append(" ");
        }

        LogUtil.info("合成音频视频  cmd:" + sb2.toString());

        try {
            // 创建一个PrintWriter对象，用于写入shell脚本到文件中
            PrintWriter writer = new PrintWriter(new FileWriter(shellPath));


            // 编写shell脚本
            writer.println("#!/bin/sh");
            writer.println(sb.toString());
            writer.println(sb2.toString());

            // 关闭PrintWriter对象
            writer.close();

            // 输出提示信息，表示shell脚本写入成功
            LogUtil.info("Shell script written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 创建一个ProcessBuilder对象，指定shell脚本文件的路径和名称
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", shellPath);

            // 启动进程并执行shell脚本
            Process process = pb.start();
            dealStream(process);
            process.waitFor();
            // 输出提示信息，表示shell脚本执行成功
            LogUtil.info("Shell script executed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                File ff = new File(shellPath);
                if (ff.exists()){
                    ff.delete();
                }
                File ffTmp = new File(outPathTemp);
                if (ffTmp.exists()){
                    ffTmp.delete();
                }
            }catch (Exception e){

            }
        }



        stringRedisTemplate.opsForValue().set(redisProgressKey, "90", 5, TimeUnit.MINUTES);
        return true;
    }

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

    public static String getOrgPath(String url){
        if (url.indexOf(GlobalConfig.private_replace_key) < 0){
            return url;
        }
        return url.replace(GlobalConfig.private_replace_key, "/private/");
    }
    public static String getOrgPath(String url, String fileName){
        if (url.indexOf(GlobalConfig.private_replace_key) >= 0){
            return url.replace(GlobalConfig.private_replace_key, "/private/") + fileName;
        }else {
            return GlobalConfig.default_disk_path_pre + "/private" + url + fileName;
        }
    }
}
