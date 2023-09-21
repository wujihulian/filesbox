package com.svnlan.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.NoticeDTO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @TableName notice
 */
@Data
@TableName("notice")
public class Notice implements Serializable {
    /**
     * 通知id
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。
     */
    private Integer level;

    /**
     * 状态，0暂存，1已发送，2已删除
     */
    private Integer status;

    /**
     * 是否启用，0未启用，1启用
     */
    private Integer enable;

    /**
     * 推送方式 1 立即推送 2 计划推送
     */
    private Integer sendType;

    /**
     * 通知发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 排序 越大越靠前
     */
    private Long sort;

    /**
     * 通知发送者id
     */
    private Long senderId;

    /**
     * 发送通知的IP地址，json(222.22.22.22,杭州)
     */
    private String senderIp;

    /**
     * 消息类型，1通知2消息3私信
     */
    private Integer noticeType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, update = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime modifyTime;

    public Notice() {
    }

    public Notice(Long id) {
        this.id = id;
        this.modifyTime = LocalDateTime.now();
    }

    public void populateData(NoticeDTO dto, LoginUser loginUser) {
        if (StringUtils.hasText(dto.getTitle())) {
            this.title = dto.getTitle();
        }
        Optional.ofNullable(dto.getLevel()).ifPresent(this::setLevel);
        Optional.ofNullable(dto.getEnable()).ifPresent(this::setEnable);
        Optional.ofNullable(dto.getSendType()).ifPresent(this::setSendType);
        if (Objects.equals(dto.getSendType(), 2)) {
            // 表示计划推送，需要有推送时间
            this.setSendTime(
                    Optional.ofNullable(dto.getSendTime())
                            .orElseThrow(() -> new IllegalArgumentException("计划推送需要具体时间")));
        } else {
            // 即时发送
            this.setSendTime(LocalDateTime.now());
        }
        this.senderId = loginUser.getUserID();
        if (StringUtils.hasText(loginUser.getIp())) {
            this.senderIp = loginUser.getIp();
        }

        Optional.ofNullable(dto.getNoticeType()).ifPresent(this::setNoticeType);
    }
}