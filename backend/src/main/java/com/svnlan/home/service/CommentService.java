package com.svnlan.home.service;

import com.svnlan.home.domain.Comment;
import com.svnlan.home.dto.CommentDto;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.PageResult;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:19
 */
public interface CommentService {

    Comment saveComment(CommentDto dto, LoginUser loginUser);
    void delComment(CommentDto dto, LoginUser loginUser);
    PageResult getCommentList(CommentDto dto, LoginUser loginUser);
}
