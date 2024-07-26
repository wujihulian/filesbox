package com.svnlan.user.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.svnlan.user.domain.ShareReport;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * 分享资源举报相关
 *
 * @TableName share_report
 */
@NoArgsConstructor
@Data
public class ShareReportVo implements Serializable {
    /**
     * 自增id
     */
    @Excel(name = "ID")
    private Long id;

    /**
     * 分享id
     */
    private Long shareID;

    /**
     * 分享标题
     */
    @Excel(name = "举报分享")
    private String title;

    /**
     * 举报资源id
     */
    private Long sourceID;

    /**
     * 举报文件id,文件夹则该处为0
     */
    private Long fileID;

    /**
     * 举报用户id
     */
    @Excel(name = "举报者ID")
    private Long userID;
    /**
     * 举报者姓名
     */
    @Excel(name = "举报者名称")
    private String reportUserName;
    /**
     * 举报类型 (1-侵权,2-色情,3-暴力,4-政治,5-其他)
     */
    @Excel(name = "举报类型" ,replace = {"侵权_1","色情_2","暴力_3","政治_4","其他_5"})
    private Integer type;
    /**
     * 举报原因
     */
    @Excel(name = "举报原因")
    private String reason;
    /**
     * 处理状态(0-未处理,1-已处理,2-禁止分享)
     */
    private Integer status;

    @Excel(name = "处理结果")
    private String result;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @Excel(name = "举报时间")
    private String createTimeStr;
    /**
     * 最后修改时间
     */
    private Long modifyTime;

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public ShareReportVo(ShareReport shareReport, String reportUserName) {
        this.reportUserName = reportUserName;
        this.id = shareReport.getId();
        this.userID = shareReport.getUserId();
        this.title = shareReport.getTitle();
        this.reason = Optional.ofNullable(shareReport.getReason()).orElse("-");
        this.type = shareReport.getReportType();
        this.createTime = shareReport.getCreateTime();
        this.createTimeStr= createTime.format(dateTimeFormatter);
        // 处理结果 status[处理状态(0-未处理,1-已处理)] shareStatus[share状态 1 正常 2 取消分享 3 禁止分享]
        Integer status = shareReport.getStatus();
        Integer shareStatus = shareReport.getShareStatus();
        if (Objects.equals(status, 0)) {
            this.result = "未处理";
        } else {
            if (Objects.equals(shareStatus, 1)) {
                this.result = "已处理(关闭举报)";
            } else if (Objects.equals(shareStatus, 2)) {
                this.result = "已处理(取消分享)";
            } else if (Objects.equals(shareStatus, 3)) {
                this.result = "已处理(禁止分享)";
            }
        }


    }
}