package com.svnlan.user.tools;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.KeepAliveOption;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.utils.AsyncCubeFileUtil;
import com.svnlan.home.utils.CubeUtil;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jooq.tables.pojos.SystemOptionModel;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.dto.PluginDto;
import com.svnlan.user.vo.*;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 14:50
 */
@Component
public class OptionTool {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    SystemOptionDao systemOptionDaoImpl;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    AsyncCubeFileUtil cubeFileUtil;
    private static final String STORAGE_TYPE = "Storage";
    private static final String STORAGE_TYPE_DEFAULT = "StorageDefault";

    public Map<String, Object> optionDataMap(List<OptionVo> optionList){

        if (CollectionUtils.isEmpty(optionList)){
            return null;
        }
        Map<String, Object> dataMap = new HashMap<>(1);
        for (OptionVo option : optionList){
            if (Arrays.asList("browserLogo","systemLogo","wmPicPath").contains(option.getKeyString()) && !ObjectUtils.isEmpty(option.getValue())){
                dataMap.put(option.getKeyString()+"ShowPath", FileUtil.getShowImageUrl(option.getValue(), option.getKeyString()+".png"));
            }
            dataMap.put(option.getKeyString(), option.getValue());
        }
        if (!dataMap.containsKey("passwordState")){
            dataMap.put("passwordState", "1");
        }
        return dataMap;
    }

    /**  获取系统设置 */
    public Map<String, Object> getSystemConfigMap(){
        String key = GlobalConfig.systemConfig_captcha;
        String configString = stringRedisTemplate.opsForValue().get(key);
        Map<String, Object> reMap = null;
        if (!ObjectUtils.isEmpty(configString)){
            try {
                reMap = JsonUtils.jsonToMap(configString);
            }catch (Exception e){
                LogUtil.error(e , "getSystemConfigMap error");
            }
            if (!ObjectUtils.isEmpty(reMap)){
                if (!reMap.containsKey("defaultHome")){
                    reMap.put("defaultHome", 2);
                }
                checkThirdLoginConfig(reMap);
                defaultNetworkConfig(reMap);
                return reMap;
            }
        }
        /** 系统用户配置 */
        List<OptionVo> systemOptionList = systemOptionDaoImpl.getSystemConfig();
        reMap = this.optionDataMap(systemOptionList);
        if (ObjectUtils.isEmpty(reMap)){
            reMap = new HashMap<>(1);
        }
        if (!reMap.containsKey("defaultHome")){
            reMap.put("defaultHome", 2);
        }
        checkThirdLoginConfig(reMap);
        defaultNetworkConfig(reMap);
        if (ObjectUtils.isEmpty(configString)){
            stringRedisTemplate.opsForValue().set(GlobalConfig.systemConfig_captcha , JsonUtils.beanToJson(reMap), 5, TimeUnit.HOURS);
        }
        return reMap;

    }

