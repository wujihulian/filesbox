package com.svnlan.webdav.auth;

import com.ithit.webdav.server.DavRequest;
import com.ithit.webdav.server.DavResponse;
import com.svnlan.common.GlobalConfig;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.IpUtil;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PasswordUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.svnlan.webdav.impl.WebDavEngine.userVoThreadLocal;

/**
 * webdav 认证
 *
 * @author lingxu 2023/04/14 14:57
 */
@Slf4j
public class WebdavAuthenticator implements Authenticator {

    @Setter
    private UserDao userDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean doAuthenticate(DavRequest davRequest, DavResponse davResponse) {

        if (checkForCachedAuthentication(davRequest, davResponse)) {
            // 判断是否已经认证过了
            return true;
        }
        String authorization = davRequest.getHeader("authorization");

        String token = davRequest.getHeader("token");


        if (StringUtils.hasText(authorization) && (authorization.length() > 6)) {
            String authToken = authorization.substring(6);

            String decodedAuth = getFromBASE64(authToken);
            log.info("***************** auth decoded from base64 is {}  token {}", decodedAuth, token);
            String[] splits = decodedAuth.split(":");
            // 通过用户名查询用户信息
            UserVo userVo = userDao.getUserByUserName(splits[0]);
            if (Objects.isNull(userVo) || !Objects.equals(userVo.getStatus(), 1)) {
                LogUtil.info(" doAuthenticate --------------*****************" +decodedAuth+" user is null or status!=1");
                failToLogin(davResponse);
                return false;
            }
            boolean isClear = false;
            String name = splits[0];
            String pwd = null;
            if (splits.length > 1) {
                LogUtil.info(" doAuthenticat e -----------*****************" +decodedAuth+" splits length > 1");
                pwd = splits[1];
            } else {
                isClear = true;
                LogUtil.info(" doAuthenticat e -----------*****************" +decodedAuth+" splits length == 1");
                pwd = getRedisUserLoginInfo(davRequest,name);
            }

            if (!ObjectUtils.isEmpty(pwd)){
                if (!PasswordUtil.passwordEncode(pwd, GlobalConfig.PWD_SALT).equals(userVo.getPassword())) {
                    if (isClear) {
                        clearRedisUserLoginInfo(davRequest, name);
                    }
                    failToLogin(davResponse);
                    return false;
                }
            }else {
                failToLogin(davResponse);
                return false;
            }

            setRedisUserLoginInfo(davRequest, name, pwd);
            //userVo.setPassword(null); 注释，密码用于判断后续密码是否修改
            CaffeineUtil.AUTHORIZATION_USERID.put(authorization, userVo);
            userVo.setPassword(null);
            userVoThreadLocal.set(userVo);
            return true;
        } else {
            LogUtil.info(" doAuthenticate ----------*****************" +authorization+" authorization length < 6  token=" + token);
            failToLogin(davResponse);
            return false;
        }

    }

    private void setRedisUserLoginInfo(DavRequest davRequest, String name, String pwd){
        String ua = davRequest.getHeader("User-Agent");
        String ip = IpUtil.getDavIp(davRequest);
        String key = name+ "_" + PasswordUtil.MD5(ua+"_"+ip, GlobalConfig.DAV_SALT);
        LogUtil.info("dav doAuthenticate setRedisUserLoginInfo key=" + key + "，"+pwd);
        stringRedisTemplate.opsForValue().set(GlobalConfig.dav_login_key +  key, pwd, 30,TimeUnit.DAYS);
    }

    private void clearRedisUserLoginInfo(DavRequest davRequest, String name){
        String ua = davRequest.getHeader("User-Agent");
        String ip = IpUtil.getDavIp(davRequest);
        String key = name+ "_" + PasswordUtil.MD5(ua+"_"+ip, GlobalConfig.DAV_SALT);
        LogUtil.info("dav doAuthenticate clearRedisUserLoginInfo key=" + key );
        stringRedisTemplate.delete(GlobalConfig.dav_login_key +  key);
    }

    private String getRedisUserLoginInfo(DavRequest davRequest, String name){
        String ua = davRequest.getHeader("User-Agent");
        String ip = IpUtil.getDavIp(davRequest);
        String key = name+ "_" + PasswordUtil.MD5(ua+"_"+ip, GlobalConfig.DAV_SALT);
        String pwd = stringRedisTemplate.opsForValue().get(GlobalConfig.dav_login_key +  key);
        LogUtil.info("dav doAuthenticate getRedisUserLoginInfo key=" + key + ",ppp=" + pwd );
        return pwd;
    }

    private void failToLogin(DavResponse davResponse) {
        LogUtil.info("failToLogin ********************************** ");
        davResponse.setHeader("Cache-Control", "no-cache,no-store, must-revalidate");
        davResponse.setHeader("Pragma", "no-cache");
        davResponse.setHeader("Expires", "0");
        davResponse.setStatus(401, "");
        davResponse.setHeader("WWW-authenticate", "Basic realm=\"请输入密码\"");
    }

}
