package com.svnlan.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.svnlan.annotation.CreateGroup;
import com.svnlan.annotation.UpdateGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @TableName notice
 */

@Data
public class NoticeDTO {
    /**
     * 通知id
     */
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;

    /**
     * 标题
     */
    @NotEmpty(message = "标题不能为空")
    private String title;
    /**
     * 通知的内容
     */
    @NotEmpty(message = "通知内容不能为空", groups = CreateGroup.class)
    private String content;
    /**
     * 0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。
     */
    private Integer level;

    /**
     * 是否启用，0未启用，1启用
     */
    private Integer enable;

    /**
     * 消息类型，1通知2消息3私信
     */
    private Integer noticeType;
    /**
     * 用户id集合
     */
    private List<Long> userIds;
    /**
     * 部门id集合
     */
    private List<Long> deptIds;
    /**
     * 角色id集合
     */
    private List<Long> roleIds;
    /**
     * 推送方式 1 立即推送 2 计划推送
     */
    private Integer sendType;
    /**
     * 计划推送时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

}