    // 检查是否默认isSyncDing
    public void checkThirdLoginConfig(Map<String, Object> reMap){
        if (reMap.containsKey("thirdLoginConfig")){
            String third = (String)reMap.get("thirdLoginConfig");
            if (!ObjectUtils.isEmpty(third) && third.indexOf("isSyncDing") < 0){
                List<ThirdUserInitializeConfig> thirdUserInitializeConfigList = JSONObject.parseArray(third, ThirdUserInitializeConfig.class);
                if (!CollectionUtils.isEmpty(thirdUserInitializeConfigList)){
                    boolean isCube = true;
                    Integer dingRoleId = 0;
                    for (ThirdUserInitializeConfig thirdConfig : thirdUserInitializeConfigList){
                        if ("dingding".equals(thirdConfig.getThirdName())){
                            thirdConfig.setIsSyncDing(0);
                            dingRoleId = thirdConfig.getRoleID();
                            thirdConfig.setDingInfo(this.defaultDingInfo());
                        }else if ("enWechat".equals(thirdConfig.getThirdName())){
                            thirdConfig.setIsSyncDing(0);
                            thirdConfig.setDingInfo(this.defaultEnWebChatInfo());
                        }else if ("eube".equals(thirdConfig.getThirdName()) || "cube".equals(thirdConfig.getThirdName())){
                            isCube = false;
                            thirdConfig.setIsSyncDing(0);
                            thirdConfig.setThirdName("cube");
                            //thirdConfig.setDingInfo(this.defaultEnWebChatInfo());
                        }

                    }
                    if (isCube){
                        // 默认
                        ThirdUserInitializeConfig thirdConfig = new ThirdUserInitializeConfig();
                        thirdConfig.setIsSyncDing(0);
                        thirdConfig.setDingInfo(null);
                        thirdConfig.setSizeMax(2.0);
                        thirdConfig.setThirdName("cube");
                        thirdConfig.setRoleID(dingRoleId);
                        thirdUserInitializeConfigList.add(thirdConfig);
                    }
                    LogUtil.info(isCube + "  thirdUserInitializeConfigList=" + JsonUtils.beanToJson(thirdUserInitializeConfigList));
                    reMap.put("thirdLoginConfig", JsonUtils.beanToJson(thirdUserInitializeConfigList));
                }
            }
        }
    }
    // 检查是否默认isSyncDing
    public void defaultNetworkConfig(Map<String, Object> reMap){
        if (!reMap.containsKey("networkConfig")){
            reMap.put("networkConfig", "{\"status\":0,\"intranet\":\"\",\"external\":\"\"}");
        }
    }

