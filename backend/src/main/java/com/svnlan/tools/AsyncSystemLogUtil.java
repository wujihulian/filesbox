package com.svnlan.tools;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.OperatingSystemEnum;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.ParentPathDisplayVo;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.domain.LogLogin;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.SystemLog;
import com.svnlan.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/21 14:05
 */
@Slf4j
@Component
public class AsyncSystemLogUtil {
    @Resource
    HomeExplorerDao homeExplorerDaoImpl;
    @Resource
    private SystemLogDao systemLogDaoImpl;
    @Resource
    private IoSourceMetaDao ioSourceMetaDaoImpl;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Async(value = "asyncTaskExecutor")
    public void setSysLog(LoginUser loginUser, String opType, List<Map<String, Object>> paramList, String serverName, String ua
            , String resolution, String ip, Long tenantId, String sessionID, String country) {
        LogUtil.info("setSysLog 日志保存begin opType=" + opType + "，paramList=" + JsonUtils.beanToJson(paramList));

        ip = ObjectUtils.isEmpty(ip) ? "" : ip;
        loginUser.setIp(ip);
        LogLogin logLogin = new LogLogin();
        logLogin.setSex("1");
        logLogin.setUserIp(ip);
        logLogin.setDomain(serverName);
        logLogin.setGmtLogin(System.currentTimeMillis());
        logLogin.setName(loginUser.getName());
        logLogin.setNickname(ObjectUtils.isEmpty(loginUser.getNickname()) ? loginUser.getName() : loginUser.getNickname());
        logLogin.setUserID(loginUser.getUserID());
        logLogin.setAvatar(loginUser.getAvatar());
        logLogin.setSessionID(sessionID);
        if (!ObjectUtils.isEmpty(loginUser.getSex())){
            logLogin.setSex(loginUser.getSex().toString());
        }

        if (resolution != null) {
            logLogin.setResolution(resolution);
        } else {
            logLogin.setResolution("");
        }
        if (ua == null) {
            logLogin.setBrowser("");
            logLogin.setNetworkOperator("");
            logLogin.setOperatingSystem("");
        } else {
            // 获取 设备类型 操作系统
            Pair<Integer, String> pair = getClientTypeAndOsName(ua);
            //操作系统的名字
            logLogin.setOperatingSystem(pair.getSecond());
            logLogin.setClientType(pair.getFirst() + "");
            //保存ua
            if (ua.length() > 512) {
                String string = ua.subSequence(0, 512).toString();
                logLogin.setUserAgent(string);
            } else {
                logLogin.setUserAgent(ua);
            }
        }
        logLogin.setType(opType);


        this.addSysLog(logLogin, paramList, tenantId, country);
        LogUtil.info("setSysLog 日志保存end opType=" + opType + "，paramList=" + JsonUtils.beanToJson(paramList));
    }


