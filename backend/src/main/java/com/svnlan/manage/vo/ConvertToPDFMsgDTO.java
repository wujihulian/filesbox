package com.svnlan.manage.vo;

import lombok.Data;

/**
 * @description: 将PPT原文件转成PDF
 */
@Data
public class ConvertToPDFMsgDTO {

    //任务ID
    private String taskId;
    //发送时间
    private String sendTime;

    //PPT文件ID
    private Long sourceID;

    private Boolean noImage;

    public ConvertToPDFMsgDTO() {
    }

    public ConvertToPDFMsgDTO(String taskId, String sendTime, Long sourceID) {
        this.taskId = taskId;
        this.sendTime = sendTime;
        this.sourceID = sourceID;
    }

}
