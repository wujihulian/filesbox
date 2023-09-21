package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:17
 */
@Data
public class Comment {
    /**评论id */
    private Long commentID;
    /** 该评论上级ID */
    private Long pid;
    /** 评论用户id */
    private Long userID;
    /** 评论对象类型1分享2文件3文章4...... */
    private Integer targetType;
    /** 评论对象id */
    private Long targetID;
    /** 评论内容 */
    private String content;
    /** 点赞统计 */
    private Integer praiseCount;
    /** 评论统计 */
    private Integer commentCount;
    /** 状态 1正常 2异常 3其他 */
    private Integer status;
    private Long modifyTime;
    private Long createTime;

}