    /**
     * 获取用户的设备类型 和 操作系统
     * 1pc , 2h5, 3安卓app, 4 ios-app, 5小程序
     */
    public Pair<Integer, String> getClientTypeAndOsName(HttpServletRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            request = requestAttributes.getRequest();
        }
        String ua = request.getHeader("User-Agent");
        return getClientTypeAndOsName(ua);
    }

    public Pair<Integer, String> getClientTypeAndOsName(String ua) {

        // 1 pc , 2 h5, 3 安卓app, 4 ios-app, 5 小程序, 6 电脑app, 7 其他
        int clientType = -1;
        // 设备操作系统
        String borderGroup = UserAgentUtils.getBorderGroup(ua);
        String osName = UserAgentUtils.getOsName(ua);
        LogUtil.info("osName => " + osName);
        OperatingSystemEnum systemEnum = OperatingSystemEnum.checkAndGet(osName);
        if (systemEnum == OperatingSystemEnum.MAC_OS_IPHONE) {
            // ios 操作系统
            osName = "iOS";
            if ("Unknown".equals(borderGroup)) {
                // 表示是 iphone 的 APP
                clientType = 4;
            } else {
                // 表示是 iphone 的 h5;
                clientType = 2;
                if (ua.contains("MicroMessenger")) {
                    // 表示是 iphone 端的 小程序
                    clientType = 5;
                }
            }
        } else if (systemEnum == OperatingSystemEnum.ANDROID) {
            osName = "Android";
            if (ua.endsWith("WUJIAPP")) {
                // 表示是 安卓 app
                clientType = 3;
            } else if (ua.contains("MicroMessenger")) {
                // 表示是 android 端的 小程序
                clientType = 5;
            } else {
                // 表示是 安卓 h5
                clientType = 2;
            }
        } else {
            if (systemEnum == OperatingSystemEnum.WINDOWS) {
                // 表示是电脑网页访问
                osName = "Windows10";
                clientType = 1;
            } else if (systemEnum == OperatingSystemEnum.MAC_OS_X) {
                // 表示是电脑网页访问
                osName = "MacOSX";
                clientType = 1;
            } else {
                log.info("ua => {}, clientType => {},  osName => unknown? {}", ua, clientType, osName);
                clientType = 7;
            }
        }
        log.info("clientType => {},  osName => {}", clientType, osName);
        // 去掉中间的空格，比如 Windows 10 需要变为 Windows10
        return Pair.of(clientType, osName.replace(" ", ""));
    }

    public void addSysLog(LogLogin logLogin, List<Map<String, Object>> paramList,Long tenantId, String country) {
        String userIp = logLogin.getUserIp().split(",")[0];
        String network = "";
        if (ObjectUtils.isEmpty(country)){
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
                LogUtil.error(" setSysLog >>查询ip地址失败" + e.getMessage());
                logLogin.setNetworkOperator("0");
            }
        }

        setNetwork(logLogin, network);
        if (!ObjectUtils.isEmpty(country)) {
            if (country.contains(",IP地址库文件错误")) {
                country = country.replace(",IP地址库文件错误", "");
                LogUtil.info("IP地址库文件解析 systemLog--->,IP地址库文件错误:ip" + userIp);
            }
            if (country.contains("IP地址库文件错误")) {
                country = country.replace("IP地址库文件错误", "");
            }
            logLogin.setUserIp(userIp);
        }


        Map<String, Object> rMap = new HashMap<>(6);
        rMap.put("ua", logLogin.getUserAgent());
        rMap.put("ip", userIp);
        //0 其他 1 电信，2 移动，3 联通，4 网通，5 铁通，6 华数，7 教育网
        rMap.put("network", Arrays.asList("电信", "移动", "联通", "网通", "铁通", "华数", "教育网").contains(network) ? network : "其他");
        rMap.put("country", ObjectUtils.isEmpty(country) ? "" : country);
        rMap.put("browser", logLogin.getBrowser());
        rMap.put("userID", logLogin.getUserID());
        rMap.put("name", logLogin.getName());
        rMap.put("nickName", logLogin.getNickname());

        Date visitDate = new Date();
        List<SystemLog> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(paramList)) {
            Map<Long, String> parentPathDisplayMap = null;
            Set<String> pLevelList = new HashSet<>();
            int checkSpace = 0;
            for (Map map : paramList) {
                if (map.containsKey("sourceParentLevel") && !ObjectUtils.isEmpty(map.get("sourceParentLevel"))) {

                    // 个人空间
                    HomeExplorerVO space = this.mySpaceDefault(logLogin.getUserID(), tenantId);
                    // space.getSourceID()

                    if (map.get("sourceParentLevel").toString().equals(",0," + space.getSourceID() + ",")) {
                        checkSpace = 1;
                    } else if (map.get("sourceParentLevel").toString().startsWith(",0," + space.getSourceID() + ",")) {
                        checkSpace = 2;
                    }
                    pLevelList.add(map.get("sourceParentLevel").toString());
                }
            }
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = this.getParentPathDisplayMap(pLevelList);
            }
            for (Map map : paramList) {
                if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                    if (map.containsKey("sourceParentLevel") && !ObjectUtils.isEmpty(map.get("sourceParentLevel"))) {
                        String pathDisplay = this.setParentPathDisplay(parentPathDisplayMap, map.get("sourceParentLevel").toString(), 0L);
                        map.put("pathDisplay", pathDisplay);
                        if (checkSpace == 1) {
                            map.put("pathDisplay", I18nUtils.i18n("explorer.toolbar.rootPath"));
                        } else if (checkSpace == 1) {
                        }else if (checkSpace == 2){
                            map.put("pathDisplay", I18nUtils.i18n("explorer.toolbar.rootPath") + pathDisplay.substring(pathDisplay.indexOf("/"), pathDisplay.length()));
                        }
                    }
                }
                if (Arrays.asList("explorer.index.zipDownload", "explorer.index.fileDownload").contains(logLogin.getType())
                        && map.containsKey("fromSourceID")) {
                    map.put("fromName", ioSourceDao.getSourceName(Long.parseLong(map.get("fromSourceID").toString())));
                }
                map.putAll(rMap);
                if (!ObjectUtils.isEmpty(logLogin.getSessionID())){
                    list.add(new SystemLog(logLogin.getUserID(), logLogin.getType(), JsonUtils.beanToJson(map), logLogin.getOperatingSystem(), visitDate, logLogin.getClientType(), logLogin.getSessionID()));
                }else {
                    list.add(new SystemLog(logLogin.getUserID(), logLogin.getType(), JsonUtils.beanToJson(map), logLogin.getOperatingSystem(), visitDate, logLogin.getClientType()));
                }
            }
        } else {
            SystemLog record = new SystemLog();
            record.setCreateTime(System.currentTimeMillis());
            record.setSessionID("");
            record.setUserID(logLogin.getUserID());
            record.setType(logLogin.getType());
            record.setClientType(logLogin.getClientType());
            record.setOsName(logLogin.getOperatingSystem());
            record.setVisitDate(new Date());
            record.setDesc(JsonUtils.beanToJson(rMap));
            list.add(record);
        }
        try {
            LogUtil.info("systemLogDao batchInsert 插入数据信息:" + JsonUtils.beanToJson(list));
            systemLogDaoImpl.batchInsert(list);
        } catch (Exception e) {
            LogUtil.error("systemLogDao batchInsert 登录日志插入失败 record=" + JsonUtils.beanToJson(list) + ", e: " + e.getMessage());
        }
    }

    public String setParentPathDisplay(Map<Long, String> map, String parentLevel, Long spaceID) {

        List<Long> idList = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return "";
        }
        List<String> nameList = new ArrayList<>();
        for (Long id : idList) {
            if (map.containsKey(id)) {
                if (id.longValue() == spaceID) {
                    nameList.add(I18nUtils.tryI18n("explorer.toolbar.rootPath"));
                } else {
                    nameList.add(map.get(id));
                }
            }
        }
        return CollectionUtils.isEmpty(nameList) ? "" : StringUtil.joinString(nameList, "/");
    }

    public Map<Long, String> getParentPathDisplayMap(Set<String> pList) {
        Map<Long, String> map = null;
        if (!CollectionUtils.isEmpty(pList)) {

            List<Long> idSplitList = null;
            Set<Long> ids = new HashSet<>();
            for (String pId : pList){
                if (",0,".equals(pId)){
                    continue;
                }
                idSplitList = Arrays.asList(pId.split(",")).stream().filter(n-> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                ids.addAll(idSplitList);
            }
            if (CollectionUtils.isEmpty(ids)){
                return null;
            }

            List<ParentPathDisplayVo> list = ioSourceDao.getParentPathDisplayByIds(new ArrayList<>(ids));
            if (!CollectionUtils.isEmpty(list)) {
                map = list.stream().collect(Collectors.toMap(ParentPathDisplayVo::getSourceID, ParentPathDisplayVo::getName, (v1, v2) -> v2));
            }
        }
        return map;
    }

    public HomeExplorerVO mySpaceDefault(Long userID, Long tenantId) {

        HomeExplorerVO space = homeExplorerDaoImpl.getHomeSpace(userID, 0L);
        List<IOSourceMeta> pyList = new ArrayList<>();
        String spaceSourceIDStr = ioSourceMetaDaoImpl.getSourceIDMetaByKey(userID + "", "mySpace", tenantId);
        // 个人空间
        if (ObjectUtils.isEmpty(spaceSourceIDStr)) {
            pyList.add(new IOSourceMeta(space.getSourceID(), "mySpace", userID + ""));
            try {
                ioSourceMetaDaoImpl.batchInsert(pyList);
            } catch (Exception e) {
                LogUtil.error(e, " setUserDefaultSource mySpaceDefault meta 1 error pyList=" + JsonUtils.beanToJson(pyList));
            }

        }
        return space;
    }

    /**
     * 0 其他 1 电信，2 移动，3 联通，4 网通，5 铁通，6 华数，7 教育网
     */
    public void setNetwork(LogLogin logLogin, String address) {
        if (address.contains("电信")) {
            logLogin.setNetworkOperator("1");
        } else if (address.contains("移动")) {
            logLogin.setNetworkOperator("2");
        } else if (address.contains("联通")) {
            logLogin.setNetworkOperator("3");
        } else if (address.contains("网通")) {
            logLogin.setNetworkOperator("4");
        } else if (address.contains("铁通")) {
            logLogin.setNetworkOperator("5");
        } else if (address.contains("华数")) {
            logLogin.setNetworkOperator("6");
        } else if (address.contains("教育网")) {
            logLogin.setNetworkOperator("7");
        } else {
            logLogin.setNetworkOperator("0");
        }
    }
}
