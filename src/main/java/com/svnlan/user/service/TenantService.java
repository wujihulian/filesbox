package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.svnlan.user.domain.Tenant;
import com.svnlan.user.dto.TenantDTO;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.List;

/**
* @author zhanghanyi
* @description 针对表【t_tenant】的数据库操作Service
* @createDate 2023-08-26 10:47:33
*/
public interface TenantService extends IService<Tenant> {

    Pair<List<JSONObject>, Long> listPage(Integer currentPage, Integer pageSize);

    JSONObject detail(Long id);

    void createOrUpdate(TenantDTO dto);

    void deleteTenant(Long id);

    void clearTenant(Long id);

    void clearDiskResourceByTenantId(Long tenantId) throws IOException;
}
