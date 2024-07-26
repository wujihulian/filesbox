package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.enums.DocumentTypeEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.VisitCountRecordDao;
import com.svnlan.user.service.OverviewService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.TenantUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.svnlan.utils.VisitRecordExecutor.VISIT_TOTAL_COUNT_KEY;

/**
 * 概览 服务实现
 *
 * @author lingxu 2023/04/10 13:10
 */
@Slf4j
@Service
public class OverviewServiceImpl implements OverviewService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private VisitCountRecordDao visitCountRecordDao;

    @Resource
    private IoSourceDao ioSourceDao;

    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private UserDao userDao;

    @Resource
    private GroupDao groupDaoImpl;
    @Resource
    OptionTool optionTool;


    /**
     * 获取总访问量
     */
    @Override
    public Object getCumulativeVisitTotal() {
        Object total = redisTemplate.opsForValue().get(VISIT_TOTAL_COUNT_KEY);
        if (Objects.isNull(total) || Objects.equals(total, 0)) {
            total = Optional.ofNullable(visitCountRecordDao.getCumulativeVisitTotal()).orElse(0L);
            redisTemplate.opsForValue().increment(VISIT_TOTAL_COUNT_KEY, ((Long) total));
        }
        return total;
    }

    @Resource
    private TenantUtil tenantUtil;


    @SneakyThrows
    @Override
    public Object getFileRankingAndProportion(Integer topCount) {
        Long tenantId = tenantUtil.getTenantIdByServerName();
        Long diskDefaultSize = optionTool.getTotalSpace();
        // 获取文件占比数据
        CompletableFuture<List<JSONObject>> fileProportionFuture = CompletableFuture.supplyAsync(() -> queryFileProportion(tenantId), asyncTaskExecutor);
        CompletableFuture<List<JSONObject>> UserSpaceFuture = CompletableFuture.supplyAsync(() -> queryUserSpaceFileUsageRankList(topCount, tenantId), asyncTaskExecutor);
        CompletableFuture<List<JSONObject>> groupSpaceFuture = CompletableFuture.supplyAsync(() -> getGroupSpaceFileUsageList(tenantId), asyncTaskExecutor);
        CompletableFuture<List<JSONObject>> targetTypeProportionFuture = CompletableFuture.supplyAsync(() -> getTargetTypeProportion(tenantId), asyncTaskExecutor);
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(fileProportionFuture, UserSpaceFuture, groupSpaceFuture, targetTypeProportionFuture);
        // 设置 20s 超时
        allFuture.get(20, TimeUnit.SECONDS);
        if (allFuture.isDone()) {
            List<JSONObject> fileProportionList = fileProportionFuture.get();
            List<JSONObject> userSpaceList = UserSpaceFuture.get();
            List<JSONObject> groupSpaceList = groupSpaceFuture.get();
            List<JSONObject> targetTypeProportionList = targetTypeProportionFuture.get();
            return new JSONObject()
                    .fluentPut("fpList", fileProportionList)
                    .fluentPut("usList", userSpaceList)
                    .fluentPut("gsList", groupSpaceList)
                    .fluentPut("tyList", targetTypeProportionList)
                    .fluentPut("spaceCapacity", diskDefaultSize * 1024 * 1024 * 1024);
        } else {
            throw new SvnlanRuntimeException("20s timeout for query");
        }
    }

    public static final String FILE_EXT_PROPORTION = "FILE_EXT_PROPORTION";
    public static final String USER_SPACE_RANK = "USER_SPACE_RANK";
    public static final String GROUP_SPACE_PROPORTION = "GROUP_SPACE_PROPORTION";
    public static final String FILE_TYPE_PROPORTION = "FILE_TYPE_PROPORTION";

    /**
     * 查询文件类型占比
     */
    private List<JSONObject> queryFileProportion(Long tenantId) {
        List<JSONObject> jsonObjList = CaffeineUtil.FILE_EXT_PROPORTION_CACHE.getIfPresent(FILE_EXT_PROPORTION);
        if (CollectionUtils.isEmpty(jsonObjList)) {
            jsonObjList = ioSourceDao.selectFileProportion(tenantId);
            for (JSONObject jsonObj : jsonObjList) {
                jsonObj.put("t", DocumentTypeEnum.getTypeByExt(jsonObj.getString("ft")));
            }
            CaffeineUtil.FILE_EXT_PROPORTION_CACHE.put(FILE_EXT_PROPORTION, jsonObjList);
        }
        return jsonObjList;

    }

    /**
     * 获取用户个人中心文件占用排名
     */
    private List<JSONObject> queryUserSpaceFileUsageRankList(Integer topCount, Long tenantId) {
        List<JSONObject> jsonObjList = CaffeineUtil.USER_SPACE_RANK_CACHE.getIfPresent(USER_SPACE_RANK);
        if (CollectionUtils.isEmpty(jsonObjList)) {
            jsonObjList = userDao.getUserSpaceRank(topCount, tenantId);
            CaffeineUtil.USER_SPACE_RANK_CACHE.put(USER_SPACE_RANK, jsonObjList);
        }
        return jsonObjList;
    }

    /**
     * 查询群组文件占比
     */
    private List<JSONObject> getGroupSpaceFileUsageList(Long tenantId) {
        List<JSONObject> jsonObjList = CaffeineUtil.GROUP_SPACE_PROPORTION_CACHE.getIfPresent(GROUP_SPACE_PROPORTION);
        if (CollectionUtils.isEmpty(jsonObjList)) {
            jsonObjList = groupDaoImpl.selectGroupSpaceUsage(tenantId);
            CaffeineUtil.GROUP_SPACE_PROPORTION_CACHE.put(GROUP_SPACE_PROPORTION, jsonObjList);
        }
        return jsonObjList;
    }

    /**
     * 查询文件targetType 类型占比
     * 0-sys,1-user,2-group 3-资源
     */
    private List<JSONObject> getTargetTypeProportion(Long tenantId) {
        List<JSONObject> jsonObjList = CaffeineUtil.FILE_TYPE_PROPORTION_CACHE.getIfPresent(FILE_TYPE_PROPORTION);
        if (CollectionUtils.isEmpty(jsonObjList)) {
            jsonObjList = ioSourceDao.getTargetTypeProportion(tenantId);
            CaffeineUtil.FILE_TYPE_PROPORTION_CACHE.put(FILE_TYPE_PROPORTION, jsonObjList);
        }
        return jsonObjList;
    }
}
