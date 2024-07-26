package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.vo.PluginListVo;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/7 17:12
 */
public interface MsgWarningScheduleService {

    String sendMsgWarming(PluginListVo pluginListVo);
    Double getDiskUsedRate();
    Double getDiskUsedRate(JSONObject object);
}
