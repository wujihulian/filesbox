package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.dto.TenantDTO;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
* @author zhanghanyi
* @description 针对表【t_tenant】的数据库操作Service
* @createDate 2023-08-26 10:47:33
*/
public interface TenantService {

    Pair<List<JSONObject>, Long> listPage(Integer currentPage, Integer pageSize);

    JSONObject detail(Long id);

    void createOrUpdate(TenantDTO dto);

    void deleteTenant(Long id);

    void clearTenant(Long id);

    void clearDiskResourceByTenantId(Long tenantId) throws IOException;

    public Long initDataWhenTenantEstablished(Function<Integer, Long> func, Long tenantId);
}
