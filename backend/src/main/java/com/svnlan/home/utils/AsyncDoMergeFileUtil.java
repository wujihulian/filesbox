package com.svnlan.home.utils;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.UploadStateDTO;
import com.svnlan.home.service.ConvertFileService;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/27 15:37
 */
@Component
public class AsyncDoMergeFileUtil {

    @Resource
    ConvertFileService convertFileService;
    /**
       * @Description: 异步合并文件
       * @params:  [uploadStateDTO, commonSource]
       * @Return:  void
       * @Modified:
       */
    @Async(value = "asyncTaskExecutor")
    public void asyncDoMergeFileUtil(UploadStateDTO uploadStateDTO, CommonSource commonSource, CommonSourceVO commonSourceVO, String busType){
        String checksum = uploadStateDTO.getChecksum();
        String tempPath = uploadStateDTO.getTempPath();
        Integer chunks = uploadStateDTO.getChunks();
        String finalFilePath = commonSource.getPath();
        File finalFile = new File(finalFilePath);
        //临时文件数组
        File[] tempFiles = new File[chunks];
        try (FileChannel finalFileChannel = new FileOutputStream(finalFile, true).getChannel();) {
            StringBuilder tempFilePath;
            //将临时文件合并到最终文件
            LogUtil.info("asyncDoMergeFileUtil merge break1. time:" + new Date());
            for (int i = 0; i < chunks; i++) {
                //拼接临时文件路径
                tempFilePath = new StringBuilder();
                tempFilePath.append(tempPath);
                tempFilePath.append(checksum);
                tempFilePath.append("_");
                tempFilePath.append(i);
                tempFilePath.append(".part");
                //通过FileChannel
                tempFiles[i] = new File(tempFilePath.toString());
                try (FileChannel tempFileChannel = new FileInputStream(tempFiles[i]).getChannel();) {
                    tempFileChannel.transferTo(0, tempFileChannel.size(), finalFileChannel);
                }
            }
            LogUtil.info("asyncDoMergeFileUtil merge break2. time:" + new Date());

            //删除临时文件
            for (int i = 0; i < chunks; i++) {
                tempFiles[i].delete();
            }
            try {
                new File(tempPath + "done.txt").delete();
            } catch (Exception e) {
                LogUtil.error(e, "asyncDoMergeFileUtil 删除done文件失败");
            }
        } catch (Exception e) {
            LogUtil.error(e, "asyncDoMergeFileUtil 合并文件失败" + JsonUtils.beanToJson(commonSource));
            return ;
        }

        convertFileService.tryToConvertFile(commonSourceVO, commonSource, busType);
    }
}
