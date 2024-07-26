package com.svnlan.manage.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user_common_info
 */
@EqualsAndHashCode(callSuper = false, of = {"userId", "infoId"})
//@TableName(value = "user_common_info")
@NoArgsConstructor
@Data
public class UserCommonInfo implements Serializable {
    /**
     *
     */
//    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 资讯id
     */
    private Long infoId;
    /**
     * 用户ip
     */
    private String ip;

    /**
     * 阅读数
     */
    private Integer viewCount;

    /**
     * 是否点赞 0 否 1 是
     */
    private Integer isLike;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;


    public UserCommonInfo(Long userId, Long infoId) {
        this.userId = userId;
        this.infoId = infoId;
    }

    public UserCommonInfo(String ip, Long infoId) {
        this.ip = ip;
        this.infoId = infoId;
    }

//    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}