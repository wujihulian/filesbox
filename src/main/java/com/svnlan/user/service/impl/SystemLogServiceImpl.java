package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.MyMenuEnum;
import com.svnlan.enums.SortEnum;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.LogDescVo;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.SystemLogDto;
import com.svnlan.user.service.SystemLogService;
import com.svnlan.user.vo.OperateLogExportVO;
import com.svnlan.user.vo.SystemLogExportVO;
import com.svnlan.user.vo.SystemLogVo;
import com.svnlan.utils.DateDUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.svnlan.utils.VisitRecordExecutor.convertAndPopulateNullData;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 14:20
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Resource
    SystemLogDao systemLogDao;
    @Resource
    IoSourceDao ioSourceDao;

    @Override
    public PageResult getSystemLogList(SystemLogDto systemLogDto, LoginUser loginUser){
        Map<String, Object> map = new HashMap<>(2);
        this.searchParamMap( map, systemLogDto);
        LogUtil.info("getSystemLogList  map=" + JsonUtils.beanToJson(map));
        //
        PageHelper.startPage(systemLogDto.getCurrentPage(), systemLogDto.getPageSize());
        List<SystemLogVo> list = this.systemLogDao.getSystemLogList(map);

        if (CollectionUtils.isEmpty(list)) {
            return new PageResult(0L, new ArrayList());
        }
        this.systemLogReturnList(list);

        PageInfo<SystemLogVo> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setList(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    private void systemLogReturnList(List<SystemLogVo> list){

        Set<Long> pID = new HashSet<>();
        LogDescVo descVo = null;
        for (SystemLogVo vo : list){
            vo.setNickname(ObjectUtils.isEmpty(vo.getNickname()) ? vo.getName() : vo.getNickname());
            vo.setAvatar(ObjectUtils.isEmpty(vo.getAvatar()) ? "" : vo.getAvatar());
            if (!ObjectUtils.isEmpty(vo.getDesc())){
                descVo = JsonUtils.jsonToBean(vo.getDesc(), LogDescVo.class);
                if (!ObjectUtils.isEmpty(descVo.getSourceParent()) && descVo.getSourceParent().longValue() > 0){
                    pID.add(descVo.getSourceParent());
                    vo.setParentID(descVo.getSourceParent());
                }
                vo.setLogDescVo(descVo);
            }
        }
        Map<Long, IOSource> pMap = null;
        String space = I18nUtils.tryI18n(MyMenuEnum.rootPath.getCode());
        if (!CollectionUtils.isEmpty(pID)){
            List<IOSource> moveList = ioSourceDao.copySourceList(new ArrayList<>(pID));
            pMap = CollectionUtils.isEmpty(moveList) ? null
                    : moveList.stream().collect(Collectors.toMap(IOSource::getSourceID, Function.identity(), (v1, v2) -> v2));
        }
        if (!ObjectUtils.isEmpty(pMap)){
            for (SystemLogVo vo : list){
                if (!ObjectUtils.isEmpty(vo.getAvatar())){
                    vo.setAvatar(FileUtil.getShowAvatarUrl(vo.getAvatar(), vo.getName()));
                }
                if (!ObjectUtils.isEmpty(vo.getParentID()) && vo.getParentID().longValue() > 0 && pMap.containsKey(vo.getParentID())) {
                    IOSource ps = pMap.get(vo.getParentID());
                    if (ps.getTargetType().intValue() == 1 && ps.getIsFolder().intValue() == 1 && ps.getParentID().longValue() == 0) {
                        // 个人空间
                        vo.setParentName(space);
                    }
                }
            }
        }
    }
    @Override
    public Long getLoginUserCountToday() {
        return systemLogDao.getLoginUserCountToday();
    }

    @Override
    public List<Long> getLoginUserCountDaysBefore(int daysBefore) {
        List<JSONObject> userLoginCountList = systemLogDao.queryUserLoginCount(getTimeRange(daysBefore));
        return convertAndPopulateNullData(userLoginCountList, daysBefore);
    }

    private Pair<Long, Long> getTimeRange(int daysBefore) {
        LocalDateTime now = LocalDateTime.now();
        long startTimestamp = now.minusDays(daysBefore).withHour(0).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.of("+8"));
        long endTimestamp = now.minusDays(1L).withHour(23).withMinute(59).withSecond(59).toEpochSecond(ZoneOffset.of("+8"));
        return Pair.of(startTimestamp, endTimestamp);
    }

    private List<List<Long>> initialDataJsonList(int daysBefore) {

        List<Long> itemList = Stream.iterate(0, it -> it + 1).limit(daysBefore).map(it -> 0L).collect(Collectors.toList());
        List<List<Long>> dataJsonList = new ArrayList<>();
        for (Platform value : Platform.values()) {
            dataJsonList.add(new ArrayList<>(itemList));
        }
        return dataJsonList;
    }

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LONGIN_DEVICE_STAT_KEY = "login_device_stat_key:";

    @Override
    public Object selectUserLoginDeviceStat(int daysBefore, Long userId) {
        Object obj = redisTemplate.opsForValue().get(LONGIN_DEVICE_STAT_KEY + userId + "_" + daysBefore);
        if (Objects.nonNull(obj)) {
            return obj;
        }

        List<JSONObject> jsonList = systemLogDao.queryUserLoginCountStat(getTimeRange(daysBefore), userId);
        // 分为 PC H5 APP 小程序
        List<List<Long>> lists = initialDataJsonList(daysBefore);
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(daysBefore);

        for (JSONObject jsonItem : jsonList) {
            resolvePlatformFromDesc(startDate, lists, jsonItem);
        }
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
        // 表示到今天24点过期
        redisTemplate.opsForValue().set(LONGIN_DEVICE_STAT_KEY + userId + "_" + daysBefore, lists, seconds, TimeUnit.SECONDS);
        return lists;
    }

    private enum Platform {
        PC, H5, APP, MINI_APP, UNKNOWN;
    }

    private void resolvePlatformFromDesc(LocalDate startDate, List<List<Long>> lists, JSONObject jsonItem) {
        String desc = jsonItem.getString("desc");
        JSONObject descObject = JSON.parseObject(desc);
        String op = descObject.getString("op");

        int type = Platform.UNKNOWN.ordinal();

        JSONObject json = JSON.parseObject(desc);
        String browser = json.getString("browser");
        if ("FilesBox".equals(browser) || "App".equals(browser)) {
            // 表示是 app
            type = Platform.APP.ordinal();
        } else if (Objects.nonNull(op)
                && ("Mac OS X".equals(op) || op.startsWith("Windows") || op.toLowerCase().contains("linux"))) {
            // 表示是 pc
            type = Platform.PC.ordinal();
        } else if ("Mac OS X (iPhone)".equals(op)) {
            String ua = descObject.getString("ua");
            if (ua.contains("MicroMessenger")) {
                // 苹果端的小程序
                type = Platform.MINI_APP.ordinal();
            } else {
                // 苹果端的 h5
                type = Platform.H5.ordinal();
            }
        } else if (StringUtils.hasText(op) && op.startsWith("Android")) {
            String ua = jsonItem.getString("ua");
            if (StringUtils.hasText(ua)) {
                if (ua.contains("MiniProgramEnv")) {
                    // 安卓端的小程序
                    type = Platform.MINI_APP.ordinal();
                } else {
                    // 安卓端的 h5
                    type = Platform.H5.ordinal();
                }
            }
        }
        // 某一个类型下的数据
        List<Long> list = lists.get(type);

        Long timestamp = jsonItem.getLong("timestamp");
        LocalDateTime currentLocalDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
        int days = Period.between(startDate, currentLocalDateTime.toLocalDate()).getDays();
        list.set(days, list.get(days) + 1);
    }

    public void searchParamMap(Map<String, Object> map, SystemLogDto systemLogDto) {
        if (!ObjectUtils.isEmpty(systemLogDto.getUserID())) {
            map.put("userID", systemLogDto.getUserID());
        }
        if (!ObjectUtils.isEmpty(systemLogDto.getLogType())) {
            if (systemLogDto.getLogType().indexOf(",") < 0){
                map.put("type", systemLogDto.getLogType());
            }else {
                List<String> typeList = Arrays.asList(systemLogDto.getLogType().split(",")).stream().map(String::valueOf).collect(Collectors.toList());
                map.put("typeList", typeList);
            }
        }
        if (!ObjectUtils.isEmpty(systemLogDto.getTimeFrom())) {
            Date timeFrom = DateDUtil.strToDate("yyyy-MM-dd", systemLogDto.getTimeFrom());
            map.put("minDate", timeFrom.getTime()/1000);
        }
        if (!ObjectUtils.isEmpty(systemLogDto.getTimeTo())) {
            Date timeTo = DateDUtil.strToDate( "yyyy-MM-dd HH:mm:ss", systemLogDto.getTimeTo() + " 23:59:59");
            map.put("maxDate", timeTo.getTime()/1000);
        }
        map.put("sortType", SortEnum.getSortType(systemLogDto.getSortType()));
    }

    @Override
    public List<SystemLogExportVO> getSystemLogExportList(SystemLogDto systemLogDto){
        List<SystemLogVo> list = this.getSystemLogSearchExportList(systemLogDto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<SystemLogExportVO> reList = systemLogReturnExportList(list);
        return reList;
    }
    private List<SystemLogVo> getSystemLogSearchExportList(SystemLogDto systemLogDto){
        Map<String, Object> map = new HashMap<>(2);
        this.searchParamMap( map, systemLogDto);
        LogUtil.info("getSystemLogExportList  map=" + JsonUtils.beanToJson(map));
        return this.systemLogDao.getSystemLogExportList(map);
    }
    private List<SystemLogExportVO> systemLogReturnExportList(List<SystemLogVo> list){
        List<SystemLogExportVO> reList = new ArrayList<>();
        SystemLogExportVO exportVO = null;
        LogDescVo descVo = null;
        for (SystemLogVo vo : list){
            exportVO = new SystemLogExportVO();
            if (String.valueOf(vo.getCreateTime()).length() == 10){
                exportVO.setLoginTime(DateDUtil.DateToStr(DateDUtil.YYYY_MM_DD_HH_MM_SS, new Date(Long.parseLong(vo.getCreateTime()+"000"))));
            }else {
                exportVO.setLoginTime(DateDUtil.DateToStr(DateDUtil.YYYY_MM_DD_HH_MM_SS, new Date(vo.getCreateTime())));
            }
            exportVO.setName(ObjectUtils.isEmpty(vo.getNickname()) ? vo.getName() : vo.getNickname());
            exportVO.setSystem(vo.getOsName());
            if (!ObjectUtils.isEmpty(vo.getDesc())){
                descVo = JsonUtils.jsonToBean(vo.getDesc(), LogDescVo.class);
                if (!ObjectUtils.isEmpty(descVo)) {
                    exportVO.setClient(descVo.getBrowser());
                    exportVO.setAddress(descVo.getCountry());
                }
            }
            reList.add(exportVO);
        }
        return reList;
    }

    @Override
    public List<OperateLogExportVO> getOperateLogExportList(SystemLogDto systemLogDto){
        List<SystemLogVo> list = this.getSystemLogSearchExportList(systemLogDto);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<OperateLogExportVO> reList = operateLogReturnExportList(list);
        return reList;
    }

    private List<OperateLogExportVO> operateLogReturnExportList(List<SystemLogVo> list){
        List<OperateLogExportVO> reList = new ArrayList<>();
        OperateLogExportVO exportVO = null;
        LogDescVo descVo = null;
        for (SystemLogVo vo : list){
            exportVO = new OperateLogExportVO();if (String.valueOf(vo.getCreateTime()).length() == 10){
                exportVO.setLoginTime(DateDUtil.DateToStr(DateDUtil.YYYY_MM_DD_HH_MM_SS, new Date(Long.parseLong(vo.getCreateTime()+"000"))));
            }else {
                exportVO.setLoginTime(DateDUtil.DateToStr(DateDUtil.YYYY_MM_DD_HH_MM_SS, new Date(vo.getCreateTime())));
            }
            exportVO.setName(ObjectUtils.isEmpty(vo.getNickname()) ? vo.getName() : vo.getNickname());
            if (!ObjectUtils.isEmpty(vo.getDesc())){
                descVo = JsonUtils.jsonToBean(vo.getDesc(), LogDescVo.class);
                if (!ObjectUtils.isEmpty(descVo)) {
                    exportVO.setAddress(descVo.getCountry());
                    exportVO.setOperate(LogTypeEnum.getValueByCode(vo.getType()));
                    exportVO.setDesc(this.getOperateDescString(vo, descVo, vo.getType()));
                }
            }
            reList.add(exportVO);
        }
        return reList;
    }
    private String getOperateDescString(SystemLogVo vo,LogDescVo descVo, String operateType){

        if (Arrays.asList("user.index.loginSubmit", "user.index.logout", "explorer.fav.del","explorer.index.fileDownload"
                , "explorer.index.zipDownload", "file.shareLinkAdd", "file.shareLinkEdit", "file.shareLinkRemove", "file.shareToAdd", "file.shareEdit").contains(operateType)){
            switch (operateType) {
                case "user.index.loginSubmit":
                    return vo.getOsName()  + (ObjectUtils.isEmpty(descVo.getBrowser()) ? "" : " " + descVo.getBrowser());
                case "user.index.logout":
                    return I18nUtils.i18n("logs-detail-user") + (ObjectUtils.isEmpty(vo.getNickname())  ? vo.getName() : vo.getNickname());
                case "explorer.fav.del":
                    return I18nUtils.i18n("logs-detail-favDel") + " " + this.pathName(descVo.getPathName());
                case "explorer.index.fileDownload":
                case "explorer.index.zipDownload":
                    return String.format(I18nUtils.i18n("log-event-down-item"),this.pathName(descVo.getFromName()), this.pathName(descVo.getPathName())) ;
                case "file.shareLinkAdd":
                    return String.format(I18nUtils.i18n("log-event-share-shareLinkAdd-item"), this.pathName(descVo.getPathName()));
                case "file.shareLinkEdit":
                case "file.shareEdit":
                    return String.format(I18nUtils.i18n("log-event-share-shareEdit-item"), this.pathName(descVo.getPathName()));
                case "file.shareLinkRemove":
                    return String.format(I18nUtils.i18n("log-event-share-shareLinkRemove-item"), this.pathName(descVo.getPathName()));
                case "file.shareToAdd":
                    return String.format(I18nUtils.i18n("log-event-share-shareToAdd-item"), this.pathName(descVo.getPathName()));
                case "file.shareToRemove":
                    return String.format(I18nUtils.i18n("log-event-share-shareToRemove-item"), this.pathName(descVo.getPathName()));
                default:
                    return "";
            }
        }else {
            switch (descVo.getType()){
                case "mkdir":
                case "mkfile":
                case "editFile":
                case "favAdd":
                case "rename":
                case "uploadNew":
                    return I18nUtils.i18n("logs-detail-" + descVo.getType()) + " " + this.pathName(descVo.getPathDisplay(), "/")+ descVo.getPathName();
                case "upload":
                    return I18nUtils.i18n("logs-detail-" + operateType) + " " + this.pathName(descVo.getPathDisplay(), "/")+ descVo.getPathName();
                case "remove":
                    return I18nUtils.i18n("logs-detail-remove") + " [" + descVo.getPathName()+"]";
                case "recycle":
                    return I18nUtils.i18n("logs-detail-moveFrom-toRecycle") + " [" + descVo.getPathName()+"]" + I18nUtils.i18n("logs-detail-moveTo-toRecycle");
                case "restore":
                    return I18nUtils.i18n("logs-detail-moveFrom-restore") + this.pathName(descVo.getPathDisplay(), "/")+ descVo.getPathName() + I18nUtils.i18n("logs-detail-moveTo-restore");
                case "copy":
                    return I18nUtils.i18n("logs-detail-" + operateType) + this.pathName(descVo.getPathDisplay(), "/")+ descVo.getPathName() ;
                case "move":
                    return I18nUtils.i18n("logs-detail-move") + descVo.getPathName() + I18nUtils.i18n("logs-detail-from") + this.pathName(descVo.getFromName()) + I18nUtils.i18n("logs-detail-moveTo") + descVo.getSourceParentName();
                case "moveOut":
                    return I18nUtils.i18n("logs-detail-from") + this.pathName(descVo.getFromName()) + I18nUtils.i18n("logs-detail-moveOut") + descVo.getPathName();
                default:
                    return "";
            }
        }
    }

    private String pathName(String value){
        return pathName(value, "");
    }
    private String pathName(String value, String s){
        return ObjectUtils.isEmpty(value) ? "" : value+ s;
    }
}
