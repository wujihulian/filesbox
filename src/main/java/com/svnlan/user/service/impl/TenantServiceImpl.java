package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.TenantDao;
import com.svnlan.user.domain.Tenant;
import com.svnlan.user.dto.StorageDTO;
import com.svnlan.user.dto.TenantDTO;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.service.TenantService;
import com.svnlan.utils.AESUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanghanyi
 * @description 针对表【t_tenant】的数据库操作Service实现
 * @createDate 2023-08-26 10:47:33
 */
@Slf4j
@Service
public class TenantServiceImpl extends ServiceImpl<TenantDao, Tenant> implements TenantService {

    @Resource
    private TenantDao tenantDao;

    @Resource
    private StorageService storageService;

    @Value("${webdav.rootFolder}")
    private String rootFolder;
    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;
    @Resource
    private SystemLogTool systemLogTool;
    @Resource
    private LoginUserUtil loginUserUtil;


    public Pair<List<JSONObject>, Long> listPage(Integer currentPage, Integer pageSize) {
        Integer startIndex = (currentPage - 1) * pageSize;
        Long total = tenantDao.selectTenantListCount();
        if (Objects.nonNull(total) && total > 0) {
            List<JSONObject> list = tenantDao.selectTenantList(startIndex, pageSize);
            return Pair.of(list, total);
        }
        return Pair.of(Collections.emptyList(), 0L);
    }

    @Override
    public JSONObject detail(Long id) {
        return tenantDao.getById(id);
    }

    @Override
    public void createOrUpdate(TenantDTO dto) {
        if (StringUtils.hasText(dto.getPassword()) && Objects.nonNull(dto.getId())) {
            // 表示是修改密码的
            // 校验是否存在
            Long userId = tenantDao.checkIfAdminUserExist(dto.getId());
            Assert.notNull(userId, "管理员不存在");
            // 真实的密码
            String realPassword = AESUtil.decrypt(dto.getPassword(), GlobalConfig.LOGIN_PASSWORD_AES_KEY, true);
            // 加密后的密码
            String cryptoPassword = PasswordUtil.passwordEncode(realPassword, GlobalConfig.PWD_SALT);
            Assert.isTrue(tenantDao.updateAdminUserPassword(userId, cryptoPassword) == 1, "密码修改失败");
            return;
        }
        Tenant tenant = new Tenant();
        tenant.populateData(dto);
        if (Objects.nonNull(dto.getId())) {
            Tenant originTenant = getById(dto.getId());
            Assert.notNull(originTenant, "未查询到该租户信息");
            Assert.isTrue(Objects.equals(originTenant.getDr(), 0), "已经被删除了");
            // 更新
            tenantDao.updateById(tenant);
        } else {
            // 新增
            // TODO 新增管理员 等
            // 判断二级域名是否存在
            Long id = tenantDao.checkDomainExist(tenant.getSecondLevelDomain());
            Assert.isNull(id, "该二级域名已经存在");
            tenant.setCreateTime(LocalDateTime.now());
            ObjUtil.initializefield(tenant);
            tenantDao.insert(tenant);
        }

    }

    @Override
    public void deleteTenant(Long id) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setDr(1);
        Assert.isTrue(tenantDao.updateDrById(id) == 1, "租户删除失败");
    }

    @Override
    public void clearTenant(Long id) {
        // 该租户是否为删除的状态
        Tenant tenant = getById(id);
        Assert.notNull(tenant, "未查询租户信息");
        Assert.isTrue(Objects.equals(tenant.getDr(), 1), "在删除的状态下才能清除数据");
        Assert.isTrue(tenantDao.deleteById(id) == 1, "删除租户失败");

        asyncTaskExecutor.execute(() -> {
            try {
                // 清除磁盘上的资源
                clearDiskResourceByTenantId(id);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        });
        // 日志
        log.info("写操作日志 开始");
        LoginUser loginUser = loginUserUtil.getLoginUser();
        /** 操作日志 */
        List<Map<String, Object>> paramList = new ArrayList<>();
        Map<String, Object> reMap = new HashMap<>(4);
        reMap.put("tenantId", id);
        reMap.put("operateTime", LocalDateTime.now());
        reMap.put("type", "clearTenant");
        reMap.put("operatorId", loginUser.getUserID());
        paramList.add(reMap);

        systemLogTool.setSysLog(loginUser, LogTypeEnum.clearTenant.getCode(), paramList, systemLogTool.getRequest());
    }

    @Override
    public void clearDiskResourceByTenantId(Long tenantId) throws IOException {
        log.info("clearDiskResourceByTenantId 开始");
        // 查询出所有的磁盘location
        List<StorageDTO> list = storageService.list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Path> pathList = list.stream().map(it -> Paths.get(rootFolder, it.getLocation(), String.valueOf(tenantId))).collect(Collectors.toList());
        for (Path path : pathList) {
            // 存在的话，则需要删除
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
