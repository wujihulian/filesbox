package com.svnlan.webdav.config;

import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceEventDao;
import com.svnlan.home.dao.IoSourceRecycleDao;
import com.svnlan.home.service.UploadService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.user.service.IoSourceService;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.webdav.attribute.ExtendedAttributesExtension;
import com.svnlan.webdav.impl.FavorResourceProcessor;
import com.svnlan.webdav.impl.PrivateResourceProcessor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户承接 service 或者 dao 的wrapper 避免再 DiskDirResourceSet 过多引入
 *
 * @author lingxu 2023/06/03 15:27
 */
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
@Component("diskServiceOrDaoWrapper")
public class DiskServiceOrDaoWrapper {

    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    IoSourceService ioSourceService;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    IoSourceRecycleDao ioSourceRecycleDao;
    @Resource
    IoSourceEventDao ioSourceEventDao;

    @Resource
    UploadService uploadService;

    @Resource
    LoginUserUtil loginUserUtil;

    @Resource
    FileOptionTool fileOptionTool;

    @Resource
    StorageService storageService;

    @Resource
    RedissonClient redissonClient;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    DiskSourceUtil diskSourceUtil;

    @Resource
    ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    FavorResourceProcessor favorResourceProcessor;

    @Resource
    PrivateResourceProcessor privateResourceProcessor;


    @Resource
    ExtendedAttributesExtension extAttrExtension;
}
