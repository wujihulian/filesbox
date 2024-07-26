package com.svnlan.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.domain.User;
import com.svnlan.user.vo.UserVo;
import org.springframework.data.util.Pair;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * CaffeineUtil
 *
 * @author jmal
 */
public class CaffeineUtil {

    /**
     * 用于缓存 {@link com.svnlan.webdav.config.DiskSourceUtil#getIoSource(java.nio.file.Path, java.lang.Long, java.lang.Long, java.lang.String)}
     * 的数据，即 path 与 ioSource 的对应关系
     */
    public static final Cache<String, IOSourceVo> PATH_IO_SOURCE_MAP_CACHE = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    /**
     * 当前在线的用户， key 为 userId  value 为时间戳
     */
    public static final Cache<Long, Long> CURRENT_ONLINE_USER = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    /**
     * 每个存储用了多少空间
     */
    public static final Cache<String, Long> STORAGE_USAGE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();
//    private static final Cache<Long, UserSimpleInfo> USER_SIMPLE_INFO = Caffeine.newBuilder()
//            .expireAfterWrite(2, TimeUnit.DAYS)
//            .build();
    /**
     * 存储id及对应的路径
     */
    public static final Cache<Integer, String> STORAGE_MAP_INFO = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();
    /***
     * 用户信息缓存
     * key: username
     * value: ConsumerDO 用户信息
     */
    public static final Cache<String, User> CONSUMER_USERNAME = Caffeine.newBuilder().build();

    /***
     * 已上传的分片索引
     */
    private static Cache<String, CopyOnWriteArrayList<Integer>> resumeCache;

    /***
     * 已写入的分片索引
     */
    private static Cache<String, CopyOnWriteArrayList<Integer>> writtenCache;

    /***
     * 未写入(已上传)的分片索引
     */
    private static Cache<String, CopyOnWriteArrayList<Integer>> unWrittenCache;

    /***
     * 分片写锁
     */
    private static Cache<String, Lock> chunkWriteLockCache;

    /***
     * 上传文件夹锁
     */
    private static Cache<String, Lock> uploadFolderLockCache;
    /**
     * 文件后缀类型缓存
     */
    public final static Cache<String, List<JSONObject>> FILE_EXT_PROPORTION_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.SECONDS)
            .build();
    /**
     * 用户使用空间缓存
     */
    public final static Cache<String, List<JSONObject>> USER_SPACE_RANK_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();
    /**
     * 部门使用空间缓存
     */
    public final static Cache<String, List<JSONObject>> GROUP_SPACE_PROPORTION_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(50, TimeUnit.SECONDS)
            .build();
    /**
     * 文件所属类型缓存
     */
    public final static Cache<String, List<JSONObject>> FILE_TYPE_PROPORTION_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(120, TimeUnit.SECONDS)
            .build();
    /**
     * 用户个人空间根资源 ioSourceId
     */
    public final static Cache<Long, IOSourceVo> USER_ROOT_DIRECTORY_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.DAYS)
            .build();
    /**
     * 收藏夹根下的资源
     */
    public final static Cache<String, Pair<IOSourceVo, IOFile>> FAVOR_SOURCE = Caffeine.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .build();

    /**
     * 认证信息
     * key token
     * value userVo
     */
    public final static Cache<String, UserVo> AUTHORIZATION_USERID = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    @PostConstruct
    public void initCache() {
        initMyCache();
    }

    public static void initMyCache() {
        if (resumeCache == null) {
            resumeCache = Caffeine.newBuilder().build();
        }
        if (writtenCache == null) {
            writtenCache = Caffeine.newBuilder().build();
        }
        if (unWrittenCache == null) {
            unWrittenCache = Caffeine.newBuilder().build();
        }
        if (chunkWriteLockCache == null) {
            chunkWriteLockCache = Caffeine.newBuilder().build();
        }
        if (uploadFolderLockCache == null) {
            uploadFolderLockCache = Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();
        }
    }

}
