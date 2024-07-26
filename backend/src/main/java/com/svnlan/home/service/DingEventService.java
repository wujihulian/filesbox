package com.svnlan.home.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.OpenDingTalkClient;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/19 10:54
 */
public interface DingEventService {

    void dingEventCallback(JSONObject eventJson);

    OpenDingTalkClient configureStreamClient(String clientId, String clientSecret) throws Exception;
}
