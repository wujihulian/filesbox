package com.svnlan.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.svnlan.annotation.CreateGroup;
import com.svnlan.utils.PageQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.svnlan.common.GlobalConfig.dateStandardFormatter;
import static com.svnlan.common.GlobalConfig.dateTimeStandardFormatter;

/**
 * 资源分享举报 查询参数
 *
 * @author lingxu 2023/04/06 14:03
 */
@Slf4j
@Data
public class ShareReportDTO extends PageQuery {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 分享id
     */
    @NotNull(message = "分享id不能为空", groups = CreateGroup.class)
    private Long shareId;

    /**
     * 分享标题
     */
    private String title;

    /**
     * 举报资源id
     */
    private Long sourceId;

    /**
     * 举报文件id,文件夹则该处为0
     */
    private Long fileId;

    /**
     * 举报用户id
     */
    private Long userId;

    /**
     * 举报类型 (1-侵权,2-色情,3-暴力,4-政治,5-其他)
     */
    @NotNull(message = "举报类型不能为空", groups = CreateGroup.class)
    private Integer reportType;
    /**
     * 理由
     */
    private String reason;

    /**
     * 处理状态(0-未处理,1-已处理,2-取消分享,3-禁止分享)
     */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timeFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timeTo;

    public void initTimeRange() {
        if (StringUtils.hasText(this.timeFrom)) {
            LocalDate localDate = LocalDate.parse(this.timeFrom, dateStandardFormatter);
            this.timeFrom = localDate.atTime(LocalTime.MIN).format(dateTimeStandardFormatter);
        }
        if (StringUtils.hasText(this.timeTo)) {
            LocalDate localDate = LocalDate.parse(this.timeTo, dateStandardFormatter);
            this.timeTo = localDate.atTime(LocalTime.MAX).format(dateTimeStandardFormatter);
        }
    }

}
