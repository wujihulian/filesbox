package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.NoticeDetailDao;
import com.svnlan.user.dao.NoticeUserDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.NoticeDetail;
import com.svnlan.user.domain.NoticeUser;
import com.svnlan.user.dto.NoticeDTO;
import com.svnlan.user.service.NoticeUserService;
import com.svnlan.utils.CaffeineUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lingxu
 * @description 针对表【notice_user】的数据库操作Service实现
 * @createDate 2023-05-16 15:20:58
 */
@Service
@Slf4j
public class NoticeUserServiceImpl extends ServiceImpl<NoticeUserDao, NoticeUser> implements NoticeUserService {
    @Resource
    private NoticeUserDao noticeUserDao;

    @Resource
    private NoticeDetailDao noticeDetailDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GroupDao groupDao;

    @Resource
    private Environment environment;

    /**
     * 建立一条通知与多个在线用户的关联关系 【创建通知或者编辑通知】
     *
     * @param id    通知id
     * @param now   当前时间
     * @param isAll 是否为全部用户
     */
    @Override
    public void buildNoticeAndUserRelation(Long id, LocalDateTime now, NoticeDTO dto, boolean isAll) {
        // 当前在线的用户
        ConcurrentMap<@NonNull Long, @NonNull Long> onlineUserList = CaffeineUtil.CURRENT_ONLINE_USER.asMap();
        if (CollectionUtils.isEmpty(onlineUserList)) {
            if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
                // 当前没有在线的用户
                return;
            }
            onlineUserList.put(111L, 234234525432L);
            onlineUserList.put(30010L, 234234525432L);
            onlineUserList.put(30130L, 234234525432L);
            onlineUserList.put(30056L, 234234525432L);
            onlineUserList.put(30074L, 234234525432L);
        }
        Set<@NonNull Long> userIds = onlineUserList.keySet();
        log.info("当前在线的用户Id =>{}", userIds);
        Stream<@NonNull Long> stream = userIds.stream();
        if (!isAll) {
            // 需要关联的用户
            List<Long> userIdList = Optional.ofNullable(dto.getUserIds()).orElseGet(Collections::emptyList);
            Set<Long> userIdSet = new HashSet<>(userIdList);
            // 需要关联的部门
            List<Long> deptIdList = dto.getDeptIds();
            if (!CollectionUtils.isEmpty(deptIdList)) {
                // 根据部门id查询下面的用户
                List<Long> groupUserIds = groupDao.getUserIdByGroupId(deptIdList);
                userIdSet.addAll(groupUserIds);
            }
            // 需要关联的角色
            List<Long> roleIdList = dto.getRoleIds();
            if (!CollectionUtils.isEmpty(roleIdList)) {
                // 根据部门id查询下面的用户
                List<Long> roleIdUserIds = userDao.getUserIdByRoleId(roleIdList);
                userIdSet.addAll(roleIdUserIds);
            }
            log.info("符合条件的用户Id =>{}", userIdList);
            stream = stream.filter(userIdSet::contains);
        }
        // 当前在线的用户，需要在 userIdSet 中， 才能建立关联关系
        // 这里需要更新通知的目标用户进行过滤
        List<NoticeUser> needToInsertList = stream.map(it -> new NoticeUser(id, it, now)).collect(Collectors.toList());
        Assert.isTrue(saveBatch(needToInsertList), "保存通知用户关系失败");
    }


    /**
     * 建立登录用户与多个通知的关联关系 【用户登录完成之后】
     *
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buildRelationAfterLogin(Long userId, boolean needCheckUnique) {
        // 查询出表中最大的创建时间
        LocalDateTime startQueryTime = noticeUserDao.getMaxCreateTime(userId);
        if (Objects.isNull(startQueryTime)) {
            // 说明该用户从来没有收到通知，此时需要用用户的注册时间作为通知查询开始时间
            Long createTimestamp = userDao.getCreateTime(userId);
            // 转化为 localDateTime
            startQueryTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(createTimestamp), ZoneId.of("+0"));
        }
        log.info("startQueryTime =>{}", startQueryTime);
        // 查询该用户的 roleId 和所在部门id
        Long roleID = Long.valueOf(userDao.getUserInfo(userId).getRoleID());
        List<Long> groupIdList = groupDao.getGroupIdByUserId(userId);
        log.info("userId => {} roleId =>{} groupIdList => {}", userId, roleID, groupIdList);
        // 查询 startQueryTime 之后的所有通知【modifyTime】
        List<NoticeDetail> noticeDetailList = noticeDetailDao.selectUnSyncList(startQueryTime);
        if (CollectionUtils.isEmpty(noticeDetailList)) {
            // 没有更新的通知
            return;
        }
        // 查询当前用户已经关联的通知
        List<Long> originNoticeIdList;
        if (needCheckUnique) {
            originNoticeIdList = list(
                    new LambdaQueryWrapper<NoticeUser>()
                            .select(NoticeUser::getNoticeId)
                            .eq(NoticeUser::getUserId, userId))
                    .stream().map(NoticeUser::getNoticeId).collect(Collectors.toList());
        } else {
            // 不需要检验唯一性
            originNoticeIdList = null;
        }

        List<NoticeUser> needToInsertList =  noticeDetailList.stream().map(NoticeTemp::new)
                .filter(it -> it.checkIfMatch(userId, groupIdList, roleID, originNoticeIdList))
                .map(it -> new NoticeUser(it.noticeId, userId, it.modifyTime)).collect(Collectors.toList());

        Assert.isTrue(saveBatch(needToInsertList), "保存通知用户关系失败");
    }


    private static class NoticeTemp {
        public Long noticeId;

        public Integer isAll;

        public List<Long> userIds;

        public List<Long> deptIds;

        public List<Long> roleIds;

        private LocalDateTime modifyTime;

        public NoticeTemp(NoticeDetail noticeDetail) {
            this.isAll = noticeDetail.getIsAll();
            this.noticeId = noticeDetail.getNoticeId();
            if (StringUtils.hasText(noticeDetail.getTargetUserIds()) && !noticeDetail.getTargetUserIds().equals("[]")) {
                userIds = JSONArray.parseArray(noticeDetail.getTargetUserIds(), Long.class);
            }
            if (StringUtils.hasText(noticeDetail.getTargetDeptIds()) && !noticeDetail.getTargetDeptIds().equals("[]")) {
                deptIds = JSONArray.parseArray(noticeDetail.getTargetDeptIds(), Long.class);
            }
            if (StringUtils.hasText(noticeDetail.getTargetRoleIds()) && !noticeDetail.getTargetRoleIds().equals("[]")) {
                roleIds = JSONArray.parseArray(noticeDetail.getTargetRoleIds(), Long.class);
            }
        }

        /**
         * 判断当前用户是否与该通知匹配上
         *
         * @param userId             用户id
         * @param groupIdList        部门id
         * @param roleId             角色id
         * @param originNoticeIdList 已建立关联的通知id
         */
        public boolean checkIfMatch(Long userId, List<Long> groupIdList, Long roleId, List<Long> originNoticeIdList) {
            if (!CollectionUtils.isEmpty(originNoticeIdList)) {
                // 判断之前该消息是否已经与该用户建立了关联
                if (originNoticeIdList.stream()
                        .anyMatch(it -> Objects.equals(this.noticeId, it))) {
                    return false;
                }
            }
            if (Objects.equals(this.isAll, 1)) {
                // 如果是所有人
                return true;
            }
            if (Objects.nonNull(this.userIds) && this.userIds.contains(userId)) {
                // 用户匹配上了
                return true;
            }

            if (!CollectionUtils.isEmpty(this.roleIds)
                    && Objects.nonNull(roleId)
                    && this.roleIds.contains(roleId)) {
                // 角色匹配上了
                return true;
            }

            if (!CollectionUtils.isEmpty(this.deptIds) && !CollectionUtils.isEmpty(groupIdList)) {
                for (Long groupId : groupIdList) {
                    if (this.deptIds.contains(groupId)) {
                        // 部门匹配上了
                        return true;
                    }
                }
            }
            return false;
        }
    }
}




