package com.svnlan.user.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @TableName notice_user
 */
@Data
public class NoticeUser implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 通知id
     */
    private Long noticeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否已读 0 未读 1 已读
     */
    private Integer isRead;

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

    private static final long serialVersionUID = 1L;

    public NoticeUser(Long userId, LocalDateTime dateTime) {
        this(dateTime);
        this.userId = userId;
    }

    public NoticeUser(Long id, Long userId, LocalDateTime dateTime) {
        this(userId, dateTime);
        this.noticeId = id;
    }

    public NoticeUser(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            dateTime = LocalDateTime.now();
        }
        createTime = dateTime;
    }

    public NoticeUser() {
    }
}