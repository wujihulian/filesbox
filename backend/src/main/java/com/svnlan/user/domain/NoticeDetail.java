package com.svnlan.user.domain;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.dto.NoticeDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * @TableName notice_detail
 */
@Data
@NoArgsConstructor
//@TableName("notice_detail")
public class NoticeDetail implements Serializable {
    /**
     * 主键id
     */
//    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 通知id
     */
    private Long noticeId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否为所有用户 1 是 0 否
     */
    private Integer isAll;

    /**
     * 目标用户id
     */
    private String targetUserIds;
    /**
     * 目标部门id
     */
    private String targetDeptIds;
    /**
     * 目标角色id
     */
    private String targetRoleIds;
    /**
     * 详情表id 主详情表为空， 后续表会指向主表， 存储 targetUserIds
     */
    private Long noticeDetailId;
    /**
     * 创建时间
     */
//    @TableField(fill = FieldFill.INSERT, update = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
//    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime modifyTime;

    /**
     * 逻辑删除 0 未删除 1 已删除
     */
    private Integer dr;

    public NoticeDetail(Long noticeId) {
        this.noticeId = noticeId;
        this.createTime = LocalDateTime.now();
    }

    private static final long serialVersionUID = 1L;

    public Integer populateData(NoticeDTO dto, boolean isCreate) {
        if (CollectionUtils.isEmpty(dto.getUserIds())
                && CollectionUtils.isEmpty(dto.getDeptIds())
                && CollectionUtils.isEmpty(dto.getRoleIds())) {
            if (!isCreate) {
                // 更新操作时 没有指定任何用户，部门和角色的话
                this.targetUserIds = "[]";
                this.targetDeptIds = "[]";
                this.targetRoleIds = "[]";
            }
            isAll = 1;
        } else {
            if (isCreate) {
                // 创建
                if (!CollectionUtils.isEmpty(dto.getUserIds())) {
                    // 如果集合超过了500，就需要拆分到另一条数据上
                    // 涉及到 next_id
                    this.targetUserIds = JSONObject.toJSONString(dto.getUserIds());
                }
                if (!CollectionUtils.isEmpty(dto.getDeptIds())) {
                    this.targetDeptIds = JSONObject.toJSONString(dto.getDeptIds());
                }
                if (!CollectionUtils.isEmpty(dto.getRoleIds())) {
                    this.targetRoleIds = JSONObject.toJSONString(dto.getRoleIds());
                }
            } else {
                // 更新
                this.targetUserIds = JSONObject.toJSONString(Optional.ofNullable(dto.getUserIds()).orElseGet(Collections::emptyList));
                this.targetDeptIds = JSONObject.toJSONString(Optional.ofNullable(dto.getDeptIds()).orElseGet(Collections::emptyList));
                this.targetRoleIds = JSONObject.toJSONString(Optional.ofNullable(dto.getRoleIds()).orElseGet(Collections::emptyList));
            }

            isAll = 0;
        }
        Optional.ofNullable(dto.getContent()).ifPresent(this::setContent);
        return isAll;
    }
}