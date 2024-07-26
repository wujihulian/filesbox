package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.user.domain.Tenant;
import com.svnlan.user.dto.TenantDTO;
import com.svnlan.user.service.TenantService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户管理
 */
@RestController
@RequestMapping("/api/tenant/admin")
public class TenantController {
    @Resource
    private TenantService tenantService;

    /**
     * 租户列表
     */
    @GetMapping("/list/page")
    public Result listPage(@RequestParam(value = "currentPage", defaultValue = "1", required = false) Integer currentPage,
                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        Pair<List<JSONObject>, Long> pair = tenantService.listPage(currentPage, pageSize);
        return Result.returnSuccess(new JSONObject().fluentPut("list", pair.getFirst()).fluentPut("total", pair.getSecond()));
    }

    /**
     * 租户详情
     */
    @GetMapping("/detail")
    public Result detail(Long id) {
        JSONObject jsonObj = tenantService.detail(id);
        return Result.returnSuccess(jsonObj);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/createOrUpdate")
    public Result createOrUpdate(@RequestBody TenantDTO dto) {
        tenantService.createOrUpdate(dto);
        return Result.returnSuccess();
    }

    /**
     * 删除租户信息
     */
    @DeleteMapping
    public Result deleteTenant(Long id) {
        tenantService.deleteTenant(id);
        return Result.returnSuccess();
    }

    /**
     * 清除租户信息
     */
    @DeleteMapping("clear")
    public Result removeTenant(Long id) {
        tenantService.clearTenant(id);
        return Result.returnSuccess();
    }
}
