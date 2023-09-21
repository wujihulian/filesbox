package com.svnlan.user.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.AuthEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.vo.TargetSpaceVo;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserOptionDao;
import com.svnlan.user.service.NoticeService;
import com.svnlan.user.service.RightsService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.DateDUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 13:50
 */
@Slf4j
@Service
public class RightsServiceImpl implements RightsService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserOptionDao userOptionDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemOptionDao systemOptionDao;
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    OptionTool optionTool;
    @Value("${disk.default.size}")
    private Long diskDefaultSize;

    @Resource
    private NoticeService noticeService;

    @Override
    public Map<String, Object> getUserOptions(LoginUser loginUser, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.loginFirst.getCode());
        }
        Map<String, Object> reMap = new HashMap<>(2);

        long userID = loginUser.getUserID();
        UserVo userVo = userDao.getUserInfo(userID);
        List<OptionVo> userSystemOptionList = userOptionDao.getUserSystemConfig(userID);

        /** 插件列表 */
        reMap.put("pluginList", optionTool.pluginList());

        /** 用户信息 */
        reMap.put("config", optionTool.optionDataMap(userSystemOptionList));
        Map<String, Object> roleMap = null;

        String userRoleKey = GlobalConfig.userRoleAuth_key;
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        String value = operations.get(userRoleKey, userID + "");

        if (!ObjectUtils.isEmpty(value)) {
            try {
                roleMap = JsonUtils.jsonToMap(value);
            } catch (Exception e) {
                LogUtil.error(e, "getUserOptions 缓存解析失败 key=" + userRoleKey + "，userID=" + userID + "，value=" + value);
            }
        }
        if (!ObjectUtils.isEmpty(userVo.getAdministrator()) && userVo.getAdministrator().intValue() == 1) {
            if (ObjectUtils.isEmpty(roleMap)) {
                roleMap = AuthEnum.getUserAuthMap(GlobalConfig.SYSTEM_AUTH);
            }
            reMap.put("role", roleMap);
            reMap.put("userType", 1);
        } else {
            if (ObjectUtils.isEmpty(roleMap)) {
                roleMap = ObjectUtils.isEmpty(userVo.getAuth()) ? null : AuthEnum.getUserAuthMap(userVo.getAuth());
            }
            reMap.put("role", roleMap);
            int userType = 3;
            if (!ObjectUtils.isEmpty(userVo.getAuth()) && userVo.getAuth().indexOf("admin") >= 0) {
                userType = 2;
            }
            reMap.put("userType", userType);
        }
        if (!ObjectUtils.isEmpty(roleMap)) {
            operations.put(userRoleKey, userID + "", JsonUtils.beanToJson(roleMap));
            operations.getOperations().expire(userRoleKey, 10, TimeUnit.HOURS);
        }
        userVo.setAuth(null);
        userVo.setSizeMax(userVo.getSizeMax() <= 0 ? diskDefaultSize.doubleValue() : userVo.getSizeMax());
        // 查询用户三方绑定信息
        userVo.setDingOpenId(StringUtils.hasText(userVo.getDingOpenId()) ? "1" : "0");
        userVo.setWechatOpenId(StringUtils.hasText(userVo.getWechatOpenId()) ? "1" : "0");
        userVo.setAlipayOpenId(StringUtils.hasText(userVo.getAlipayOpenId()) ? "1" : "0");
        userVo.setEnWechatOpenId(StringUtils.hasText(userVo.getEnWechatOpenId()) ? "1" : "0");
        if (!ObjectUtils.isEmpty(userVo.getAvatar())){
            userVo.setAvatar(FileUtil.getShowAvatarUrl(userVo.getAvatar(), userVo.getName()));
        }
        reMap.put("user", userVo);
        reMap.put("editorConfig", "");
        String lang = request.getHeader(SystemConstant.LANG);
        reMap.put("lang", ObjectUtils.isEmpty(lang) ? SystemConstant.DEFAULT_LANG : lang);
        reMap.put("targetSpace", new TargetSpaceVo(userVo.getSizeMax(), userVo.getSizeUse()));
//        // 查询用户未读的消息数
//        log.info("查询用户未读的消息数 ===========");
//        Integer total = noticeService.hasNoticeUnread();
//        log.info("未读消息数 total => {}", total);
//        reMap.put("unreadCount", total);
        // 水印
        Map<String, String> wmMap = new HashMap<>(18);
        List<String> search = Arrays.asList("markType","wmTitle","wmColor","wmFont","wmSize","wmSubTitle","wmSubColor","wmSubFont","wmSubSize","wmTransparency","wmRotate"
                ,"wmMargin","wmPicPath","wmPicSize","wmPosition","needUploadCheck", "showFileLink", "shareLinkAllow", "shareLinkAllowGuest","treeOpen");

        List<OptionVo> list = systemOptionDao.getSystemConfigByKeyList(search);
        for (OptionVo vo : list){
            if (vo.getKey().equals("treeOpen")){
                reMap.put("treeOpen", ObjectUtils.isEmpty(vo.getValue()) ? "" : vo.getValue());
            }else if (vo.getKey().equals("wmTitle")){
                wmMap.put(vo.getKey(), ObjectUtils.isEmpty(vo.getValue()) ? "" : this.replaceMarkSubTitle(vo.getValue(), loginUser, userVo.getLastLogin()));
            }else if (vo.getKey().equals("wmPicPath")){
                wmMap.put(vo.getKey(), ObjectUtils.isEmpty(vo.getValue()) ? "" : vo.getValue());
                wmMap.put(vo.getKey()+"ShowPath", FileUtil.getShowImageUrl(vo.getValue(), vo.getKey()+".png"));
            }else if (vo.getKey().equals("wmSubTitle")){
                wmMap.put(vo.getKey(), ObjectUtils.isEmpty(vo.getValue()) ? "" : this.replaceMarkSubTitle(vo.getValue(), loginUser, userVo.getLastLogin()));
            }else {
                wmMap.put(vo.getKey(), ObjectUtils.isEmpty(vo.getValue()) ? "" : vo.getValue());
                if ("markType".equals(vo.getKey()) && "0".equals(vo.getValue())){
                    // 无水印
                }
            }

        }
        reMap.put("markConfig", wmMap);
        // 桌面ID
        HomeExplorerVO commonSource = sourceOperateTool.myDesktopDefault(loginUser.getUserID());
        reMap.put("desktop", commonSource);
        reMap.put("desktopBackImg", "");
        OptionVo backImgVo = userOptionDao.getUserConfigVoByKey(loginUser.getUserID(), "desktopBackgroundImg");
        if (!ObjectUtils.isEmpty(backImgVo) && !ObjectUtils.isEmpty(backImgVo.getValue()) ){
            reMap.put("desktopBackImg", backImgVo.getValue());
        }
        return reMap;
    }

    private String replaceMarkSubTitle(String subTitle, LoginUser loginUser, Long lastLoginTime){
        if (ObjectUtils.isEmpty(subTitle)){
            return "";
        }
        return subTitle.replace("{userLoginTime}",DateDUtil.getYearMonthDayHMS(new Date(lastLoginTime), "yyyy-MM-dd HH:mm"))
                .replace("{userNickName}", (ObjectUtils.isEmpty(loginUser.getNickname()) ? loginUser.getName() : loginUser.getNickname()))
                .replace("{userName}", loginUser.getName())
                .replace("{userID}", loginUser.getUserID()+"")
                .replace("{timeDay}", DateDUtil.getYearMonthDayHMS(new Date(), "yyyy-MM-dd"))
                ;
    }
}
