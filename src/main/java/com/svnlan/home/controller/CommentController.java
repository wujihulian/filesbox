package com.svnlan.home.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.Comment;
import com.svnlan.home.dto.CommentDto;
import com.svnlan.home.service.CommentService;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:19
 */
@RestController
public class CommentController {

    @Resource
    CommentService commentService;
    @Resource
    LoginUserUtil loginUserUtil;

    /**
     * @Description: 添加评论
     * @params: [dto]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/comment/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveComment(@RequestBody CommentDto dto) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            Comment comment = commentService.saveComment(dto, loginUser);

            result = new Result(true, CodeMessageEnum.success.getCode(), comment);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, "保存评论失败" + JsonUtils.beanToJson(dto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 删除评论
     * @params: [dto]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/comment/del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String delComment(@RequestBody CommentDto dto) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            commentService.delComment(dto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, "删除评论失败" + JsonUtils.beanToJson(dto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 评论列表
     * @params: [dto]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/comment/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCommentList(CommentDto dto) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();

        PageResult re = null;
        try {
            re = commentService.getCommentList(dto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "分享列表" + JsonUtils.beanToJson(dto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
}
