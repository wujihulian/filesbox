package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jooq.tables.records.GroupMetaRecord;
import com.svnlan.jooq.tables.records.SystemOptionRecord;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.Impl.SystemOptionDaoImpl;
import com.svnlan.user.dao.TenantDao;
import com.svnlan.user.domain.Tenant;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.StorageDTO;
import com.svnlan.user.dto.TenantDTO;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.service.TenantService;
import com.svnlan.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.svnlan.common.GlobalConfig.DIVIDING_LINE;
import static com.svnlan.jooq.Tables.*;
import static com.svnlan.jooq.Tables.SYSTEM_OPTION;
import static com.svnlan.jooq.tables.Groups.GROUPS;
import static com.svnlan.jooq.tables.Role.ROLE;


/**
 * @author zhanghanyi
 * @description 针对表【t_tenant】的数据库操作Service实现
 * @createDate 2023-08-26 10:47:33
 */
@Slf4j
@Service
public class TenantServiceImpl implements TenantService {

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
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    SystemOptionDaoImpl systemOptionDaoImpl;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private DSLContext context;

    @Override
    public Pair<List<JSONObject>, Long> listPage(Integer currentPage, Integer pageSize) {
        // 判断是否是平台租户权限
        tenantUtil.checkAuthTenantSystem();

        Integer startIndex = (currentPage - 1) * pageSize;
        Long total = tenantDao.selectTenantListCount();
        if (Objects.nonNull(total) && total > 0) {
            List<JSONObject> list = tenantDao.selectTenantList(startIndex, pageSize);
            checkIfExpired(list);
            return Pair.of(list, total);
        }
        return Pair.of(Collections.emptyList(), 0L);
    }

    private void checkIfExpired(List<JSONObject> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<Long> needUpdateIds = new ArrayList<>();
        for (JSONObject jsonObj : list) {
            if (Objects.equals(jsonObj.getInteger("status"), 3)) {
                continue;
            }
            LocalDateTime expireTime = jsonObj.getObject("expireTime", LocalDateTime.class);
            if (expireTime.isBefore(now)) {
                // 表示已过期
                jsonObj.fluentPut("status", 4);
                needUpdateIds.add(jsonObj.getLong("id"));
            }
        }

        if (!CollectionUtils.isEmpty(needUpdateIds)) {
            asyncTaskExecutor.execute(() -> {
                context.update(T_TENANT).set(T_TENANT.STATUS, 3)
                        .where(T_TENANT.ID.in(needUpdateIds))
                        .and(T_TENANT.DR.eq(0));
            });
        }
    }

    @Override
    public JSONObject detail(Long id) {
        return tenantDao.getById(id);
    }

