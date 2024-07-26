package com.svnlan.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.svnlan.common.Result;
import com.svnlan.user.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * C 端 通知
 *
 * @author lingxu 2023/05/20 10:54
 */
@Slf4j
@RestController("noticeWebController")
@RequestMapping("api/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    /**
     * C端查询通知消息列表
     */
    @GetMapping("read/list/page")
    public Result noticeList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        JSONObject jsonObject = noticeService.pageList(currentPage, pageSize);
        return Result.returnSuccess(jsonObject);
    }


    /**
     * C端查询未读消息数
     */
    @GetMapping("unread")
    public Result hasNoticeUnread() {
        return Result.returnSuccess(noticeService.hasNoticeUnread());
    }

    /**
     * C端消息信息
     */
    @GetMapping("info")
    public Result noticeInfo(Long id) {

        List<Long> ids = Lists.asList(id, new Long[]{});
        List<JSONObject> previewList = noticeService.preview(ids);
        if (!CollectionUtils.isEmpty(previewList)) {
            JSONObject jsonObject = previewList.get(0);
            if (Objects.equals(jsonObject.getInteger("status"), 1)) {
                noticeService.executeNoticeRead(id);
                return Result.returnSuccess(previewList.get(0));
            }
        }
        return Result.returnSuccess();
    }

}
