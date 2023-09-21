package com.svnlan.home.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:17
 */
@Data
public class CommentDto extends PageQuery {
    /** 评论对象类型1分享2文件3文章4...... */
    private Integer targetType;
    /** 评论对象id */
    private Long targetID;
    /** 评论内容 */
    private String content;
    private Long idFrom;
    private Long pid;
    private Long commentID;
}
