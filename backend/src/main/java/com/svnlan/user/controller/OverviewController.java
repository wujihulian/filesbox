package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.user.service.*;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.VisitRecordExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 概览
 *
 * @author lingxu 2023/04/06 15:12
 */
@RestController
@RequestMapping("api/overview")
public class OverviewController {

    @Resource
    private VisitRecordExecutor visitRecordExecutor;
    @Resource
    private OverviewService overviewService;
    @Resource
    private SystemLogService systemLogService;
    @Resource
    private UserService userService;
    @Resource
    private GroupService groupService;
    @Resource
    private IoSourceService ioSourceService;

    @Resource
    private StorageService storageService;
    /**
     * 获取用户和设备访问次数数据
     */
    @GetMapping("visitRecord/list")
    public Result getDeviceAndUserVisitRecord() {
        // 获取用户 14天前的访问次数数据
//        List<?> userVisitRecordList = hyperLogLogExecutor.getVisitRecordList(14, HyperLogLogExecutor.DataType.USER_VISIT);
        // 获取设备 30天前访问次数数据
        List<?> deviceVisitRecordList = visitRecordExecutor.getVisitRecordList(30, VisitRecordExecutor.DataType.DEVICE_VISIT);
        // 累计访问量
        Object cumulativeVisitTotal = overviewService.getCumulativeVisitTotal();
        // 查询今日用户登录数
        Long loginUserCountToday = systemLogService.getLoginUserCountToday();
        // 用户总数，条件是非管理员，可用的用户
        Long total = userService.selectUserCount();
        List<Long> loginCountList = systemLogService.getLoginUserCountDaysBefore(14);
        Date endDate = DateUtil.getPlusDays(DateUtil.yyyy_MM_dd, new Date(), -1);
        Date startDate = DateUtil.getPlusDays(endDate, -13);
        return Result.returnSuccess(
                new JSONObject()
                        .fluentPut("loginCountToday", loginUserCountToday)
                        .fluentPut("visitTotal", cumulativeVisitTotal)
//                        .fluentPut("userVisitRecord", userVisitRecordList)
                        .fluentPut("deviceVisitRecord", deviceVisitRecordList)
                        .fluentPut("userTotal", total)
                        .fluentPut("loginCountList", loginCountList)
                        .fluentPut("loginDateList", DateUtil.getDays(startDate, endDate))
        );
    }

    /**
     * 获取磁盘及文件总揽信息
     */
    @GetMapping("diskInfo/overview")
    public Result getDiskSpaceInfo() {
        // 空间
        // 用户使用的空间总和
        Long userUsedTotal = userService.sumUserSpaceUsed();
        // 组织机构使用的空间总和
        Long groupUsedTotal = groupService.sumGroupSpaceUsed();
        // 总使用数
        Long totalSpaceUsed = userUsedTotal + groupUsedTotal;
        // 文件
        JSONObject jsonObject = ioSourceService.fileInfoOverview();
        Long totalSpace = storageService.getTotalSpace();
        return Result.returnSuccess(
                jsonObject.fluentPut("totalSpace", totalSpace)
                        .fluentPut("spaceUsed", totalSpaceUsed)
        );
    }

    /**
     * 获取文件类型及终端类型占比
     */
    @GetMapping("fileAndDevice/proportion")
    public Result getFileTypeAndDeviceTypeProportion() {
        List<JSONObject> jsonList = ioSourceService.getFileTypeProportion();
        List<JSONObject> osNameRecordList = visitRecordExecutor.getOsNameRecordList();

        return Result.returnSuccess(
                new JSONObject().fluentPut("fileTypeProportion", jsonList)
                        .fluentPut("osNameRecordList", osNameRecordList)
        );
    }

    /**
     * 文件排行和占比
     */
    @GetMapping("ranking/proportion")
    public Result getFileRankingAndProportion(@RequestParam(defaultValue = "10") Integer topCount) {
        return Result.returnSuccess(overviewService.getFileRankingAndProportion(topCount));
    }


    /**
     * 添加测试的 访问数据
     */
    @GetMapping("test1")
    public Result test1(Integer daysBefore) {
        visitRecordExecutor.test1(daysBefore);
        return Result.returnSuccess();
    }

    /**
     * 测试数据合并
     */
    @GetMapping("test2")
    public Result test2(Integer daysBefore) {
        visitRecordExecutor.test2(daysBefore);
        return Result.returnSuccess();
    }
}
