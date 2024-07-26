package com.svnlan.home.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.CommentDao;
import com.svnlan.home.domain.Comment;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CommentDto;
import com.svnlan.home.service.CommentService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.CommentVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import com.svnlan.utils.TenantUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/1 14:19
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentDao commentDao;
    @Resource
    TenantUtil tenantUtil;

    @Resource
    UserAuthTool userAuthTool;
    @Resource
    FileOptionTool fileOptionTool;

    @Override
    public Comment saveComment(CommentDto dto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(dto.getTargetID()) || ObjectUtils.isEmpty(dto.getContent())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (dto.getContent().length() > 1024){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        // 评论权限校验
        checkCommentGroupAuth(dto.getTargetID(), loginUser, "8,12");

        Comment comment = new Comment();
        comment.setPid(ObjectUtils.isEmpty(dto.getPid()) ? 0L : dto.getPid());
        comment.setUserID(loginUser.getUserID());
        comment.setTargetID(dto.getTargetID());
        comment.setTargetType(ObjectUtils.isEmpty(dto.getTargetType()) ? 2 : dto.getTargetType());
        comment.setContent(dto.getContent());
        comment.setPraiseCount(0);
        comment.setCommentCount(0);
        comment.setStatus(1);
        if (!ObjectUtils.isEmpty(dto.getPid())) {
            Integer check = commentDao.checkCommentExist(dto.getPid());
            if (ObjectUtils.isEmpty(check) || check <= 0){
                LogUtil.error("saveComment 添加失败，回复的评论已被删除 comment=" + JsonUtils.beanToJson(comment));
                return null;
            }
        }
        try {
            commentDao.insert(comment);
        }catch (Exception e){
            LogUtil.error("添加评论失败！");
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        return comment;
    }
    @Override
    public void delComment(CommentDto dto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(dto.getCommentID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        CommentVo commentVo = commentDao.getTargetIdByCommentId(dto.getCommentID());
        if (ObjectUtils.isEmpty(commentVo)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 添加删除自己的评论：评论+编辑，删除他人的评论：管理权限
        String auth;
        if (commentVo.getUserID().longValue() == loginUser.getUserID().longValue()){
            auth = "8,12";
        }else {
            auth = "14";
        }
        // 评论权限校验
        checkCommentGroupAuth(commentVo.getTargetID(), loginUser, auth);

        try {
            commentDao.deleteComment(dto.getCommentID());
        }catch (Exception e){
            LogUtil.error("删除评论失败！");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
    }

    @Override
    public PageResult getCommentList(CommentDto dto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(dto.getTargetID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 评论权限校验
        checkCommentGroupAuth(dto.getTargetID(), loginUser, "12");

        dto.setTargetType(ObjectUtils.isEmpty(dto.getTargetType()) ? 2 : dto.getTargetType());
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("targetID", dto.getTargetID());
        paramMap.put("targetType", dto.getTargetType());
        if (!ObjectUtils.isEmpty(dto.getIdFrom())){
            paramMap.put("idFrom", dto.getIdFrom());
        }
        paramMap.put("tenantId", tenantUtil.getTenantIdByServerName());
        paramMap.put("startIndex", dto.getStartIndex());
        paramMap.put("pageSize", dto.getPageSize());
        long total = commentDao.getCountComment(paramMap);
        if (total <= 0){
            return new PageResult(0L, new ArrayList());
        }

        List<CommentVo> list = commentDao.getCommentList(paramMap);

        if (!CollectionUtils.isEmpty(list)){
            List<Long> pidList = list.stream().filter(n->n.getPid() > 0).map(CommentVo::getPid).collect(Collectors.toList());
            Map<Long, CommentVo> pMap = null;
            if (!CollectionUtils.isEmpty(pidList)){
                List<CommentVo> pList = commentDao.getCommentListByIds(pidList);
                if (!CollectionUtils.isEmpty(pList)){
                    pMap = pList.stream().collect(Collectors.toMap(CommentVo::getCommentID, Function.identity(), (v1, v2) -> v2));
                }
            }
            CommentVo pVo = null;
            for (CommentVo vo : list){
                if (vo.getPid() > 0 && !ObjectUtils.isEmpty(pMap) && pMap.containsKey(vo.getPid())){
                    pVo = pMap.get(vo.getPid());
                    vo.setTargetContent(pVo.getContent());
                    vo.setTargetNickname(pVo.getNickname());
                    vo.setTargetUserName(pVo.getName());
                }
                vo.setTargetContent(ObjectUtils.isEmpty(vo.getTargetContent()) ? "" : vo.getTargetContent());
                vo.setTargetNickname(ObjectUtils.isEmpty(vo.getTargetNickname()) ? "" : vo.getTargetNickname());
                vo.setTargetUserName(ObjectUtils.isEmpty(vo.getTargetUserName()) ? "" : vo.getTargetUserName());
                vo.setAvatar(ObjectUtils.isEmpty(vo.getAvatar()) ? "" : vo.getAvatar());
            }
        }

        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    /**  1 查看 2 添加 3 删除*/
    private void checkCommentGroupAuth(Long sourceId, LoginUser loginUser, String auth){
        CommonSource commonSource = fileOptionTool.getSourceInfo(sourceId);
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), auth, commonSource.getTargetType());

    }

}