    @SneakyThrows
    @Override
    public void createOrUpdate(TenantDTO dto) {
        // 判断是否是平台租户权限
        tenantUtil.checkAuthTenantSystem();

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
            // 如果需要更改域名，则也需要判断
            if (StringUtils.hasText(dto.getSecondLevelDomain())) {
                List<Long> ids = tenantDao.checkDomainExist(dto.getSecondLevelDomain());
                if (!CollectionUtils.isEmpty(ids)) {
                    if (ids.size() == 1 && Objects.equals(dto.getId(), ids.get(0))) {
                        // 表示是它本身
                    } else {
                        throw new SvnlanRuntimeException("该二级域名已经存在");
                    }
                } else {
                    // 这种情况下， 说明是在创建的时候， 未设置二级域名
                }
            }
            JSONObject originTenant = tenantDao.getById(dto.getId());
            Assert.notNull(originTenant, "未查询到该租户信息");
            Assert.isTrue(Objects.equals(originTenant.getInteger("dr"), 0), "已经被删除了");

            // 更新
            tenantDao.updateById(tenant);
            // 更新缓存
            stringRedisTemplate.delete(GlobalConfig.tenantIdRedisKey + originTenant.getString("secondLevelDomain"));
            stringRedisTemplate.delete(GlobalConfig.tenantIdRedisKey + dto.getSecondLevelDomain());
        } else {
            // 新增
            // 判断二级域名是否存在
            Assert.hasText(dto.getSecondLevelDomain(), "二级域名不能为空");
            List<Long> ids = tenantDao.checkDomainExist(tenant.getSecondLevelDomain());
            Assert.isTrue(CollectionUtils.isEmpty(ids), "该二级域名已经存在");
            Assert.hasText(dto.getAdminUserName(), "用户名不能为空");
            // 判断用户名是否存在
            List<Long> userIds = context.select(USERS.ID).from(USERS).where(USERS.NAME.eq(dto.getAdminUserName())).fetch(USERS.ID);
            Assert.isTrue(CollectionUtils.isEmpty(userIds), "该用户名已存在");

            LocalDateTime now = LocalDateTime.now();
            tenant.setCreateTime(now);
            tenant.setModifyTime(now);
            ObjUtil.initializefield(tenant);
            tenant.setUserId(0L);
            tenantDao.insert(tenant);
            if (ObjectUtils.isEmpty(tenant.getId())){
                throw new SvnlanRuntimeException("创建新租户失败");
            }

            Long tenantId = tenant.getId();
            dto.setId(tenantId);
            dto.setIsSystem(1);
            // 创建GROUP等相关
            Long userId = this.initDataWhenTenantEstablished(it -> insertUser(it, dto), tenantId);
            if (Objects.equals(-1L, userId)) {
                // 表示上个步骤出错了
                throw new SvnlanRuntimeException("创建新租户失败！");
            }
            tenant.setUserId(userId);
            tenantDao.updateTenantUserById(tenant);

            // 添加默认配置
            if (userId > 0) {
                LoginUser loginUser = loginUserUtil.getLoginUser();
                sourceOperateTool.addDefaultSetting(userId, tenantId);
                // 默认创建目录
                String folders = systemOptionDaoImpl.getSystemConfigByKey("newUserFolder");
                // 创建用户默认source

                User user = new User();
                user.setUserID(userId);
                user.setName(dto.getAdminUserName());
                user.setTenantId(tenantId);
                sourceOperateTool.setUserDefaultSource(user, loginUser.getUserID(), folders);
            }
        }

    }

    @Override
    public void deleteTenant(Long id) {
        // 判断是否是平台租户权限
        tenantUtil.checkAuthTenantSystem();

        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setStatus(2);
        Assert.isTrue(tenantDao.updateDrById(id) == 1, "租户删除失败");
    }

    @Override
    public void clearTenant(Long id) {
        // 判断是否是平台租户权限
        tenantUtil.checkAuthTenantSystem();

        // 该租户是否为删除的状态
        JSONObject tenant = tenantDao.getById(id);
        Assert.notNull(tenant, "未查询租户信息");
        Assert.isTrue(Objects.equals(tenant.getInteger("status"), 2), "在删除的状态下才能清除数据");

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

        // 判断是否是平台租户权限
        tenantUtil.checkAuthTenantSystem();

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


    private Long insertUser(Integer roleId, TenantDTO tenantDTO) {

        String realPassword = StringUtils.hasText(tenantDTO.getPassword()) ? tenantDTO.getPassword() : "Wujihl12!@";
        try {
            // 解码
            realPassword = AESUtil.decrypt(tenantDTO.getPassword(), GlobalConfig.LOGIN_PASSWORD_AES_KEY, true);
        } catch (Exception e) {
            // ignore
        }
        LocalDateTime now = LocalDateTime.now();
        return context.insertInto(USERS)
                .set(USERS.NAME, tenantDTO.getAdminUserName())
                .set(USERS.ROLE_ID, roleId)
                .set(USERS.AVATAR, "")
                .set(USERS.PASSWORD, PasswordUtil.passwordEncode(realPassword, GlobalConfig.PWD_SALT))
                .set(USERS.NICKNAME, "系统管理员")
                .set(USERS.PHONE, "")
                .set(USERS.EMAIL, "")
                .set(USERS.SIZE_MAX, 2d)
                .set(USERS.SIZE_USE, 0L)
                .set(USERS.STATUS, 1)
                .set(USERS.LAST_LOGIN, now)
                .set(USERS.CREATE_TIME, now)
                .set(USERS.MODIFY_TIME, now)
                .set(USERS.SEX, 1)
                .set(USERS.TENANT_ID, tenantDTO.getId())
                .set(USERS.IS_SYSTEM, ObjectUtils.isEmpty(tenantDTO.getIsSystem()) ? 0 : tenantDTO.getIsSystem())
                .returning(USERS.ID).fetchOne().getId();

    }

    /**
     * 当租户建立时，需要初始化的数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long initDataWhenTenantEstablished(Function<Integer, Long> func, Long tenantId) {
        LocalDateTime now = LocalDateTime.now();
        try {
            // 初始化 GROUP 数据
            Long groupId = context.insertInto(GROUPS)
                    .set(GROUPS.NAME, "企业网盘")
                    .set(GROUPS.PARENT_ID, 0L)
                    .set(GROUPS.PARENT_LEVEL, ",0,")
                    .set(GROUPS.SORT, 1)
                    .set(GROUPS.SIZE_MAX, 0d)
                    .set(GROUPS.SIZE_USE, 0L)
                    .set(GROUPS.MODIFY_TIME, now)
                    .set(GROUPS.CREATE_TIME, now)
                    .set(GROUPS.STATUS, 1)
                    .set(GROUPS.TENANT_ID, tenantId)
                    .returning(GROUPS.ID).fetchOne().getId();

            List<Integer> roleIdList = new ArrayList<>();
            // 初始化 ROLE 数据
            resolveInitialData("ROLE",
                    it -> {
                        Integer roleId = context.insertInto(ROLE)
                                .set(ROLE.ROLE_NAME, it[0])
                                .set(ROLE.CODE, it[1])
                                .set(ROLE.DESCRIPTION, it[2])
                                .set(ROLE.LABEL, it[3])
                                .set(ROLE.AUTH, it[4])
                                .set(ROLE.CREATE_TIME, now)
                                .set(ROLE.MODIFY_TIME, now)
                                .set(ROLE.DELETE_TIME, now)
                                .set(ROLE.STATUS, Integer.valueOf(it[8]))
                                .set(ROLE.ENABLE, Integer.valueOf(it[9]))
                                .set(ROLE.IS_SYSTEM, Integer.valueOf(it[10]))
                                .set(ROLE.ADMINISTRATOR, Integer.valueOf(it[11]))
                                .set(ROLE.IGNORE_FILE_SIZE, Double.valueOf(StringUtils.hasText(it[12]) ? it[12] : "0d"))
                                .set(ROLE.IGNORE_EXT, it[13])
                                .set(ROLE.SORT, Integer.valueOf(it[14]))
                                .set(ROLE.ROLE_TYPE, it[15])
                                .set(ROLE.TENANT_ID, tenantId)
                                .returning(ROLE.ID).fetchOne().getId();
                        roleIdList.add(roleId);
                    }, null);

            // 初始化 GROUP_META 数据
            resolveInitialData("GROUP_META", null, it -> {
                InsertQuery<GroupMetaRecord> insertQuery = context.insertQuery(GROUP_META);
                for (String[] item : it) {
                    insertQuery.newRecord();
                    String k = item[0];
                    insertQuery.addValue(GROUP_META.GROUP_ID, groupId);
                    insertQuery.addValue(GROUP_META.KEY_STRING, k);
                    if ("systemGroupSource".equals(k)){
                        // 初始化默认（标记此记录不可删除）
                        insertQuery.addValue(GROUP_META.VALUE_TEXT, groupId+"");
                    }else {
                        insertQuery.addValue(GROUP_META.VALUE_TEXT, item[1]);
                    }
                    insertQuery.addValue(GROUP_META.CREATE_TIME, now);
                    insertQuery.addValue(GROUP_META.MODIFY_TIME, now);
                    insertQuery.addValue(GROUP_META.TENANT_ID, tenantId);
                }

                insertQuery.execute();
            });

            // 初始化 IO_SOURCE
            Long sourceId = context.insertInto(IO_SOURCE)
                    .set(IO_SOURCE.SOURCE_HASH, "")
                    .set(IO_SOURCE.TARGET_TYPE, 2)
                    .set(IO_SOURCE.TARGET_ID, 1L)
                    .set(IO_SOURCE.CREATE_USER, 0L)
                    .set(IO_SOURCE.MODIFY_USER, 0L)
                    .set(IO_SOURCE.IS_FOLDER, 1)
                    .set(IO_SOURCE.NAME, "企业网盘")
                    .set(IO_SOURCE.FILE_TYPE, "")
                    .set(IO_SOURCE.PARENT_ID, 0L)
                    .set(IO_SOURCE.PARENT_LEVEL, ",0,")
                    .set(IO_SOURCE.FILE_ID, 0L)
                    .set(IO_SOURCE.IS_DELETE, 0)
                    .set(IO_SOURCE.SIZE, 0L)
                    .set(IO_SOURCE.CREATE_TIME, now)
                    .set(IO_SOURCE.MODIFY_TIME, now)
                    .set(IO_SOURCE.VIEW_TIME, now)
                    .set(IO_SOURCE.TENANT_ID, tenantId)
                    .returning(IO_SOURCE.ID).fetchOne().getId();

            // 初始化 GROUP_SOURCE
            context.insertInto(GROUP_SOURCE)
                    .set(GROUP_SOURCE.GROUP_ID, groupId)
                    .set(GROUP_SOURCE.SOURCE_ID, sourceId)
                    .set(GROUP_SOURCE.CREATE_TIME, now)
                    .set(GROUP_SOURCE.TENANT_ID, tenantId)
                    .execute();

            // 初始化 SYSTEM_OPTION 数据
            resolveInitialData("SYSTEM_OPTION", null, it -> {
                InsertQuery<SystemOptionRecord> insertQuery = context.insertQuery(SYSTEM_OPTION);
                for (String[] item : it) {
                    String type = item[0];
                    String value = item[2];
                    insertQuery.newRecord();
                    insertQuery.addValue(SYSTEM_OPTION.TYPE, type.trim());
                    insertQuery.addValue(SYSTEM_OPTION.KEY_STRING, item[1]);
                    insertQuery.addValue(SYSTEM_OPTION.VALUE_TEXT, value.trim());
                    insertQuery.addValue(SYSTEM_OPTION.CREATE_TIME, now);
                    insertQuery.addValue(SYSTEM_OPTION.MODIFY_TIME, now);
                    insertQuery.addValue(SYSTEM_OPTION.TENANT_ID, tenantId);
                }
                insertQuery.execute();
            });
            Long userId = func.apply(roleIdList.get(0));
            // 初始化 USER_GROUP 数据
            context.insertInto(USER_GROUP)
                    .set(USER_GROUP.USER_ID, userId)
                    .set(USER_GROUP.GROUP_ID, groupId)
                    .set(USER_GROUP.AUTH_ID, 4)
                    .set(USER_GROUP.SORT, 0)
                    .set(USER_GROUP.CREATE_TIME, now)
                    .set(USER_GROUP.MODIFY_TIME, now)
                    .set(USER_GROUP.TENANT_ID, tenantId)
                    .execute();

            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return -1L;
    }

    @SneakyThrows
    private void resolveInitialData(String tableName, Consumer<String[]> consumer, Consumer<List<String[]>> listConsumer) {
        ClassPathResource classPathResource = new ClassPathResource(GlobalConfig.INITIAL_DATA);
        InputStream inputStream = classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean isMatch = false;
        List<String[]> list = null;
        if (Objects.nonNull(listConsumer)) {
            list = new ArrayList<>();
        }
        for (; ; ) {
            line = br.readLine();
            if (Objects.isNull(line)) {
                break;
            }
            if (line.startsWith(DIVIDING_LINE)) {
                // 表示到了分割线
                if (Objects.equals(DIVIDING_LINE + "||" + tableName, line)) {
                    isMatch = true;
                    // 表示解析开始
                } else {
                    if (isMatch) {
                        // 表示解析结束
                        break;
                    }
                }
            } else {
                if (isMatch) {
                    String[] splits = line.split("\\|\\|");
                    if (Objects.nonNull(consumer)) {
                        consumer.accept(splits);
                    } else if (Objects.nonNull(listConsumer)) {
                        list.add(splits);
                    }
                }
            }
        }
        br.close();
        inputStream.close();
        if (Objects.nonNull(listConsumer)) {
            listConsumer.accept(list);
        }
    }
}
