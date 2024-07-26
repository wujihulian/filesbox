package com.svnlan.home.utils;

import com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoResponseBody;
import com.svnlan.home.executor.PartMergeTaskExecutor;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.Partition;
import com.svnlan.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/21 10:42
 */
@Component
public class DingDownloadTool {
    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    ThreadPoolExecutor partMergeTask = new ThreadPoolExecutor(10, 30,
            100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4));
    // , new ThreadPoolExecutor.CallerRunsPolicy()

    // 线程过大弃
    public Boolean dingDownloadAndMergeFile(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String finalFilePath, String finalFolderPath
            , Long rangeSize, String fileName, String extension,String finalTopPath) {
        String bodyMsg = "，getFileDownloadInfoResponseBody=" + JsonUtils.beanToJson(getFileDownloadInfoResponseBody + "，finalFilePath=" + finalFilePath);
        int chunkSize = 100*1024*1024;
        String tempFolderPath = finalTopPath + "tmp/" ;
        // 105,906,176 + 2,097,152
        File folder = new File(tempFolderPath + fileName + "/");
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LOGGER.error("storageDentryCreate dingDownloadAndMergeFile 创建目录失败,makeFile  path:" + tempFolderPath+ fileName + "/");
                return false;
            }
        }

        Long chunksLong = (rangeSize + chunkSize - 1) / chunkSize ;
        int chunks = chunksLong.intValue();
        // 分片下载合并
        List<String> filePathList = new ArrayList<>();
        for (int i = 0; i < chunks; i++){
            String path = tempFolderPath  + fileName + "/"+ fileName + "_"+ i + ".part";
            String range = null;
            if (i == chunks-1){
                range =  "bytes=" + (i * chunkSize) + "-" ;
            }else {
                range =  "bytes=" + (i * chunkSize) + "-" + (( (i+ 1) * chunkSize) - 1);
            }
            // 下载文件
            LOGGER.info("PartDingDownloadTaskExecutor 当前正在下载的文件是：" + path + ":range=" + range);
            DingUtil.dingDownloadFile(getFileDownloadInfoResponseBody, path , range);
            filePathList.add(path);
        }

        File finalFile = new File(finalFilePath);
        //临时文件数组
        File[] tempFiles = new File[chunks];

        try (FileChannel finalFileChannel = new FileOutputStream(finalFile, true).getChannel();){
            StringBuilder tempFilePath;
            //将临时文件合并到最终文件
            LOGGER.info("ThreadMergeFile merge break1. time:"+ new Date());
            for (int k = 0; k < chunks; k++){
                tempFilePath = new StringBuilder();
                tempFilePath.append(tempFolderPath  + fileName + "/"+ fileName);
                tempFilePath.append("_");
                tempFilePath.append(k);
                tempFilePath.append(".part");
                tempFiles[k] = new File(tempFilePath.toString());
                try(FileChannel tempFileChannel = new FileInputStream(tempFiles[k]).getChannel();) {
                    tempFileChannel.transferTo(0, tempFileChannel.size(), finalFileChannel);
                }
            }
            LOGGER.info("dingDownloadAndMergeFile merge break2. time:"+ new Date());
            //删除临时文件
            LOGGER.info("dingDownloadAndMergeFile delete begin 隐藏  " );

            File delFile ;
            if (!CollectionUtils.isEmpty(filePathList)){
                LOGGER.info("dingDownloadAndMergeFile delete begin filePathList  " );
                for (String p : filePathList){
                    delFile = new File(p);
                    if (delFile.exists()){
                        delFile.delete();
                    }
                }
                LOGGER.info("dingDownloadAndMergeFile delete end filePathList  " );
            }
        }  catch (Exception e) {
            LOGGER.error("dingDownloadAndMergeFile 合并文件失败"+ bodyMsg ,e.getMessage());
            return false;
        }
        return true;
    }

    /** 2024-04-09  */
    public Boolean dingDownloadAndMergeFile_old(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String finalFilePath, String finalFolderPath
            , Long rangeSize, String fileName, String extension) {
        String bodyMsg = "，getFileDownloadInfoResponseBody=" + JsonUtils.beanToJson(getFileDownloadInfoResponseBody + "，finalFilePath=" + finalFilePath);
        int chunkSize = 100*1024*1024;
        // 105,906,176 + 2,097,152
        File folder = new File(finalFolderPath + fileName + "/");
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("storageDentryCreate dingDownloadAndMergeFile 创建目录失败,makeFile  path:" + finalFolderPath+ fileName + "/");
                return false;
            }
        }

        Long chunksLong = (rangeSize + chunkSize - 1) / chunkSize ;
        int chunks = chunksLong.intValue();
        // 分片下载合并
        List<String> filePathList = new ArrayList<>();
        for (int i = 0; i < chunks; i++){
            String path = finalFolderPath  + fileName + "/"+ fileName + "_"+ i + ".part";
            String range = null;
            if (i == chunks-1){
                range =  "bytes=" + (i * chunkSize) + "-" ;
            }else {
                range =  "bytes=" + (i * chunkSize) + "-" + (( (i+ 1) * chunkSize) - 1);
            }
            // 下载文件
            LogUtil.info("PartDingDownloadTaskExecutor 当前正在下载的文件是：" + path + ":range=" + range);
            DingUtil.dingDownloadFile(getFileDownloadInfoResponseBody, path , range);
            filePathList.add(path);
        }

        // 合并
        ArrayList<Future<Long>> futureMerges = new ArrayList<>();
        List<String> partTmpPath = new ArrayList<>();
        try {
            File pFile = null;
            int j = 0;
            for (List<String> subList : Partition.ofSize(filePathList, 100)) {
                String p = finalFolderPath  + fileName + "/"+ fileName + "_part"+ j + ".part";
                partTmpPath.add(p);
                j ++;
                futureMerges.add(partMergeTask.submit(new PartMergeTaskExecutor(p, subList)));
            }
            //这里主要用于监听各个文件写入线程是否执行结束
            int count = 0;
            while (count != futureMerges.size()) {
                for (Future<Long> future : futureMerges) {
                    if (future.isDone()) {
                        String p = finalFolderPath  + fileName + "/"+ fileName +  "_part" + count + ".part";
                        LogUtil.info("dingDownloadAndMergeFile 合并文件名:" + p + "; 总大小：" + future.get());
                        count++;
                    }
                }
                Thread.sleep(1);
            }
            for (String p : partTmpPath){
                pFile = new File(p);
                if (pFile.exists()){
                    LogUtil.info("dingDownloadAndMergeFile 文件名:" + p + " 存在" );
                }else {
                    LogUtil.info("dingDownloadAndMergeFile 文件名:" + p + " 不存在" );
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "dingDownloadAndMergeFile 合并失败"+ bodyMsg);
        }
        File finalFile = new File(finalFilePath);
        //临时文件数组
        File[] tempFiles = new File[chunks];

        try (FileChannel finalFileChannel = new FileOutputStream(finalFile, true).getChannel();){
            //将临时文件合并到最终文件
            LogUtil.info("ThreadMergeFile merge break1. time:"+ new Date());
            int k = 0;
            for (String path : partTmpPath){
                tempFiles[k] = new File(path);
                try(FileChannel tempFileChannel = new FileInputStream(tempFiles[k]).getChannel();) {
                    tempFileChannel.transferTo(0, tempFileChannel.size(), finalFileChannel);
                }
                k++;
            }
            LogUtil.info("dingDownloadAndMergeFile merge break2. time:"+ new Date());
            //删除临时文件
            LogUtil.info("dingDownloadAndMergeFile delete begin 隐藏  " );

            File delFile ;
            if (!CollectionUtils.isEmpty(filePathList)){
                LogUtil.info("dingDownloadAndMergeFile delete begin filePathList  " );
                for (String p : filePathList){
                    delFile = new File(p);
                    if (delFile.exists()){
                        delFile.delete();
                    }
                }
                LogUtil.info("dingDownloadAndMergeFile delete end filePathList  " );
            }
            if (!ObjectUtils.isEmpty(partTmpPath)){
                LogUtil.info("dingDownloadAndMergeFile delete begin partTmpPath  " );
                for (String f : partTmpPath){
                    delFile = new File(f);
                    if (delFile.exists()){
                        delFile.delete();
                    }
                }
                LogUtil.info("dingDownloadAndMergeFile delete end partTmpPath  " );
            }
        }  catch (Exception e) {
            LogUtil.error(e, "dingDownloadAndMergeFile 合并文件失败"+ bodyMsg );
            return false;
        }
        return true;
    }

    // 线程过大弃
    /*public Boolean dingDownloadAndMergeFile_Old(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String finalFilePath, String finalFolderPath
            , Long rangeSize, String fileName, String extension) {
        String bodyMsg = "，getFileDownloadInfoResponseBody=" + JsonUtils.beanToJson(getFileDownloadInfoResponseBody + "，finalFilePath=" + finalFilePath);
        int chunkSize = 100*1024*1024;
        // 105,906,176 + 2,097,152
        File folder = new File(finalFolderPath + fileName + "/");
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("storageDentryCreate dingDownloadAndMergeFile 创建目录失败,makeFile  path:" + finalFolderPath+ fileName + "/");
                return false;
            }
        }

        Long chunksLong = (rangeSize + chunkSize - 1) / chunkSize ;
        int chunks = chunksLong.intValue();
        // 分片下载合并
        ArrayList<Future<Long>> futures = new ArrayList<>();
        List<String> filePathList = new ArrayList<>();
        for (int i = 0; i < chunks; i++){
            String path = finalFolderPath  + fileName + "/"+ fileName + "_"+ i + ".part";
            String range = null;
            if (i == chunks-1){
                range =  "bytes=" + (i * chunkSize) + "-" ;
            }else {
                range =  "bytes=" + (i * chunkSize) + "-" + (( (i+ 1) * chunkSize) - 1);
            }
            // 下载文件
            futures.add(partMergeTask.submit(new PartDingDownloadTaskExecutor(getFileDownloadInfoResponseBody, path , range)));
            filePathList.add(path);
        }
        //这里主要用于监听各个文件写入线程是否执行结束
        try {
            int count = 0;
            while (count != futures.size()) {
                for (Future<Long> future : futures) {
                    if (future.isDone()) {
                        String path = finalFolderPath  + fileName + "/"+ fileName + "_"+ count + ".part";
                        LogUtil.info("dingDownloadAndMergeFile 下载文件名:" + path + "; 总大小：" + future.get());
                        count++;
                    }
                }
                Thread.sleep(1);
            }
        }catch (Exception e){
            LogUtil.error(e, "dingDownloadAndMergeFile 下载失败 "+ bodyMsg);
            return false;
        }

        // 合并
        ArrayList<Future<Long>> futureMerges = new ArrayList<>();
        List<String> partTmpPath = new ArrayList<>();
        try {
            File pFile = null;
            int j = 0;
            for (List<String> subList : Partition.ofSize(filePathList, 100)) {
                String p = finalFolderPath  + fileName + "/"+ fileName + "_part"+ j + ".part";
                partTmpPath.add(p);
                j ++;
                futureMerges.add(partMergeTask.submit(new PartMergeTaskExecutor(p, subList)));
            }
            //这里主要用于监听各个文件写入线程是否执行结束
            int count = 0;
            while (count != futureMerges.size()) {
                for (Future<Long> future : futureMerges) {
                    if (future.isDone()) {
                        String p = finalFolderPath  + fileName + "/"+ fileName +  "_part" + count + ".part";
                        LogUtil.info("dingDownloadAndMergeFile 合并文件名:" + p + "; 总大小：" + future.get());
                        count++;
                    }
                }
                Thread.sleep(1);
            }
            for (String p : partTmpPath){
                pFile = new File(p);
                if (pFile.exists()){
                    LogUtil.info("dingDownloadAndMergeFile 文件名:" + p + " 存在" );
                }else {
                    LogUtil.info("dingDownloadAndMergeFile 文件名:" + p + " 不存在" );
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "dingDownloadAndMergeFile 合并失败"+ bodyMsg);
        }
        File finalFile = new File(finalFilePath);
        //临时文件数组
        File[] tempFiles = new File[chunks];

        try (FileChannel finalFileChannel = new FileOutputStream(finalFile, true).getChannel();){
            //将临时文件合并到最终文件
            LogUtil.info("ThreadMergeFile merge break1. time:"+ new Date());
            int k = 0;
            for (String path : partTmpPath){
                tempFiles[k] = new File(path);
                try(FileChannel tempFileChannel = new FileInputStream(tempFiles[k]).getChannel();) {
                    tempFileChannel.transferTo(0, tempFileChannel.size(), finalFileChannel);
                }
                k++;
            }
            LogUtil.info("dingDownloadAndMergeFile merge break2. time:"+ new Date());
            //删除临时文件
            LogUtil.info("dingDownloadAndMergeFile delete begin 隐藏  " );

            File delFile ;
            if (!CollectionUtils.isEmpty(filePathList)){
                LogUtil.info("dingDownloadAndMergeFile delete begin filePathList  " );
                for (String p : filePathList){
                    delFile = new File(p);
                    if (delFile.exists()){
                        delFile.delete();
                    }
                }
                LogUtil.info("dingDownloadAndMergeFile delete end filePathList  " );
            }
            if (!ObjectUtils.isEmpty(partTmpPath)){
                LogUtil.info("dingDownloadAndMergeFile delete begin partTmpPath  " );
                for (String f : partTmpPath){
                    delFile = new File(f);
                    if (delFile.exists()){
                        delFile.delete();
                    }
                }
                LogUtil.info("dingDownloadAndMergeFile delete end partTmpPath  " );
            }
        }  catch (Exception e) {
            LogUtil.error(e, "dingDownloadAndMergeFile 合并文件失败"+ bodyMsg );
            return false;
        }
        return true;
    }*/
}
