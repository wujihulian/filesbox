package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 *
 */
@Component
public class AsyncFileTools {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    AsyncDingFileUtil asyncDingFileUtil;

    @Async(value = "asyncTaskExecutor")
    public void asyncPullDingFiles(JSONObject eventJson, List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList){

        boolean check = false;
        String eventType = eventJson.getString("EventType");
        switch (eventType){
            case "storage_dentry_create":
                check = checkEventRepeat(eventType, eventJson);
                if (!check){
                    return;
                }
                // 文件或文件夹添加事件
                asyncDingFileUtil.storageDentryCreate("",eventJson, dingInfoDTOList);
                break;
            case "storage_dentry_update":
                // 文件或文件夹更新事件
                check = checkEventRepeat(eventType, eventJson);
                if (!check){
                    return;
                }
                asyncDingFileUtil.storageDentryUpdate(eventJson, dingInfoDTOList);
                break;
            case "storage_dentry_delete":
                // 文件或文件夹删除事件
                check = checkEventRepeat(eventType, eventJson);
                if (!check){
                    return;
                }
                asyncDingFileUtil.storageDentryDelete(eventJson, dingInfoDTOList);
                break;
            default:
                LogUtil.info("dingEventCallback 时间订阅其他事件 eventJson=" + JsonUtils.beanToJson(eventJson));
                break;
        }
    }


    private boolean checkEventRepeat(String eventType, JSONObject eventJson){
        String type = eventJson.getString("type");
        String dentryId = eventJson.getString("dentryId");
        String UnionId = null;
        if (eventJson.containsKey("unionId") ){
            UnionId = eventJson.getString("unionId");
        }else {
            UnionId = eventJson.getString("UnionId");
        }
        String spaceId = eventJson.getString("spaceId");
        String key = "checkEventRepeat_" + eventType + "_" + UnionId + "_" + type + "_" + spaceId + "_" + dentryId;
        Boolean setSuccess = this.stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 3, TimeUnit.SECONDS);
        if(!setSuccess) {
            LogUtil.error( "  checkEventRepeat进行中！" + JsonUtils.beanToJson(eventJson));
            return false;
        }
        return true;
    }

}
