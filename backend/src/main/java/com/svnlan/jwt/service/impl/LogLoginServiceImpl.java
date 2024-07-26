package com.svnlan.jwt.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.dao.UserJwtDao;
import com.svnlan.jwt.domain.LogLogin;
import com.svnlan.jwt.domain.SystemLog;
import com.svnlan.jwt.service.LogLoginService;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Description:
 */
@Slf4j
@Service
public class LogLoginServiceImpl implements LogLoginService {
    @Resource
    private SystemLogDao systemLogDao;
    @Resource
    private UserJwtDao userJwtDaoImpl;
    @Resource
    private SystemLogDao systemLogDaoImpl;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemLogTool systemLogTool;

    /**
     * @Description: 插入登录日志记录
     * @params: [value]
     * @Return: void
     * @Modified:
     */
    @Override
    public void setLogLogin(String value) {
        LogLogin logLogin = JsonUtils.jsonToBean(value, LogLogin.class);
        setLogLogin(logLogin);
    }
    @Override
    public void setLogLogin(LogLogin logLogin) {
        String userIp = logLogin.getUserIp().split(",")[0];
        String network = "";
        String country = null;
        try {
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            String value = operations.get(GlobalConfig.user_ip_location, userIp);
            IPLocation ipLocation = null;
            if (ObjectUtils.isEmpty(value)) {
                IPAddressUtils ipAddressUtils = new IPAddressUtils();
                try {
                    ipLocation = ipAddressUtils.getIPLocation(userIp);
                }catch (Exception e){
                }
                if (ObjectUtils.isEmpty(ipLocation)) {
                    ipLocation = new IPLocation();
                    ipLocation.setCountry("本机地址");
                    ipLocation.setArea("");
                }
                operations.put(GlobalConfig.user_ip_location, userIp, ipLocation.getCountry()+"@&&@"+ipLocation.getArea());
                operations.getOperations().expire(GlobalConfig.user_ip_location, 24, TimeUnit.HOURS);
            }else {
                if (value.indexOf("@&&@") >= 0){
                    List<String> areaList = Arrays.asList(value.split("@&&@")).stream().map(String::valueOf).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(areaList)){
                        network = areaList.size() >=2 ? areaList.get(1) : "";
                        country = areaList.size() >=1 ? areaList.get(0) : "";
                    }
                }
            }

            if (!ObjectUtils.isEmpty(ipLocation)){
                network = ipLocation.getArea();
                country = ipLocation.getCountry();
                if (ObjectUtils.isEmpty(country)){
                    country = null;
                }
            }
            //获取网络运营商
        } catch (Exception e) {
            LogUtil.error("JWTServiceImpl.saveLoginLog >>查询ip地址失败" + e.getMessage());
            logLogin.setNetworkOperator("0");
        }
        systemLogTool.setNetwork(logLogin, network);
        if (!ObjectUtils.isEmpty(country)) {
            if (country.contains(",IP地址库文件错误")) {
                country = country.replace(",IP地址库文件错误", "");
                LogUtil.info("IP地址库文件解析--->,IP地址库文件错误:ip" + userIp);
            }
            if (country.contains("IP地址库文件错误")) {
                country = country.replace("IP地址库文件错误", "");
            }
            logLogin.setUserIp(userIp);
        }
        SystemLog record = new SystemLog();
        record.setCreateTime(System.currentTimeMillis());
        record.setSessionID("");
        record.setUserID(logLogin.getUserID());
        record.setType(LogTypeEnum.loginSubmit.getCode());

        Map<String, Object> rMap = new HashMap<>(6);
        rMap.put("ua", logLogin.getUserAgent());
        rMap.put("ip", userIp);
        //0 其他 1 电信，2 移动，3 联通，4 网通，5 铁通，6 华数，7 教育网
        rMap.put("network", Arrays.asList("电信","移动","联通","网通","铁通","华数","教育网").contains(network) ? network : "其他");
        rMap.put("country", ObjectUtils.isEmpty(country) ? "" : country);
        rMap.put("op", logLogin.getOperatingSystem());
        rMap.put("browser", logLogin.getBrowser());
        record.setDesc(JsonUtils.beanToJson(rMap));


        record.setClientType(logLogin.getClientType());
        record.setOsName(logLogin.getOperatingSystem());
        record.setVisitDate(new Date());
        try {
            LogUtil.info("systemLogDao insert 插入数据信息:" + JsonUtils.beanToJson(record));
            systemLogDaoImpl.insert(record);
        } catch (Exception e) {
            LogUtil.error(e,"systemLogDao insert 登录日志插入失败 record=" + JsonUtils.beanToJson(record) + ", e: " + e.getMessage());
        }
        Map<String, Object> map = new HashMap<>(9);
        map.put("userID", logLogin.getUserID());
        map.put("lastLogin", logLogin.getGmtLogin());
        //更新登录终端等数据
        try {
            if (log.isInfoEnabled()) {
                int userNum = userJwtDaoImpl.updateLogin(map);
                log.info("setLogLogin   userNum = " + userNum);
            }
        } catch (Exception e) {
            LogUtil.error(e, "setLogLogin  updateLogin  error  !!!!");
        }
    }
}
