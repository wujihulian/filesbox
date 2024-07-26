package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.ShareDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.Share;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.dao.ShareReportDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.ShareReport;
import com.svnlan.user.dto.ShareReportDTO;
import com.svnlan.user.service.ShareReportService;
import com.svnlan.user.vo.ShareReportVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.LoginUserUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.svnlan.jooq.Tables.SHARE_REPORT;
import static com.svnlan.jooq.tables.Share.SHARE;

/**
 * 分享资源举报 实现
 *
 * @author lingxu 2023/04/06 09:47
 */
@Slf4j
@Service
public class ShareReportServiceImpl implements ShareReportService {

    @Resource
    private ShareReportDao shareReportDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ShareDao shareDao;

    @Resource
    private IoSourceDao ioSourceDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DSLContext context;

    @Override
    public JSONObject shareReportListPage(ShareReportDTO dto) {
        Long count = shareReportDao.selectCount(dto);
        if (Objects.isNull(count) || count <= 0) {
            return new JSONObject().fluentPut("list", Collections.emptyList())
                    .fluentPut("total", 0);
        }
        List<ShareReport> records = shareReportDao.selectList(dto);
        Map<Long, UserVo> simpleInfoMapList = deriveUserInfo(records);
        List<JSONObject> list = records.stream().map(it -> {
            UserVo userVo = Optional.ofNullable(simpleInfoMapList.get(it.getUserId())).orElseGet(() -> {
                UserVo temp = new UserVo();
                temp.setUserID(0L);
                temp.setName("匿名用户");
                return temp;
            });
            if (!ObjectUtils.isEmpty(userVo.getAvatar())) {
                userVo.setAvatar(FileUtil.getShowAvatarUrl(userVo.getAvatar(), userVo.getName()));
            }
            JSONObject jsonObj = new JSONObject().fluentPut("reportUserName", userVo.getName())
                    .fluentPut("reportUserAvatar", userVo.getAvatar());
            return ObjUtil.toJsonObject(jsonObj, ShareReport.class, it, true);
        }).collect(Collectors.toList());

        return new JSONObject().fluentPut("list", list)
                .fluentPut("total", count);
    }

