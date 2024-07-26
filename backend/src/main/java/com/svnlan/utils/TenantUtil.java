package com.svnlan.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.TenantDao;
import net.bytebuddy.implementation.bytecode.Throw;
import org.jooq.DSLContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.svnlan.jooq.Tables.SYSTEM_OPTION;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/16 11:33
 */
@Component
public class TenantUtil {
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    TenantDao tenantDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private DSLContext context;


    public Long getTenantIdByServerName(){
        return getTenantIdByServerName(null);
    }

    public Long getTenantIdByRequestServerName(HttpServletRequest request){
        String serverName = loginUserUtil.getServerName(request);
        return getTenantIdByServerName(serverName);
    }
    public Long getTenantIdByServerName(String serverName){
        Long tenantId = 0L;
        if (ObjectUtils.isEmpty(serverName)){
            try {
                serverName = loginUserUtil.getServerName();
            }catch (Exception e){
                return 1L;
            }
        }
        String key = GlobalConfig.tenantIdRedisKey + serverName;
        String tenantIdStr = stringRedisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(tenantIdStr) && StringUtil.isNumeric(tenantIdStr)){
            tenantId = Long.valueOf(tenantIdStr);
        }
        if (tenantId > 0){
            return tenantId;
        }
        // tenantTypeConfig  1 单租户模式 2 平台模式
        String tenantType = context.select(SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TYPE.eq("").and(SYSTEM_OPTION.KEY_STRING.eq("tenantTypeConfig")))
                .fetchOne(SYSTEM_OPTION.VALUE_TEXT);
        if ("1".equals(tenantType)){
            tenantId = 1L;
        }else {
            tenantId = tenantDao.getTenantIdByServerName(serverName);
            if (ObjectUtils.isEmpty(tenantId)){
                tenantId = 1L;
            }
        }

        LogUtil.info("getTenantIdByServerName serverName=" + serverName + "，tenantType= " + tenantType + "，tenantId=" + tenantId);
        stringRedisTemplate.opsForValue().set(key, tenantId+"", 12, TimeUnit.HOURS);
        return tenantId;
    }

    public void checkAuthTenantSystem() {
        Long tenantId = getTenantIdByServerName();
        if (tenantId.longValue() != 1){
            throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
        }
    }

    public Long getTenantIdByUserId(Long userId){
        return tenantDao.selectTenantIdByUserId(userId);
    }

    public Long getTenantIdBySourceId(Long sourceId){
        return tenantDao.selectTenantIdBySourceId(sourceId);
    }
}
