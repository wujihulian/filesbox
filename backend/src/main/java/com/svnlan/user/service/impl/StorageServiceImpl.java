package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.IOFile;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dto.StorageDTO;
import com.svnlan.user.dto.StorageDTO.StorageEnum;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.StringUtil;
import com.svnlan.utils.TenantUtil;
import com.svnlan.webdav.FileProperties;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 存储管理
 *
 * @author lingxu 2023/06/08 13:34
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    private static final String STORAGE_TYPE = "Storage";
    private static final String STORAGE_TYPE_DEFAULT = "StorageDefault";

    private static final String DEFAULT_STORAGE_ID_KEY = STORAGE_TYPE + ":" + STORAGE_TYPE_DEFAULT + "_ID";
    private static final String DEFAULT_STORAGE_PATH_KEY = STORAGE_TYPE + ":" + STORAGE_TYPE_DEFAULT + "_PATH";

    @Resource
    private IoFileDao ioFileDao;
    @Resource
    private SystemOptionDao systemOptionDaoImpl;

    @Resource
    private SystemOptionDao systemOptionDao;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    OptionTool optionTool;

    @Override
    public List<StorageDTO> list() {
        List<OptionVo> optionVos = systemOptionDaoImpl.selectListByType(STORAGE_TYPE);
        if (CollectionUtils.isEmpty(optionVos)) {
            return Collections.emptyList();
        }
        // 找出 key 为 STORAGE_TYPE_DEFAULT
        OptionVo defaultOptionVo = optionVos.stream().filter(it -> STORAGE_TYPE_DEFAULT.equals(it.getKeyString())).findAny().orElseGet(() -> {
            // 这种情况下，设置 key 为 local1 的默认的存储配置
            for (OptionVo item : optionVos) {
                if (StorageEnum.LOCAL.name().equals(item.getKeyString())) {
                    OptionVo optionVo = new OptionVo();
                    optionVo.setType(STORAGE_TYPE);
                    optionVo.setKeyString(STORAGE_TYPE_DEFAULT);
                    optionVo.setValueText(item.getId().toString());
                    optionVo.setCreateTime(LocalDateTime.now());
                    optionVo.setModifyTime(LocalDateTime.now());
                    optionVo.setTenantId(1L);
                    systemOptionDaoImpl.insert(optionVo);
                    systemOptionDaoImpl.initSourceStorageId(item.getId());
                    return optionVo;
                }
            }
            throw new SvnlanRuntimeException("未查询到默认记录数据");
        });
        return optionVos.stream().filter(it -> !STORAGE_TYPE_DEFAULT.equals(it.getKeyString())).map(it -> {
            StorageDTO storageDTO = JSONObject.parseObject(it.getValueText(), StorageDTO.class);
            storageDTO.setId(it.getId());
            if (it.getId().toString().equals(defaultOptionVo.getValueText())) {
                // 说明这个是默认的配置
                storageDTO.setIsDefault(1);
            } else {
                storageDTO.setIsDefault(0);
            }
            storageDTO.setUsage(getStorageUsage(storageDTO.getLocation()));
            return storageDTO;
        }).collect(Collectors.toList());
    }


    private Long getStorageUsage(String location) {
        Long sum = CaffeineUtil.STORAGE_USAGE.getIfPresent(location);
        if (Objects.isNull(sum)) {
            synchronized (this) {
                sum = CaffeineUtil.STORAGE_USAGE.getIfPresent(location);
                if (Objects.isNull(sum)) {
                    sum = Optional.ofNullable(ioFileDao.selectStorageUsage(location)).orElse(0L);
                    CaffeineUtil.STORAGE_USAGE.put(location, sum);
                }
            }
        }
        return sum;

    }

    @Override
    public StorageDTO getById(Integer id) {
        OptionVo optionVo = systemOptionDao.selectById(id);
        if (Objects.isNull(optionVo)) {
            return null;
        }
        StorageDTO storageDTO = JSONObject.parseObject(optionVo.getValue(), StorageDTO.class);
        OptionVo defaultOptionVo = systemOptionDao.selectOneByKeyString(STORAGE_TYPE_DEFAULT);
        Assert.notNull(defaultOptionVo, "未查询到默认记录数据");
        storageDTO.setId(id);
        if (defaultOptionVo.getValue().equals(id.toString())) {
            storageDTO.setIsDefault(1);
        } else {
            storageDTO.setIsDefault(0);
        }
        return storageDTO;
    }

    @Override
    public void createOrEdit(StorageDTO storageDTO, boolean isDefaultSet) {
        String value = JSONObject.toJSONString(storageDTO);
        if (StringUtils.hasText(storageDTO.getLocation())) {
            // 判断 location 的唯一性
            List<OptionVo> originOptionList = systemOptionDao.selectOneByTypeAndKeyString(STORAGE_TYPE, STORAGE_TYPE_DEFAULT, false);
            if (!CollectionUtils.isEmpty(originOptionList)) {
                Optional<OptionVo> optional = originOptionList.stream().filter(it -> {
                    StorageDTO storage = JSONObject.parseObject(it.getValueText(), StorageDTO.class);
                    return Objects.equals(storageDTO.getLocation(), storage.getLocation());
                }).findFirst();

                if (optional.isPresent()) {
                    Assert.isTrue(Objects.nonNull(storageDTO.getId()), "新增时 location 重复");
                    Assert.isTrue(Objects.equals(optional.get().getId(), storageDTO.getId()), "更新时 location 重复");
                }
            }
        }

        OptionVo optionVo = new OptionVo();
        optionVo.setModifyTime(LocalDateTime.now());
        if (Objects.nonNull(storageDTO.getId())) {
            // 查询当前的默认
            List<OptionVo> defaultOptionVoList = systemOptionDao.selectOneByTypeAndKeyString(STORAGE_TYPE, STORAGE_TYPE_DEFAULT, true);
            if (!CollectionUtils.isEmpty(defaultOptionVoList)) {
                OptionVo defaultOptionVo = defaultOptionVoList.get(0);
                // 并且当前的就是默认的
                if (Objects.equals(defaultOptionVo.getValue(), storageDTO.getId().toString())
                        && Objects.equals(storageDTO.getIsDefault(), 0)) {
                    throw new SvnlanRuntimeException("禁止关闭默认存储");
                }
            }

            optionVo.setId(storageDTO.getId());
            if (!isDefaultSet) {
                // 更新
                optionVo.setValueText(value);
                optionVo.setKeyString(storageDTO.getStorageKey() + "_" + storageDTO.getName());
                systemOptionDao.updateById(optionVo);
            } else {
                // 表示是修改的 默认存储
                stringRedisTemplate.delete(DEFAULT_STORAGE_ID_KEY);
                stringRedisTemplate.delete(DEFAULT_STORAGE_PATH_KEY);
            }
        } else {
            // 新增
            optionVo.setType(STORAGE_TYPE);
            optionVo.setKeyString(storageDTO.getStorageKey() + "_" + storageDTO.getName());
            optionVo.setValueText(value);
            optionVo.setCreateTime(LocalDateTime.now());
            systemOptionDao.insert(optionVo);

        }

        // 更新现在默认的记录 如果这条是默认的话
        if (Objects.equals(storageDTO.getIsDefault(), 1)) {
            OptionVo optionVoUpdate = new OptionVo();
            optionVoUpdate.setKeyString(STORAGE_TYPE_DEFAULT);
            optionVoUpdate.setValueText(optionVo.getId().toString());
            optionVoUpdate.setModifyTime(LocalDateTime.now());
            optionVoUpdate.setType(STORAGE_TYPE);
            systemOptionDao.updateByKeyStringAndType(optionVoUpdate);
        }
    }

    @Override
    public void unloadStorage(Integer id) {
        // 查询
        OptionVo optionVo = systemOptionDao.selectById(id);
        Assert.notNull(optionVo, "未查询到对应的存储");
        // 判断是否为 默认的
        List<OptionVo> defaultOptionVoList = systemOptionDao.selectOneByTypeAndKeyString(STORAGE_TYPE, STORAGE_TYPE_DEFAULT, true);
        Assert.notEmpty(defaultOptionVoList, "未查询到对应的存储");
        OptionVo defaultOptionVo = defaultOptionVoList.get(0);
        Assert.isTrue(!Objects.equals(defaultOptionVo.getValue(), id.toString()), "默认的存储不能被卸载");
        StorageDTO storageDTO = JSONObject.parseObject(optionVo.getValue(), StorageDTO.class);
        Assert.hasText(storageDTO.getLocation(), "该存储没有对应的目录");
        // 判断是否已经有数据存在 如果有的话，也不能被卸载

        IOFile ioFile = ioFileDao.selectOneByPathLike(storageDTO.getLocation());

        // 后面会做数据迁移
        Assert.isTrue(Objects.isNull(ioFile), "该存储上有资源，不能被卸载");

        Assert.isTrue(Objects.equals(systemOptionDao.deleteById(id), 1), "存储卸载失败");
    }

    @Resource
    private FileProperties fileProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取默认的存储路径
     */
    @Override
    public String getDefaultStorageDevicePath() {
        return getDefaultStorageDevicePath(null, 0);
    }
    /** type: 0 租户Id， 1 用户Id， 2 sourceID  */
    @Override
    public String getDefaultStorageDevicePath(Long opTypeId, int type) {
        String defaultLocation = stringRedisTemplate.opsForValue().get(DEFAULT_STORAGE_PATH_KEY);
        Long tenantId;
        if (type == 1){
            tenantId = tenantUtil.getTenantIdByUserId(opTypeId);
        }else if (type == 2){
            tenantId = tenantUtil.getTenantIdBySourceId(opTypeId);
        }else {
            tenantId = ObjectUtils.isEmpty(opTypeId) ? tenantUtil.getTenantIdByServerName() : opTypeId;
        }
        if (!StringUtils.hasText(defaultLocation)) {
            synchronized (this) {
                defaultLocation = stringRedisTemplate.opsForValue().get(DEFAULT_STORAGE_PATH_KEY);
                if (!StringUtils.hasText(defaultLocation)) {
                    StorageDTO storageDTO = getDefaultStorageDevice(2);
                    Assert.hasText(storageDTO.getLocation(), "未解析出 location");
                    defaultLocation = storageDTO.getLocation();
                    // 过期时间为 10 分钟
                    stringRedisTemplate.opsForValue().set(DEFAULT_STORAGE_PATH_KEY, defaultLocation, 10, TimeUnit.MINUTES);
                }
            }
        }
        if (ObjectUtils.isEmpty(tenantId)){
            tenantId = 0L;
        }
        // 思维缜密
        return defaultLocation + "/" + tenantId;
    }

    /**
     * 获取 storageDTO
     *
     * @param type 1 只需要id 2 只需要 path 3 我全都要😁
     */
    private StorageDTO getDefaultStorageDevice(Integer type) {
        List<OptionVo> defaultOptionVoList = systemOptionDao.selectOneByTypeAndKeyString(STORAGE_TYPE, STORAGE_TYPE_DEFAULT, true);

        // 添加默认存储设置
        if (CollectionUtils.isEmpty(defaultOptionVoList)){
            List<OptionVo> optionVoList = systemOptionDao.selectOneByTypeAndKeyString(STORAGE_TYPE, StorageEnum.LOCAL.name(), true);
            if (!CollectionUtils.isEmpty(optionVoList)){
                OptionVo optionVo = new OptionVo();
                optionVo.setType(STORAGE_TYPE);
                optionVo.setKeyString(STORAGE_TYPE_DEFAULT);
                optionVo.setValueText(optionVoList.get(0).getId().toString());
                optionVo.setCreateTime(LocalDateTime.now());
                optionVo.setModifyTime(LocalDateTime.now());
                optionVo.setTenantId(1L);
                systemOptionDaoImpl.insert(optionVo);
                systemOptionDaoImpl.initSourceStorageId(optionVoList.get(0).getId());
                defaultOptionVoList = new ArrayList<>();
                defaultOptionVoList.add(optionVo);
            }
        }
        Assert.notEmpty(defaultOptionVoList, "未查询到默认的存储设置记录");
        OptionVo defaultOptionVo = defaultOptionVoList.get(0);

        Integer defaultStorageDeviceId = Integer.valueOf(defaultOptionVo.getValue());
        OptionVo optionVo = systemOptionDao.selectById(defaultStorageDeviceId);
        Assert.notNull(optionVo, "未查询到默认的存储设置");
        if (Objects.equals(type, 1)) {
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setId(optionVo.getId());
            return storageDTO;
        } else if (Objects.equals(type, 2)) {
            return JSONObject.parseObject(optionVo.getValue(), StorageDTO.class);
        } else {
            StorageDTO storageDTO = JSONObject.parseObject(optionVo.getValue(), StorageDTO.class);
            storageDTO.setId(optionVo.getId());
            return storageDTO;
        }
    }

    /**
     * 获取默认的 存储id
     */
    @Override
    public Integer getDefaultStorageDeviceId() {
        StorageDTO storageDTO = getDefaultStorageDevice(1);
        return  storageDTO.getId();
        /*
        String defaultId = stringRedisTemplate.opsForValue().get(DEFAULT_STORAGE_ID_KEY);
        if (!StringUtils.hasText(defaultId)) {
            synchronized (this) {
                defaultId = stringRedisTemplate.opsForValue().get(DEFAULT_STORAGE_ID_KEY);
                if (!StringUtils.hasText(defaultId)) {
                    StorageDTO storageDTO = getDefaultStorageDevice(1);
                    defaultId = storageDTO.getId().toString();
                    // 过期时间为 10 分钟
                    stringRedisTemplate.opsForValue().set(DEFAULT_STORAGE_ID_KEY, defaultId, 10, TimeUnit.MINUTES);
                }
            }
        }
        if (StringUtils.hasText(defaultId) && StringUtil.isNumeric(defaultId)) {
            return Integer.parseInt(defaultId);
        }
        throw new SvnlanRuntimeException("defaultId 为空 或者不为数字类型");*/
    }

    /**
     * 根据 存储id 查询对应的 path
     */
    @Override
    public String getStoragePathById(Integer id) {
        String path = CaffeineUtil.STORAGE_MAP_INFO.getIfPresent(id);
        if (!StringUtils.hasText(path)) {
            synchronized (this) {
                path = CaffeineUtil.STORAGE_MAP_INFO.getIfPresent(id);
                if (!StringUtils.hasText(path)) {
                    initializeStorageInfo();
                    path = CaffeineUtil.STORAGE_MAP_INFO.getIfPresent(id);
                }
            }
        }
        return path;
    }

    @Override
    public Long getTotalSpace() {
        return optionTool.getTotalSpace();
    }

    /**
     * 将 储存数据缓存到内存中
     */
    @PostConstruct
    public void initializeStorageInfo() {
        List<OptionVo> optionVoList = systemOptionDaoImpl.selectListByType(STORAGE_TYPE);
//        List<OptionVo> optionVoList = systemOptionDao.selectList(new LambdaQueryWrapper<OptionVo>().eq(OptionVo::getType, STORAGE_TYPE));
        Assert.notEmpty(optionVoList, "未查询到配置的存储信息");

        for (OptionVo optionVo : optionVoList) {
            if (STORAGE_TYPE_DEFAULT.equals(optionVo.getKeyString())) {
                continue;
            }
            StorageDTO storageDTO = JSONObject.parseObject(optionVo.getValue(), StorageDTO.class);
            CaffeineUtil.STORAGE_MAP_INFO.put(optionVo.getId(), storageDTO.getLocation());
        }

    }

}
