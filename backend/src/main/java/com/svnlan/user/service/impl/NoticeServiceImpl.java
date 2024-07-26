package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.NoticeDao;
import com.svnlan.user.domain.Notice;
import com.svnlan.user.dto.NoticeDTO;
import com.svnlan.user.service.NoticeDetailService;
import com.svnlan.user.service.NoticeService;
import com.svnlan.user.service.NoticeUserService;
import com.svnlan.utils.LoginUserUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.svnlan.jooq.Tables.*;

/**
 * @author lingxu
 * @description 针对表【notice】的数据库操作Service实现
 * @createDate 2023-05-16 15:11:07
 */
@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private NoticeDetailService noticeDetailService;

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private NoticeUserService noticeUserService;

    @Resource
    private DSLContext context;

    /**
     * 新增通知消息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addNotice(NoticeDTO dto) {
        // 当前登录用户id
        LoginUser loginUser = loginUserUtil.getLoginUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 写入到 notice 表
        Notice noticeInsert = new Notice();
        noticeInsert.populateData(dto, loginUser);
        noticeInsert.setCreateTime(now);
        noticeInsert.setModifyTime(now);
        // 查询现在表里最大的 order
        Long maxOrder = Optional.ofNullable(noticeDao.selectMaxOrder()).orElse(0L);
        noticeInsert.setSort(++maxOrder);
        Long noticeId = noticeDao.save(noticeInsert);
        // 写入到 notice_detail 表
        boolean isAll = Objects.equals(noticeDetailService.createNoticeDetail(dto, noticeId, now), 1);
        if (Objects.equals(dto.getEnable(), 1)) {
            // 目标用户为部分用户 并且启用了的话
            // 写入 notice_user 表
            noticeUserService.buildNoticeAndUserRelation(noticeId, now, dto, isAll);

            // 更新通知的状态为 已发送
            context.update(NOTICE)
                    .set(NOTICE.STATUS, 1)
                    .where(NOTICE.ID.eq(noticeId))
                    .execute();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editNotice(NoticeDTO dto) {
        // 判断该消息是否存在
//        Notice originNotice = getOne(
//                new LambdaQueryWrapper<Notice>()
//                        .select(Notice::getId).eq(Notice::getId, dto.getId()));
        Notice originNotice = context.select(NOTICE.ID)
                .from(NOTICE)
                .where(NOTICE.ID.eq(dto.getId()))
                .fetchOneInto(Notice.class);
        Assert.notNull(originNotice, "未查询到要编辑的通知");
        // 当前登录用户
        LoginUser loginUser = loginUserUtil.getLoginUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 更新 notice 表
        Notice noticeUpdate = new Notice(dto.getId());
        noticeUpdate.populateData(dto, loginUser);
        noticeUpdate.setModifyTime(now);
        noticeDao.updateById(noticeUpdate);

        // 更新 notice_detail 表
        boolean isAll = noticeDetailService.updateByNoticeId(dto, dto.getId(), now);
        // notice_user表也要同步修改
//        noticeUserService.remove(new LambdaQueryWrapper<NoticeUser>()
//                .eq(NoticeUser::getNoticeId, dto.getId()));
        context.deleteFrom(NOTICE_USER)
                .where(NOTICE_USER.NOTICE_ID.eq(dto.getId()))
                .execute();
        if (Objects.equals(dto.getEnable(), 1)) {
            // 写入 notice_user 表
            noticeUserService.buildNoticeAndUserRelation(dto.getId(), now, dto, isAll);
        }
    }

    @Override
    public JSONObject detail(Long id) {
        // 状态，0暂存，1已发送，2已删除
        Notice notice = context.select()
                .from(NOTICE)
                .where(NOTICE.ID.eq(id))
                .and(NOTICE.STATUS.in(0, 1))
                .fetchOneInto(Notice.class);
        // 查询 notice_detail
        JSONObject noticeDetailJSON = noticeDetailService.getByNoticeId(id);
        return ObjUtil.toJsonObject(noticeDetailJSON, Notice.class, notice, false);
    }

    @Override
    public List<JSONObject> preview(List<Long> ids) {
        List<JSONObject> list = noticeDao.querySimpleInfo(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        for (JSONObject item : list) {
            timestampToLocalDateTime(item, "sendTime", "createTime");
        }
        return list;
    }

    @Override
    public void operateEnable(Long id) {
        Notice notice = context.select().from(NOTICE).where(NOTICE.ID.eq(id)).fetchOneInto(Notice.class);
        Assert.isTrue(Objects.nonNull(notice) &&
                !Objects.equals(notice.getStatus(), 2), "未查询到通知 id => " + id);
        Assert.isTrue(context.update(NOTICE)
                .set(NOTICE.ENABLE, Objects.equals(notice.getEnable(), 1) ? 0 : 1)
                .where(NOTICE.ID.eq(id))
                .execute() == 1, "更新enable失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        Assert.isTrue(
                context.update(NOTICE).set(NOTICE.STATUS, 2).where(NOTICE.ID.eq(id).and(NOTICE.STATUS.ne(2)))
                        .execute() == 1,
                "删除失败，数据不存在或已经删除");
        // 删除 user_detail
        context.update(NOTICE_DETAIL).set(NOTICE_DETAIL.DR, 0).where(NOTICE_DETAIL.NOTICE_ID.eq(id))
                .execute();
        // 删除 通知用户的对应关系
        context.delete(NOTICE_USER)
                .where(NOTICE_USER.NOTICE_ID.eq(id))
                .execute();
    }

    /**
     * 查询未读消息数
     */
    @Override
    public Long hasNoticeUnread() {
        Long userId = loginUserUtil.getLoginUserId();
        log.info("查询未读消息总数 userId => {}", userId);
        // 同步数据通知到用户
        noticeUserService.buildRelationAfterLogin(userId, false);
        // 查询未读消息总数
        return noticeDao.selectNoticeUnreadListTotal(userId);
    }

    /**
     * c端查询通知列表
     */
    @Override
    public JSONObject pageList(Integer currentPage, Integer pageSize) {
        currentPage = Optional.ofNullable(currentPage).orElse(1);
        pageSize = Optional.ofNullable(pageSize).orElse(10);

        Long userId = loginUserUtil.getLoginUserId();
        // 标题、内容、创建时间
        Long total = noticeDao.selectNoticeListTotal(userId);
        int startIndex = (currentPage - 1) * pageSize;
        List<JSONObject> list = noticeDao.selectNoticeList(userId, startIndex, pageSize);
        for (JSONObject item : list) {
            timestampToLocalDateTime(item, "sendTime", "createTime");
        }
        return new JSONObject().fluentPut("list", list).fluentPut("total", total);
    }

    private void timestampToLocalDateTime(JSONObject jsonObj, String... keys) {
        for (String key : keys) {
            Object timestamp = jsonObj.getTimestamp(key);
            if (Objects.nonNull(timestamp)) {
                LocalDateTime localDateTime = ((Timestamp) timestamp).toLocalDateTime();
                jsonObj.put(key, localDateTime);
            }
        }
    }

    @Override
    public void executeNoticeRead(Long id) {
        LocalDateTime now = LocalDateTime.now();
        context.update(NOTICE_USER)
                .set(NOTICE_USER.IS_READ, 1)
                .set(NOTICE_USER.MODIFY_TIME, now)
                .where(NOTICE_USER.IS_READ.eq(0))
                .and(NOTICE_USER.USER_ID.eq(loginUserUtil.getLoginUserId()))
                .and(NOTICE_USER.NOTICE_ID.eq(id))
                .execute();
    }

    @Override
    public Pair<List<Notice>, Long> listPage(Integer currentPage, Integer pageSize) {

        //        IPage<Notice> page = noticeService.page(
//                new Page<>(currentPage, pageSize),
//                new LambdaQueryWrapper<Notice>()
//                        .select(Notice::getId, Notice::getSendTime, Notice::getStatus, Notice::getTitle)
//                        .in(Notice::getStatus, 0, 1)
//                        .orderByDesc(Notice::getSort, Notice::getCreateTime)
        List<Notice> noticeList = context.select(NOTICE.ID, NOTICE.SEND_TIME, NOTICE.STATUS, NOTICE.TITLE)
                .from(NOTICE)
                .where(NOTICE.STATUS.in(0, 1))
                .orderBy(NOTICE.SORT, NOTICE.CREATE_TIME)
                .fetchInto(Notice.class);

        Long total = context.select(DSL.count(NOTICE.ID))
                .from(NOTICE)
                .where(NOTICE.STATUS.in(0, 1))
                .fetchOneInto(Long.class);

        return Pair.of(noticeList, total);
    }
}




