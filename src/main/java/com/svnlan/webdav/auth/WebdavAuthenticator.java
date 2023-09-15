package com.svnlan.webdav.auth;

import com.ithit.webdav.server.DavRequest;
import com.ithit.webdav.server.DavResponse;
import com.svnlan.common.GlobalConfig;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.PasswordUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.util.Objects;

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

    @Override
    public boolean doAuthenticate(DavRequest davRequest, DavResponse davResponse) {


        if (checkForCachedAuthentication(davRequest, davResponse)) {
            // 判断是否已经认证过了
            return true;
        }

        String authorization = davRequest.getHeader("authorization");

        if (StringUtils.hasText(authorization) && (authorization.length() > 6)) {
            String authToken = authorization.substring(6);

            String decodedAuth = getFromBASE64(authToken);
            log.info("auth decoded from base64 is {}", decodedAuth);
            String[] splits = decodedAuth.split(":");
            // 通过用户名查询用户信息
            UserVo userVo = userDao.getUserByUserName(splits[0]);
            if (Objects.isNull(userVo) || !Objects.equals(userVo.getStatus(), 1)) {
                failToLogin(davResponse);
                return false;
            }
            if (splits.length > 1) {
                if (!PasswordUtil.passwordEncode(splits[1], GlobalConfig.PWD_SALT).equals(userVo.getPassword())) {
                    failToLogin(davResponse);
                    return false;
                }
            } else {
                // length = 1 ， 通过命令行挂载的时候，会出现这种情况
                return false;
            }

            userVo.setPassword(null);
            CaffeineUtil.AUTHORIZATION_USERID.put(authorization, userVo);
            userVoThreadLocal.set(userVo);
            return true;
        } else {
            failToLogin(davResponse);
            return false;
        }

    }

    private void failToLogin(DavResponse davResponse) {
        davResponse.setStatus(401, "");
        davResponse.setHeader("WWW-authenticate", "Basic realm=\"请输入密码\"");
        davResponse.setHeader("Expires", "0");
    }

    private String getFromBASE64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}
