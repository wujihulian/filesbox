package com.svnlan.user.tools;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dto.PluginDto;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
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
    SystemOptionDao systemOptionDao;


    public Map<String, Object> optionDataMap(List<OptionVo> optionList){

        if (CollectionUtils.isEmpty(optionList)){
            return null;
        }
        Map<String, Object> dataMap = new HashMap<>(1);
        for (OptionVo option : optionList){
            if (Arrays.asList("browserLogo","systemLogo","wmPicPath").contains(option.getKey()) && !ObjectUtils.isEmpty(option.getValue())){
                dataMap.put(option.getKey()+"ShowPath", FileUtil.getShowImageUrl(option.getValue(), option.getKey()+".png"));
            }
            dataMap.put(option.getKey(), option.getValue());
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
                return reMap;
            }
        }
        /** 系统用户配置 */
        List<OptionVo> systemOptionList = systemOptionDao.getSystemConfig();
        reMap = this.optionDataMap(systemOptionList);
        if (ObjectUtils.isEmpty(reMap)){
            reMap = new HashMap<>(1);
        }
        if (!reMap.containsKey("defaultHome")){
            reMap.put("defaultHome", 2);
        }
        if (ObjectUtils.isEmpty(configString)){
            stringRedisTemplate.opsForValue().set(GlobalConfig.systemConfig_captcha , JsonUtils.beanToJson(reMap), 5, TimeUnit.HOURS);
        }
        return reMap;

    }

    public List pluginList(){
        return pluginList("System.pluginList");
    }
    public List pluginList(String option){
        // 插件列表
        List<OptionVo> systemOptionList = systemOptionDao.getSystemOtherConfig(option);
        PluginListVo pluginListVo = null;

        List<PluginListVo> list = new ArrayList<>();
        for (OptionVo vo : systemOptionList){
            pluginListVo = JsonUtils.jsonToBean(vo.getValue(), PluginListVo.class);
            pluginListVo.setPluginID(vo.getId());
            list.add(pluginListVo);
        }
        return list;
    }
}
