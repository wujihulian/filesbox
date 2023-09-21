package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.svnlan.annotation.CreateGroup;
import com.svnlan.annotation.UpdateGroup;
import com.svnlan.common.Result;
import com.svnlan.user.domain.Notice;
import com.svnlan.user.dto.NoticeDTO;
import com.svnlan.user.service.NoticeService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知管理
 *
 * @author lingxu 2023/05/16 15:08
 */
@RestController
@RequestMapping("api/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 分页查询通知列表
     */
    @GetMapping("list/page")
    public Result listPage(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        IPage<Notice> page = noticeService.page(
                new Page<>(currentPage, pageSize),
                new LambdaQueryWrapper<Notice>()
                        .select(Notice::getId, Notice::getSendTime, Notice::getStatus, Notice::getTitle)
                        .in(Notice::getStatus, 0, 1)
                        .orderByDesc(Notice::getSort, Notice::getCreateTime)
        );
        return Result.returnSuccess(new JSONObject()
                .fluentPut("list", page.getRecords())
                .fluentPut("total", page.getTotal()));
    }

    /**
     * 通知详情
     *
     * @param id 通知id
     */
    @GetMapping("detail")
    public Result detail(@RequestParam("id") Long id) {
        return Result.returnSuccess(noticeService.detail(id));
    }

    /**
     * 通知预览
     *
     * @param id 通知id
     */
    @GetMapping("preview")
    public Result preview(@RequestParam("id") Long id) {
        List<Long> ids = Lists.asList(id, new Long[]{});
        List<JSONObject> previewList = noticeService.preview(ids);
        if (CollectionUtils.isEmpty(previewList)) {
            return Result.returnSuccess();
        }else {
            return Result.returnSuccess(previewList.get(0));
        }
    }

    /**
     * 新增通知消息
     */
    @PostMapping("add")
    public Result addNotice(@RequestBody @Validated(CreateGroup.class) NoticeDTO dto) {
        noticeService.addNotice(dto);
        return Result.returnSuccess();
    }

    /**
     * 编辑通知消息
     */
    @PostMapping("edit")
    public Result editNotice(@RequestBody @Validated(UpdateGroup.class) NoticeDTO dto) {
        noticeService.editNotice(dto);
        return Result.returnSuccess();
    }

    /**
     * 设置通知是否启用 如果启用 则禁用  禁用了 就启用
     */
    @RequestMapping("enable")
    public Result operateEnable(@RequestParam("id") Long id) {
        noticeService.operateEnable(id);
        return Result.returnSuccess();
    }

    /**
     * 删除通知消息
     */
    @RequestMapping("delete")
    public Result delete(@RequestParam("id") Long id) {
        noticeService.delete(id);
        return Result.returnSuccess();
    }
}
