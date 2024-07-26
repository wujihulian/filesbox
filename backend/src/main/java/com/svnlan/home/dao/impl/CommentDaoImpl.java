package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.CommentDao;
import com.svnlan.home.domain.Comment;
import com.svnlan.home.vo.CommentVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.svnlan.jooq.Tables.*;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/9/12 17:26
 */
@Service
public class CommentDaoImpl implements CommentDao {
    @Autowired
    private DSLContext context;
    @Resource
    TenantUtil tenantUtil;

    @Override
    public int insert(Comment comment){
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMENT)
                .set(COMMENT.PID, comment.getPid())
                .set(COMMENT.USER_ID, comment.getUserID())
                .set(COMMENT.TARGET_TYPE, comment.getTargetType())
                .set(COMMENT.TARGET_ID, comment.getTargetID())
                .set(COMMENT.CONTENT, ObjectUtils.isEmpty(comment.getContent()) ? "" : comment.getContent())
                .set(COMMENT.PRAISE_COUNT, 0)
                .set(COMMENT.COMMENT_COUNT, 0)
                .set(COMMENT.STATUS, 1)
                .set(COMMENT.CREATE_TIME, now)
                .set(COMMENT.MODIFY_TIME, now)
                .set(COMMENT.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMENT.ID).fetchOne().getId();

        comment.setCommentID(id);
        return 1;
    }
    @Override
    public int setCommentCount(Comment comment){
        return context.update(COMMENT)
                .set(COMMENT.COMMENT_COUNT, COMMENT.COMMENT_COUNT.add(comment.getCommentCount()))
                .set(COMMENT.MODIFY_TIME, LocalDateTime.now())
                .where(COMMENT.ID.eq(comment.getCommentID()))
                .execute();
    }
    @Override
    public int deleteComment(Long commentID){
        return context.delete(COMMENT)
                .where(COMMENT.ID.eq(commentID).or(COMMENT.PID.eq(commentID)))
                .execute();
    }
    @Override
    public List<CommentVo> getCommentList(Map<String, Object> map){
        SelectOnConditionStep<Record10<Long, Long, Long, Integer, Long, String, LocalDateTime, String, String, String>>
                step = context.select(COMMENT.ID.as("commentID"), COMMENT.PID.as("pid"), COMMENT.USER_ID.as("userID"),COMMENT.TARGET_TYPE,COMMENT.TARGET_ID.as("targetID")
                , COMMENT.CONTENT,COMMENT.CREATE_TIME,USERS.NAME,USERS.NICKNAME,USERS.AVATAR

        ).from(COMMENT)
                .leftJoin(USERS).on(USERS.ID.eq(COMMENT.USER_ID));
        SelectConditionStep<Record10<Long, Long, Long, Integer, Long, String, LocalDateTime, String, String, String>>
                where = step.where(getCommentListCondition(map));

        where.orderBy(COMMENT.CREATE_TIME.asc());
        Integer startIndex = (Integer) map.get("startIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            where.limit(startIndex,pageSize);
        }
        return where.fetchInto(CommentVo.class);

    }
    @Override
    public long getCountComment(Map<String, Object> map){

        SelectJoinStep<Record1<Integer>>
                step = context.selectCount().from(COMMENT);
        return step.where(getCommentListCondition(map)).fetchOneInto(Long.class);

    }
    public Condition getCommentListCondition(Map<String, Object> map) {
        Long targetID = (Long) map.get("targetID");
        Long tenantId = (Long) map.get("tenantId");
        Condition condition = DSL.trueCondition().and(COMMENT.TARGET_ID.eq(targetID))
                .and(COMMENT.STATUS.eq(1))
                .and(COMMENT.TENANT_ID.eq(tenantId));

        Long id = (Long) map.get("idFrom");
        if (!ObjectUtils.isEmpty(id)){
            condition = condition.and(COMMENT.ID.eq(id));
        }
        Integer targetType = (Integer) map.get("targetType");
        if (!ObjectUtils.isEmpty(id)){
            condition = condition.and(COMMENT.TARGET_TYPE.eq(targetType));
        }
        return condition;
    }
    @Override
    public Integer checkCommentExist(Long commentID){
        return context.selectCount().from(COMMENT).where(COMMENT.ID.eq(commentID)).fetchOneInto(Integer.class);
    }
    @Override
    public List<CommentVo> getCommentListByIds(List<Long> list){
        SelectOnConditionStep<Record5<Long, String, String, String, String>>
                step = context.select(COMMENT.ID.as("commentID"),COMMENT.CONTENT,USERS.NAME,USERS.NICKNAME,USERS.AVATAR
        ).from(COMMENT)
                .leftJoin(USERS).on(USERS.ID.eq(COMMENT.USER_ID));
        SelectConditionStep<Record5<Long, String, String, String, String>>
                where = step.where(COMMENT.ID.in(list));

        return where.fetchInto(CommentVo.class);
    }
    @Override
    public CommentVo getTargetIdByCommentId(Long commentID){
        return context.select(COMMENT.TARGET_ID.as("targetID"), COMMENT.USER_ID.as("userID")).from(COMMENT).where(COMMENT.ID.eq(commentID)).fetchOneInto(CommentVo.class);
    }
}