    public List<ThirdUserInitializeConfig.DingInfoDTO> defaultDingInfo(){
        List<ThirdUserInitializeConfig.DingInfoDTO> list = new ArrayList<>();
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("myDoc",0L,0L,"我的钉盘文档"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("teamDoc",1L,0L,"钉盘文件/团队文件"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("groupFiles",1L,0L,"钉盘文件/群文件"));
        return list;
    }

    public List<ThirdUserInitializeConfig.DingInfoDTO> defaultEnWebChatInfo(){
        List<ThirdUserInitializeConfig.DingInfoDTO> list = new ArrayList<>();
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("myDoc",0L,0L,"我的微盘文档"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("teamDoc",1L,0L,"微盘文件/团队文件"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("groupFiles",1L,0L,"微盘文件/群文件"));
        return list;
    }
    public List<ThirdUserInitializeConfig.DingInfoDTO> defaultEubeInfo(){
        List<ThirdUserInitializeConfig.DingInfoDTO> list = new ArrayList<>();
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("myDoc",0L,0L,"我的魔方文档"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("teamDoc",1L,0L,"魔方文件/团队文件"));
        list.add(new ThirdUserInitializeConfig.DingInfoDTO("groupFiles",1L,0L,"魔方文件/群文件"));
        return list;
    }

    public List pluginList(){
        return pluginList("System.pluginList", null);
    }
    public List pluginList(String option, Long tenantId){
        // 插件列表
        List<OptionVo> systemOptionList = systemOptionDaoImpl.getSystemOtherConfig(option,tenantId);
        PluginListVo pluginListVo = null;

        List<PluginListVo> list = new ArrayList<>();
        for (OptionVo vo : systemOptionList){
            pluginListVo = JsonUtils.jsonToBean(vo.getValueText(), PluginListVo.class);
            pluginListVo.setPluginID(vo.getId());

            if ("yzow".equals(vo.getType())){
                pluginListVo.setViewUrl(ObjectUtils.isEmpty(pluginListVo.getViewUrl()) ? "" : pluginListVo.getViewUrl());
                pluginListVo.setEditUrl(ObjectUtils.isEmpty(pluginListVo.getEditUrl()) ? "" : pluginListVo.getEditUrl());
            }
            list.add(pluginListVo);
        }
        return list;
    }

    public IOSource addDir(HomeExplorerVO homeExplorerVO, Long tenantId, Integer storageId) {
        HomeExplorerVO parent = homeExplorerDao.getOneSourceInfo(homeExplorerVO.getParentID());
        if (ObjectUtils.isEmpty(parent) ){
            return null;
        }
        IOSource sourceSearch = ioSourceDao.getSourceByName(homeExplorerVO.getParentID(), homeExplorerVO.getName());
        if (!ObjectUtils.isEmpty(sourceSearch)){
            return sourceSearch;
        }
        if (ObjectUtils.isEmpty(homeExplorerVO.getIsSafe())) {
            int isSafe = getSourceIsSafe(homeExplorerVO.getTargetID(), parent.getParentLevel() + homeExplorerVO.getParentID() + ",");
            homeExplorerVO.setIsSafe(isSafe);
        }

        IOSource source = new IOSource();
        source.setName(homeExplorerVO.getName());
        source.setParentLevel(parent.getParentLevel() + homeExplorerVO.getParentID() + "," );
        source.setTargetId(homeExplorerVO.getTargetID());
        source.setCreateUser(0L);
        source.setModifyUser(0L);
        source.setIsFolder(1);
        source.setTargetType(parent.getTargetType());
        source.setFileType("");
        source.setFileId(0L);
        source.setParentId(homeExplorerVO.getParentID());
        source.setStorageId(storageId);
        source.setTenantId(tenantId);
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        source.setSourceHash(homeExplorerVO.getSourceHash());
        source.setIsSafe(homeExplorerVO.getIsSafe());
        try {
            homeExplorerDao.createDir(source);
        }catch (Exception e){
            LogUtil.error(e, " addDir error");
            return null;
        }
        return source;
    }

    /**  获取钉钉设置 */
    public DingConfigVo getDingDingConfig(){
         return getDingDingConfig(null);
    }
    public DingConfigVo getDingDingConfig(Long tenantId){
        if (ObjectUtils.isEmpty(tenantId)){
            tenantId = tenantUtil.getTenantIdByServerName();
        }
        String key = GlobalConfig.systemConfig_dingding_captcha + tenantId;
        String configString = stringRedisTemplate.opsForValue().get(key);
        DingConfigVo reMap = null;
        if (!ObjectUtils.isEmpty(configString)){
            try {
                reMap = JsonUtils.jsonToBean(configString, DingConfigVo.class);
            }catch (Exception e){
                LogUtil.error(e , "getDingDingConfig error");
            }
            if (!ObjectUtils.isEmpty(reMap)){
                return reMap;
            }
        }
        /** 系统用户配置 */
        String systemOptionDing = systemOptionDaoImpl.getSystemConfigByKey("dingConfig",tenantId);

        if (!ObjectUtils.isEmpty(systemOptionDing)){
            try {
                reMap = JsonUtils.jsonToBean(systemOptionDing, DingConfigVo.class);
            }catch (Exception e){
                LogUtil.error(e , "getDingDingConfig systemOptionDing error");
            }
            if (!ObjectUtils.isEmpty(reMap)) {
                stringRedisTemplate.opsForValue().set(key, JsonUtils.beanToJson(reMap), 5, TimeUnit.HOURS);
            }
        }
        return reMap;

    }

    public Long getTotalSpace() {
        Long totalSpace = 0L;
        List<OptionVo> optionVos = systemOptionDaoImpl.selectListByType(STORAGE_TYPE);
        if (CollectionUtils.isEmpty(optionVos)) {
            return totalSpace;
        }
        for (OptionVo item : optionVos) {
            if (!item.getKeyString().equals(STORAGE_TYPE_DEFAULT)) {
                JSONObject object = JSONObject.parseObject(item.getValueText());
                if (object != null && object.getLong("size") != null) {
                    totalSpace += object.getLongValue("size");
                }
            }
        }
        return totalSpace;
    }

    /**  获取企业微信设置 */
    public EnWebChatConfigVo getEnWebChatConfig(){
        return getEnWebChatConfig(null);
    }
    public EnWebChatConfigVo getEnWebChatConfig(Long tenantId){
        if (ObjectUtils.isEmpty(tenantId)){
            tenantId = tenantUtil.getTenantIdByServerName();
        }
        String key = GlobalConfig.systemConfig_enWebchat_captcha + tenantId;
        String configString = stringRedisTemplate.opsForValue().get(key);
        EnWebChatConfigVo reMap = null;
        if (!ObjectUtils.isEmpty(configString)){
            try {
                reMap = JsonUtils.jsonToBean(configString, EnWebChatConfigVo.class);
            }catch (Exception e){
                LogUtil.error(e , "getEnWebChatConfig error");
            }
            if (!ObjectUtils.isEmpty(reMap)){
                return reMap;
            }
        }
        /** 系统用户配置 */
        String systemOptionDing = systemOptionDaoImpl.getSystemConfigByKey("enWechatConfig",tenantId);

        if (!ObjectUtils.isEmpty(systemOptionDing)){
            try {
                reMap = JsonUtils.jsonToBean(systemOptionDing, EnWebChatConfigVo.class);
            }catch (Exception e){
                LogUtil.error(e , "getEnWebChatConfig systemOption error");
            }
            if (!ObjectUtils.isEmpty(reMap)) {
                stringRedisTemplate.opsForValue().set(key, JsonUtils.beanToJson(reMap), 5, TimeUnit.HOURS);
            }
        }
        return reMap;

    }


    /**  获取钉钉设置 */
    public CubeConfigVo getCubeConfig(){
        return getCubeConfig(null);
    }
    public CubeConfigVo getCubeConfig(Long tenantId){
        if (ObjectUtils.isEmpty(tenantId)){
            tenantId = tenantUtil.getTenantIdByServerName();
        }
        String key = GlobalConfig.systemConfig_cube_captcha + tenantId;
        String configString = stringRedisTemplate.opsForValue().get(key);
        CubeConfigVo reMap = null;
        if (!ObjectUtils.isEmpty(configString)){
            try {
                reMap = JsonUtils.jsonToBean(configString, CubeConfigVo.class);
            }catch (Exception e){
                LogUtil.error(e , "getCubeConfig error");
            }
            if (!ObjectUtils.isEmpty(reMap)){
                return reMap;
            }
        }
        /** 系统用户配置 */
        String systemOption = systemOptionDaoImpl.getSystemConfigByKey("cubeConfig",tenantId);

        if (!ObjectUtils.isEmpty(systemOption)){
            try {
                reMap = JsonUtils.jsonToBean(systemOption, CubeConfigVo.class);
                if (ObjectUtils.isEmpty(reMap.getCubeOrgId()) && !ObjectUtils.isEmpty(reMap.getDingCorpId())){
                    if (!ObjectUtils.isEmpty(reMap.getAccessKey()) && !ObjectUtils.isEmpty(reMap.getSecretKey())) {
                        try {
                            // 根据钉钉组织id获取
                            JSONObject cubeOrg = cubeFileUtil.getCubeOrgByCorpIdApi(reMap.getAccessKey(), reMap.getSecretKey(), reMap.getDingCorpId());
                            reMap.setCubeOrgId(cubeOrg.getString("cubeOrgId"));
                            List<OptionVo> list = new ArrayList<>();
                            list.add(new OptionVo("cubeConfig", JsonUtils.beanToJson(reMap)));
                            systemOptionDaoImpl.updateSystemOption("",list,tenantId);
                        } catch (Exception e) {
                            LogUtil.error(e, "根据钉钉corpId获取魔方组织id失败 error");
                        }
                    }
                }
            }catch (Exception e){
                LogUtil.error(e , "getCubeConfig systemOptionDing error");
            }
            if (!ObjectUtils.isEmpty(reMap)) {
                stringRedisTemplate.opsForValue().set(key, JsonUtils.beanToJson(reMap), 5, TimeUnit.HOURS);
            }
        }
        return reMap;

    }

    /** 查看是否是私密保险箱 */
    public int getSourceIsSafe(Long userId, String parentLevel){
        return ioSourceDao.getSourceIsSafe(userId, parentLevel);
    }

}
