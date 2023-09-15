package com.svnlan.tools;

import com.svnlan.common.I18nUtils;
import com.svnlan.enums.OperatingSystemEnum;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.vo.ParentPathDisplayVo;
import com.svnlan.jwt.domain.LogLogin;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description: 日志数据
 * @Date: 2023/2/14 9:34
 */
@Component
public class SystemLogTool {
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    AsyncSystemLogUtil asyncSystemLogUtil;
    @Resource
    IoSourceDao ioSourceDao;

    public void setSysLog(LoginUser loginUser, String opType, HttpServletRequest request) {
        this.setSysLog(loginUser,  opType, null, request);
    }

    public void setSysLog(LoginUser loginUser, String opType, List<Map<String, Object>> paramList, HttpServletRequest request) {

        String ua;
        String serverName;
        String ip = "";
        String resolution = "";
        if (ObjectUtils.isEmpty(request)) {
            //服务间调用的
            ua = loginUser.getUserAgent();
            serverName = loginUser.getServerName();
            ip = loginUser.getIp();
        } else {
            ua = request.getHeader("User-Agent");
            serverName = loginUserUtil.getServerName(request);
            ip = IpUtil.getIp(request);
            //todo ua处理
            resolution = request.getHeader("resolution");
        }
        asyncSystemLogUtil.setSysLog(loginUser,  opType, paramList, serverName, ua, resolution, ip);
    }


    public String setParentPathDisplay(Map<Long, String> map, String parentLevel, Long spaceID){

        return asyncSystemLogUtil.setParentPathDisplay(map, parentLevel, spaceID);
    }

    public Map<Long, String> getParentPathDisplayMap(Set<String> pList){
        return asyncSystemLogUtil.getParentPathDisplayMap(pList);
    }

    /**
     * 0 其他 1 电信，2 移动，3 联通，4 网通，5 铁通，6 华数，7 教育网
     */
    public void setNetwork(LogLogin logLogin, String address) {
        asyncSystemLogUtil.setNetwork(logLogin, address);
    }

    public Pair<Integer, String> getClientTypeAndOsName(HttpServletRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            request = requestAttributes.getRequest();
        }
        String ua = request.getHeader("User-Agent");
        return asyncSystemLogUtil.getClientTypeAndOsName(ua);
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
