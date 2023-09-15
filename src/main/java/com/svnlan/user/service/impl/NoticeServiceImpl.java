package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.NoticeDao;
import com.svnlan.user.domain.Notice;
import com.svnlan.user.domain.NoticeDetail;
import com.svnlan.user.domain.NoticeUser;
import com.svnlan.user.dto.NoticeDTO;
import com.svnlan.user.service.NoticeDetailService;
import com.svnlan.user.service.NoticeService;
import com.svnlan.user.service.NoticeUserService;
import com.svnlan.utils.LoginUserUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author lingxu
 * @description 针对表【notice】的数据库操作Service实现
 * @createDate 2023-05-16 15:11:07
 */
@Slf4j
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, Notice> implements NoticeService {

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private NoticeDetailService noticeDetailService;

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private NoticeUserService noticeUserService;

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
        save(noticeInsert);
        // 写入到 notice_detail 表
        boolean isAll = Objects.equals(noticeDetailService.createNoticeDetail(dto, noticeInsert.getId(), now), 1);
        if (Objects.equals(dto.getEnable(), 1)) {
            // 目标用户为部分用户 并且启用了的话
            // 写入 notice_user 表
            noticeUserService.buildNoticeAndUserRelation(noticeInsert.getId(), now, dto, isAll);

            // 更新通知的状态为 已发送
            update(new LambdaUpdateWrapper<Notice>()
                    .set(Notice::getStatus, 1)
                    .eq(Notice::getId, noticeInsert.getId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editNotice(NoticeDTO dto) {
        // 判断该消息是否存在
        Notice originNotice = getOne(
                new LambdaQueryWrapper<Notice>()
                        .select(Notice::getId).eq(Notice::getId, dto.getId()));
        Assert.notNull(originNotice, "未查询到要编辑的通知");
        // 当前登录用户
        LoginUser loginUser = loginUserUtil.getLoginUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 更新 notice 表
        Notice noticeUpdate = new Notice(dto.getId());
        noticeUpdate.populateData(dto, loginUser);
        noticeUpdate.setModifyTime(now);
        updateById(noticeUpdate);

        // 更新 notice_detail 表
        boolean isAll = noticeDetailService.updateByNoticeId(dto, dto.getId(), now);
        // notice_user表也要同步修改
        noticeUserService.remove(new LambdaQueryWrapper<NoticeUser>()
                .eq(NoticeUser::getNoticeId, dto.getId()));
        if (Objects.equals(dto.getEnable(), 1)) {
            // 写入 notice_user 表
            noticeUserService.buildNoticeAndUserRelation(dto.getId(), now, dto, isAll);
        }
    }

    @Override
    public JSONObject detail(Long id) {
        // 状态，0暂存，1已发送，2已删除
        Notice notice = getOne(new LambdaQueryWrapper<Notice>().eq(Notice::getId, id).in(Notice::getStatus, 0, 1));
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
        Notice notice = getById(id);
        Assert.isTrue(Objects.nonNull(notice) &&
                !Objects.equals(notice.getStatus(),2), "未查询到通知 id => " + id);
        if (Objects.equals(notice.getEnable(), 1)) {
            // 启用 变为禁用
            Assert.isTrue(
                    update(new LambdaUpdateWrapper<Notice>()
                            .eq(Notice::getId, id)
                            .set(Notice::getEnable, 0)),
                    "更新enable失败");
        } else {
            // 未启用
            Assert.isTrue(
                    update(new LambdaUpdateWrapper<Notice>()
                            .eq(Notice::getId, id)
                            .set(Notice::getEnable, 0)),
                    "更新enable失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        Assert.isTrue(
                update(new LambdaUpdateWrapper<Notice>()
                        .eq(Notice::getId, id)
                        .ne(Notice::getStatus, 2)
                        .set(Notice::getStatus, 2)),
                "删除失败，数据不存在或已经删除"
        );
        // 删除 user_detail
        noticeDetailService.update(
                new LambdaUpdateWrapper<NoticeDetail>()
                        .eq(NoticeDetail::getNoticeId, id)
                        .set(NoticeDetail::getDr, 0)
        );
        // 删除 通知用户的对应关系
        noticeUserService.remove(new LambdaQueryWrapper<NoticeUser>()
                .eq(NoticeUser::getNoticeId, id));
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
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
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

        noticeUserService.update(new LambdaUpdateWrapper<NoticeUser>()
                .set(NoticeUser::getIsRead, 1)
                .set(NoticeUser::getModifyTime, now)
                .eq(NoticeUser::getIsRead, 0)
                .eq(NoticeUser::getUserId, loginUserUtil.getLoginUserId())
                .eq(NoticeUser::getNoticeId, id));
    }
}




