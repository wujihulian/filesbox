package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ServerAboutService {

    JSONObject getServerInfo();
    JSONObject getServerInfoBasic(HttpServletRequest request);
}
