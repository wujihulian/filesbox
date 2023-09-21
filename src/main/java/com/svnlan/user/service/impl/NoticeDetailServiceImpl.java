package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.NoticeDetailDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.NoticeDetail;
import com.svnlan.user.dto.NoticeDTO;
import com.svnlan.user.service.NoticeDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author lingxu
 * @description 针对表【notice_detail】的数据库操作Service实现
 * @createDate 2023-05-16 15:14:04
 */
@Service
public class NoticeDetailServiceImpl extends ServiceImpl<NoticeDetailDao, NoticeDetail> implements NoticeDetailService {

    @Resource
    private GroupDao groupDao;

    @Override
    public boolean updateByNoticeId(NoticeDTO dto, Long noticeId, LocalDateTime now) {
        // 更新 notice_detail 表
        NoticeDetail noticeDetailUpdate = new NoticeDetail();
        // 是否选择了所有用户
        Integer isAll = noticeDetailUpdate.populateData(dto, false);
        noticeDetailUpdate.setModifyTime(now);
        Assert.isTrue(update(noticeDetailUpdate, new LambdaQueryWrapper<NoticeDetail>().eq(NoticeDetail::getNoticeId, noticeId)), "更新 notice_detail 失败");
        return Objects.equals(isAll, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer createNoticeDetail(NoticeDTO dto, Long noticeId, LocalDateTime now) {
        NoticeDetail noticeDetailInsert = new NoticeDetail(noticeId);
        noticeDetailInsert.populateData(dto, true);
        noticeDetailInsert.setCreateTime(now);
        noticeDetailInsert.setModifyTime(now);
        save(noticeDetailInsert);
        return noticeDetailInsert.getIsAll();
    }

    @Override
    public JSONObject getByNoticeId(Long id) {
        List<NoticeDetail> noticeDetailList = list(new LambdaQueryWrapper<NoticeDetail>()
                .select(NoticeDetail::getNoticeId, NoticeDetail::getContent, NoticeDetail::getIsAll,
                        NoticeDetail::getTargetUserIds, NoticeDetail::getTargetDeptIds,
                        NoticeDetail::getTargetRoleIds)
                .eq(NoticeDetail::getNoticeId, id)
                .eq(NoticeDetail::getDr, 0));
        Assert.notEmpty(noticeDetailList, "未查询到通知详细数据");
        // 找出主数据 notice_detail_id 为 NULL的
        NoticeDetail noticeDetail;
        List<Long> userIdList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        List<Object> deptIdParentLevelList = new ArrayList<>();
        List<Long> roleIdList = new ArrayList<>();
        if (noticeDetailList.size() == 1) {
            // 表示只有主数据
            noticeDetail = noticeDetailList.get(0);
            if (!Objects.equals(noticeDetail.getIsAll(), 1)) {
                // 表示目标是部分用户
                mergeIds(noticeDetail, userIdList, deptIdList, deptIdParentLevelList, roleIdList);
            }
        } else {
            // 主数据
            noticeDetail = noticeDetailList.stream().filter(it -> Objects.isNull(it.getNoticeDetailId())).findFirst().get();
            for (NoticeDetail itemDetail : noticeDetailList) {
                mergeIds(itemDetail, userIdList, deptIdList, deptIdParentLevelList, roleIdList);
            }
        }
        return ObjUtil.toJsonObject(null, NoticeDetail.class, noticeDetail, true)
                .fluentPut("userIds", userIdList)
                .fluentPut("deptIds", deptIdList)
                .fluentPut("deptParentLevel", deptIdParentLevelList)
                .fluentPut("roleIds", roleIdList);
    }

    private void mergeIds(NoticeDetail itemDetail,
                          List<Long> userIdList,
                          List<Long> deptIdList,
                          List<Object> deptIdParentLevelList,
                          List<Long> roleIdList) {
        List<Long> userIdTemp = resolveIds(itemDetail.getTargetUserIds(), null);
        if (!CollectionUtils.isEmpty(userIdTemp)) {
            userIdList.addAll(userIdTemp);
        }
        List<Long> deptIdTemp = resolveIds(itemDetail.getTargetDeptIds(), ids -> {
            // deptIdParentLevelList
            List<Object> parentLevel = groupDao.selectObjs(new LambdaQueryWrapper<Group>().select(Group::getParentLevel).in(Group::getGroupID, ids));
            deptIdParentLevelList.addAll(parentLevel);
        });
        if (!CollectionUtils.isEmpty(deptIdTemp)) {
            deptIdList.addAll(deptIdTemp);
        }
        List<Long> roleIdTemp = resolveIds(itemDetail.getTargetRoleIds(), null);
        if (!CollectionUtils.isEmpty(roleIdTemp)) {
            roleIdList.addAll(roleIdTemp);
        }
    }

    private List<Long> resolveIds(String idStr, Consumer<List<Long>> consumer) {
        if (StringUtils.hasText(idStr)) {
            List<Long> ids = JSONArray.parseArray(idStr, Long.class);
            if (Objects.nonNull(consumer) && !CollectionUtils.isEmpty(ids)) {
                consumer.accept(ids);
            }
            return ids;
        }
        return Collections.emptyList();
    }
}




