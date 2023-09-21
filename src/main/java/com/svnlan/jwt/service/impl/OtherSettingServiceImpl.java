package com.svnlan.jwt.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.dao.UserJwtDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.OtherSettingService;
import com.svnlan.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 */
@Service
public class OtherSettingServiceImpl implements OtherSettingService {
    @Resource
    private UserJwtDao userJwtDao;
    @Resource
    private LoginUserUtil loginUserUtil;

    /**
       * @Description: 验证密码
       * @params:  [password]
       * @Return:  java.lang.Boolean
       * @Modified:
       */
    @Override
    public Boolean checkPassword(String password) {
        password = AESUtil.decrypt(password, GlobalConfig.LOGIN_PASSWORD_AES_KEY, true);
        if (password == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        } else {
            LogUtil.info("解密成功");
        }
        password = PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT);
        LoginUser loginUser = loginUserUtil.getLoginUser(HttpUtil.getRequest());
        String pwd = userJwtDao.checkPassword(loginUser.getUserID());
        return (pwd != null && password.equals(pwd));
    }
}
