package com.svnlan.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.KeepAliveOption;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.svnlan.home.service.DingEventService;
import com.svnlan.home.utils.AsyncFileTools;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/19 10:54
 */
@Service
public class DingEventServiceImpl implements DingEventService {

    @Resource
    AsyncFileTools asyncFileTools;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    SystemOptionDao systemOptionDao;

    @Override
    public void dingEventCallback(JSONObject eventJson){

        // 判断是否开启同步
        String thirdLoginConfig = systemOptionDao.getSystemConfigByKey("thirdLoginConfig");
        if (ObjectUtils.isEmpty(thirdLoginConfig)){
            LogUtil.info("未开启钉盘同步设置");
            return;
        }
        List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList = null;
        List<ThirdUserInitializeConfig> thirdUserInitializeConfigList = JSONObject.parseArray(thirdLoginConfig, ThirdUserInitializeConfig.class);
        if (!CollectionUtils.isEmpty(thirdUserInitializeConfigList)){
            for (ThirdUserInitializeConfig thirdConfig : thirdUserInitializeConfigList){
                if ("dingding".equals(thirdConfig.getThirdName())){
                    dingInfoDTOList = thirdConfig.getDingInfo();
                    break;
                }
            }
        }

        asyncFileTools.asyncPullDingFiles(eventJson, dingInfoDTOList);


    }


    @Override
    public OpenDingTalkClient configureStreamClient(String clientId, String clientSecret) throws Exception{
        return OpenDingTalkStreamClientBuilder
                .custom()
                .keepAlive(KeepAliveOption.create().withKeepAliveIdleMill(60 * 1000L))
                .credential(new AuthClientCredential(clientId, clientSecret))
                //注册事件监听
                .registerAllEventListener(new GenericEventListener() {
                    @Override
                    public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                        try {
                            //事件唯一Id
                            String eventId = event.getEventId();
                            //事件类型
                            String eventType = event.getEventType();
                            //事件产生时间
                            Long bornTime = event.getEventBornTime();
                            //获取事件体
                            JSONObject bizData = event.getData();
                            //处理事件
                            LogUtil.info("configureStreamClient other  bornTime ：" + bornTime + "发生了：" + eventType + "事件 bizData=" + JsonUtils.beanToJson(bizData));

                            bizData.put("EventType",eventType);
                            dingEventCallback(bizData);
                            //消费成功
                            return EventAckStatus.SUCCESS;
                        } catch (Exception e) {
                            LogUtil.error(e, "configureStreamClient error");
                            //消费失败
                            return EventAckStatus.LATER;
                        }
                    }
                }).build();
    }
}
