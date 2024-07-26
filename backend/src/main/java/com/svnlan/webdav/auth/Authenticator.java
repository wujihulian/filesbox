package com.svnlan.webdav.auth;

import com.ithit.webdav.server.DavRequest;
import com.ithit.webdav.server.DavResponse;
import com.svnlan.common.GlobalConfig;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.PasswordUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.util.Objects;

import static com.svnlan.webdav.impl.WebDavEngine.userVoThreadLocal;

public interface Authenticator {
    boolean doAuthenticate(DavRequest davRequest, DavResponse davResponse);

    default boolean checkForCachedAuthentication(DavRequest davRequest, DavResponse davResponse) {
        String auth = davRequest.getHeader("authorization");
        if (!StringUtils.hasText(auth)) {
            return false;
        }
        UserVo userVo = CaffeineUtil.AUTHORIZATION_USERID.getIfPresent(auth);
        boolean nonNull = Objects.nonNull(userVo);
        if (nonNull) {
            // 密码是否一致：密码修改后重新登录
            if (!ObjectUtils.isEmpty(userVo.getPassword())){
                try {
                    if (StringUtils.hasText(auth) && (auth.length() > 6)) {
                        String authToken = auth.substring(6);
                        String decodedAuth = getFromBASE64(authToken);
                        String[] splits = decodedAuth.split(":");
                        if (!PasswordUtil.passwordEncode(splits[1], GlobalConfig.PWD_SALT).equals(userVo.getPassword())) {
                            return false;
                        }
                    }
                }catch (Exception e){
                    return false;
                }
            }
            // 重新写入缓存，也就是过期时间重置
            CaffeineUtil.AUTHORIZATION_USERID.put(auth, userVo);
            userVoThreadLocal.set(userVo);
            return true;
        }
        return false;
    }

    default String getFromBASE64(String s) {
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
