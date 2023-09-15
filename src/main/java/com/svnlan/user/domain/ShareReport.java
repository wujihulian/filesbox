package com.svnlan.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.dto.ShareReportDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @TableName share_report
 */
@TableName(value = "share_report")
@Data
public class ShareReport implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享id
     */
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
    private Integer reportType;

    /**
     * 举报原因（其他）描述
     */
    private String reason;
    /**
     * 处理状态(0-未处理,1-已处理)
     */
    private Integer status;
    /**
     * share状态 1 正常 2 取消分享 3 禁止分享
     */
    @TableField(exist = false)
    private Integer shareStatus;

    @TableField(exist = false)
    private String shareHash;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void populateData(ShareReportDTO dto, ShareVo shareVo, IOSource ioSource, Long userId) {
        this.userId = userId;
        this.sourceId = ioSource.getSourceID();
        this.shareId = dto.getShareId();
        this.reportType = dto.getReportType();
        this.reason = dto.getReason();
        this.title = shareVo.getTitle();
        if (Objects.equals(ioSource.getIsFolder(), 1)) {
            this.fileId = 0L;
        } else {
            this.fileId = ioSource.getFileID();
        }
        this.status = 0;
        createTime = LocalDateTime.now();
    }
}