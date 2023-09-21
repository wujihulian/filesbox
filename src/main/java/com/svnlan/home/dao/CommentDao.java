package com.svnlan.home.dao;

import com.svnlan.home.domain.Comment;
import com.svnlan.home.vo.CommentVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:20
 */
public interface CommentDao {

    int insert(Comment comment);
    int setCommentCount(Comment comment);
    int deleteComment(Long commentID);
    List<CommentVo> getCommentList(Map<String, Object> map);
    long getCountComment(Map<String, Object> map);
    Integer checkCommentExist(Long commentID);
    CommentVo getTargetIdByCommentId(Long commentID);
}