    private Map<Long, UserVo> deriveUserInfo(List<ShareReport> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Set<Long> userIds = new HashSet<>();
//        Set<Long> shareIds = new HashSet<>();
        for (ShareReport item : list) {
            if (Objects.equals(item.getUserId(), 0L)) {
                // 此为匿名用户
                continue;
            }
            userIds.add(item.getUserId());
//            shareIds.add(item.getShareId());
        }
        // 基于此，有可能需要查询的用户是空的，也就是只有匿名用户
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }
        List<UserVo> simpleInfoMapList = userDao.getUserSimpleInfoByIds(userIds);
        return simpleInfoMapList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity()));
    }

    @Override
    public List<ShareReportVo> shareReportList(ShareReportDTO dto) {
        List<ShareReport> list = shareReportDao.selectList(dto);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Map<Long, UserVo> simpleInfoMapList = deriveUserInfo(list);
        return list.stream().map(it -> {
                    UserVo userVo = Optional.ofNullable(simpleInfoMapList.get(it.getUserId())).orElseGet(() -> {
                        UserVo user = new UserVo();
                        user.setUserID(0L);
                        user.setName("匿名用户");
                        return user;
                    });
                    return new ShareReportVo(it, userVo.getName());
                }
        ).collect(Collectors.toList());
    }

    public void doIoSourceOperate(List<IOSource> ioSourceList, Integer operateType) {
        // 如果是禁用分享的话， 还需要更新 io_source 表
        List<Long> needUpdateIds = new ArrayList<>();
        for (IOSource ioSource : ioSourceList) {
            if (Objects.equals(OperateEnum.DISABLE.type, operateType)) {
                // 资源是正常的状态下，才能为设置为 禁止分享
                Assert.isTrue(Objects.equals(ioSource.getCanShare(), 1), "资源是可分享状态下才能被设置为禁止分享");
                needUpdateIds.add(ioSource.getId());
            } else if (Objects.equals(OperateEnum.PERMIT.type, operateType)) {
                // 资源是禁止状态下，才能为设置为 正常
                Assert.isTrue(Objects.equals(ioSource.getCanShare(), 0), "资源是禁止状态下，才能为设置为允许分享");
                needUpdateIds.add(ioSource.getId());
            }
        }
        Integer canShare = Objects.equals(OperateEnum.DISABLE.type, operateType) ? 0 : 1;
        IOSource ioSourceUpdate = new IOSource();
        ioSourceUpdate.setCanShare(canShare);
        Assert.isTrue(ioSourceDao.updateCanShare(canShare, needUpdateIds) == needUpdateIds.size(), "更新ioSource canShare失败");

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void shareReportOperate(List<Long> ids, Integer operateType) {
        OperateEnum.checkValid(operateType);
        List<ShareReport> shareReport = shareReportDao.selectListByIds(ids);
        Assert.notEmpty(shareReport, "未查询到数据");

        // 批量操作的操作是同一种操作
        boolean needSuccessorExecute = false;
        for (ShareReport item : shareReport) {
            needSuccessorExecute = checkOperateWithStatus(item, operateType);
//            doShareReportOperate(item, operateType);
        }
        List<Long> sourceIds = shareReport.stream().map(ShareReport::getSourceId).distinct().collect(Collectors.toList());
        // 把该分享对应的未处理举报都更新为已处理状态
        context.update(SHARE_REPORT)
                .set(SHARE_REPORT.STATUS, 1)
                .where(SHARE_REPORT.SOURCE_ID.in(sourceIds))
                .and(SHARE_REPORT.STATUS.eq(0))
                .execute();

        if (needSuccessorExecute) {
            // 修改分享的状态 取消分享 禁用分享
            Integer tempOperateType = operateType;
            if (Objects.equals(operateType, OperateEnum.PERMIT.type)) {
                // 如果是允许分享， 说明之前有过禁止分享操作 需要变为取消状态
                tempOperateType = OperateEnum.CANCEL.type;
            }
            List<Long> shareIds = shareReport.stream().map(ShareReport::getShareId).distinct().collect(Collectors.toList());
            if (Objects.equals(operateType, OperateEnum.CANCEL.type)) {
                List<Share> shareList = context.select(SHARE.USER_ID).from(SHARE).where(SHARE.ID.in(shareIds)).fetchInto(Share.class);
//                List<Share> shareList = shareDao.selectList(new LambdaQueryWrapper<Share>().select(Share::getUserID).in(Share::getShareID, shareIds));
                // 删除缓存
                stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
                    for (Share share : shareList) {
                        conn.del((GlobalConfig.my_share_key + share.getUserID()).getBytes(StandardCharsets.UTF_8));
                    }
                    return null;
                });
            }
            shareDao.updateStatus(tempOperateType, shareIds);
        }
        if (Objects.equals(operateType, OperateEnum.PERMIT.type) || Objects.equals(operateType, OperateEnum.DISABLE.type)) {
//            List<IOSource> ioSourceList = ioSourceDao.selectList(new LambdaQueryWrapper<IOSource>().in(IOSource::getId, sourceIds));
            List<IOSource> ioSourceList = ioSourceDao.selectListByIds(sourceIds);
            Assert.notEmpty(ioSourceList, "未查询到资源数据");
            // 校验 ioSourceList 状态
            doIoSourceOperate(ioSourceList, operateType);
        }


    }

    @Override
    public void checkIfShareLinkPermit(ShareVo shareVo) {
        Integer status = shareVo.getStatus();
        Assert.isTrue(!Objects.equals(status, OperateEnum.DISABLE.type), "该分享链接被禁止了");
        Assert.isTrue(!Objects.equals(status, OperateEnum.CANCEL.type), "该分享链接被取消了");
    }

    @Resource
    private LoginUserUtil loginUserUtil;

    @Override
    public void shareReport(ShareReportDTO dto) {
        // 查询分享数据和分享的资源是否存在
        ShareVo shareVo = shareDao.getShareById(dto.getShareId());
        Assert.notNull(shareVo, "该分享不存在");
        // 查询资源
        IOSource ioSource = ioSourceDao.selectById(shareVo.getSourceID());
        Assert.notNull(ioSource, "该分享资源不存在");
        // 当前登录用户，也是举报人 0 表示匿名用户
        Long userId = 0L;
        try {
            userId = loginUserUtil.getLoginUserId();
        } catch (Exception e) {
            // ignore
            log.warn("该举报用户为匿名用户");
        }
        ShareReport shareReportInsert = new ShareReport();
        shareReportInsert.populateData(dto, shareVo, ioSource, userId);
        Assert.isTrue(shareReportDao.insert(shareReportInsert) == 1, "保存举报信息失败");
    }

    private boolean isCancelOrDisable(Integer operateType) {
        return Objects.equals(operateType, OperateEnum.CANCEL.type) || Objects.equals(operateType, OperateEnum.DISABLE.type);
    }

    /**
     * 校验状态与操作是否正确
     *
     * @return 是否需要更新后续 share 的状态
     */
    private boolean checkOperateWithStatus(ShareReport shareReport, Integer operateType) {
        // 判断其状态
        if (Objects.equals(shareReport.getStatus(), 0)) {
            // 表示未处理 此时可进行 关闭 取消 禁止 操作
            Assert.isTrue(!Objects.equals(operateType, OperateEnum.PERMIT.type), "该状态下不能进行允许操作");
            return isCancelOrDisable(operateType);
        } else {
            // 已处理
            // shareStatus 1 正常 2 取消分享 3 禁止分享
            if (Objects.equals(shareReport.getShareStatus(), 1)) {
                // 说明之前操作过关闭
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.CLOSE.type), "不能重复进行关闭操作");
                return isCancelOrDisable(operateType);
            } else if (Objects.equals(shareReport.getShareStatus(), OperateEnum.CANCEL.type)) {
                // 取消分享
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.CLOSE.type), "该状态下不能进行关闭操作");
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.CANCEL.type), "不能进行重复取消操作");
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.PERMIT.type), "该状态下不能进行允许操作");
                return true;
            } else if (Objects.equals(shareReport.getShareStatus(), OperateEnum.DISABLE.type)) {
                // 禁止分享
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.CLOSE.type), "该状态下不能进行关闭操作");
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.CANCEL.type), "不能进行取消操作");
                Assert.isTrue(!Objects.equals(operateType, OperateEnum.DISABLE.type), "不能重复禁止操作");
                return true;
            }
        }
        return false;
    }

    private enum OperateEnum {
        CLOSE(1, "关闭举报"), PERMIT(2, "允许分享"), DISABLE(3, "禁止分享"), CANCEL(4, "取消分享");
        public final Integer type;
        public final String desc;

        OperateEnum(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static void checkValid(Integer type) {
            Assert.isTrue(Arrays.stream(OperateEnum.values()).anyMatch(it -> Objects.equals(it.type, type)), "operateType 不合法");
        }
    }
}
