package com.svnlan.webdav.auth;

import com.ithit.webdav.server.DavRequest;
import com.ithit.webdav.server.DavResponse;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import org.springframework.util.StringUtils;

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
            // 重新写入缓存，也就是过期时间重置
            CaffeineUtil.AUTHORIZATION_USERID.put(auth, userVo);
            userVoThreadLocal.set(userVo);
            return true;
        }
        return false;
    }
}
