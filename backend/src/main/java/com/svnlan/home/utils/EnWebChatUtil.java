package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class EnWebChatUtil {

    /*
     * 获取部门列表
     */
    public static JSONObject getDepartmentList(RestTemplate restTemplate, String accessToken, Long id) {

        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("accessToken", accessToken);
        if (!ObjectUtils.isEmpty(id)) {
            variableMap.put("id", String.valueOf(id));
        }
        String str = restTemplate.getForObject((ObjectUtils.isEmpty(id) ? WebChatApi.DEPT_LIST : WebChatApi.DEPT_SUB_LIST),
                String.class, variableMap);
        return JSONObject.parseObject(str);
    }

    /*
     * 获取部门成员详情列表
     */
    public static JSONObject getDepartmentUserList(RestTemplate restTemplate, String accessToken, Long id) {

        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("accessToken", accessToken);
        variableMap.put("deptId", String.valueOf(id));

        String str = restTemplate.getForObject((WebChatApi.DEPT_USER_LIST),
                String.class, variableMap);
        return JSONObject.parseObject(str);
    }


}
