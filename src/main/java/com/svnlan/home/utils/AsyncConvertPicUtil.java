package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.manage.vo.ConvertToJPGDTO;
import com.svnlan.manage.vo.ConvertToPDFMsgDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/30 13:58
 */
@Component
public class AsyncConvertPicUtil {
    @Resource
    ConvertPicUtil convertPicUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Async(value = "asyncTaskExecutor")
    public void asyncConvertToJPG(ConvertToPDFMsgDTO msgDTO, CommonSource commonSource) {
        String prefix = "@将PDF、PPT、WORD、EXCEL原文件转成图片@convertToJPGKafka006 >>> ";
        StringBuilder msgBuilder = new StringBuilder();
        String paramTip = "";
        String taskId = "";
        String sendTime = "";
        Long sourceID = 0L;
        try {
            sendTime = msgDTO.getSendTime();
            sourceID = msgDTO.getSourceID();

            taskId = msgDTO.getTaskId();
            msgBuilder.append("asyncConvertToJPG:")
                    .append(sourceID).append("-").append(sendTime)
                    .append("，").append(taskId).append("；");
            paramTip = "->消息记录：" + msgBuilder.toString();
            LogUtil.info(prefix + "消费开始，" + paramTip);
        }
        catch (Exception e) {
            LogUtil.error(e, prefix + "  消费失败！原因：消息格式有误");
            //
            return;
        }

        String concurrentKey = String.format(GlobalConfig.concurrentConvertToJPGKey, commonSource.getSourceID());
        //
        Boolean setSuccess = this.stringRedisTemplate.opsForValue().setIfAbsent(concurrentKey, "1", 20, TimeUnit.MINUTES);
        if(!setSuccess) {
            LogUtil.error( prefix + "  有转码任务进行中！" + paramTip);
            return;
        }
        String cacheKey = String.format(GlobalConfig.infoConvertToJPGKey, commonSource.getSourceID());

        try {
            ConvertToJPGDTO convertToJPGDTO = new ConvertToJPGDTO("0", null);
            //进行中
            this.stringRedisTemplate.opsForValue().set(cacheKey, JsonUtils.beanToJson(convertToJPGDTO), 20, TimeUnit.MINUTES);


            Result result = this.convertPicUtil.convertToPDF(prefix, commonSource, msgDTO);
            String code = result.getCode();
            String regionFilePath = commonSource.getPath();
            String pdfPath = commonSource.getPdfPath();
            String lastImagePath = commonSource.getH264Path();
            String tip = String.format("文件ID：%d，原文件路径：%s，PDF文件路径：%s，最后1张图片路径：%s", sourceID,
                    regionFilePath, pdfPath, lastImagePath);
            String status = "1"; //状态：0-进行中，1-成功，2-失败
            if("200".equals(code)) {
                LogUtil.info(prefix + "消费成功。" + tip);
            } else if("AlreadySuccess".equals(code)) {
                LogUtil.info(prefix + "消费成功。原先已转过，" + tip);
            } else {
                LogUtil.info(prefix + "消费失败。转换失败，" + tip);
            }

            convertToJPGDTO = new ConvertToJPGDTO(status, lastImagePath);
            //结果
            this.stringRedisTemplate.opsForValue().set(cacheKey, JsonUtils.beanToJson(convertToJPGDTO), 2, TimeUnit.MINUTES);

        } catch (Exception e) {
            LogUtil.error(e, prefix + "  消费失败！" + paramTip);
        }

        //删除并发锁
        this.stringRedisTemplate.delete(concurrentKey);
    }
}
