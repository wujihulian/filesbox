package com.svnlan.jwt.constant;

/**
 * @Description:系统级静态变量
 */
public class SystemConstant {

    public static final int JWT_ERRCODE_EXPIRE = 4001; // Token过期
    public static final int JWT_ERRCODE_FAIL = 4002; // 验证不通过

    /**
     * JWT
     */
    public static final String JWT_SECRET = "8677df7fc3a34e26a61c034d5ec8245d"; // 密匙
    public static final String JWT_PARENT_SECRET = "y2490fjg8tja0wer25879dj30u1203aj";

    public static final Long JWT_TTL = 3600 * 24 * 365 * 2 * 1000L; // token有效时间，失效时间2年, 相当于永久有效

    public static final Long JWT_REDIS_TTL = 3600 * 24 * 30 * 1000L; //token实际有效时间，30天，redis tll

    public static final Long JWT_REDIS_LONG_TTL = 3600 * 24 * 365 * 1000L; //token实际有效时间，长期保存的，365天

    public static final Integer JWT_LONG_COOKIE_TIME = 3600 * 24 * 30; //保持登录状态时的，cookie时长

    public static final String JWT_TOKEN = "token";
    public static final String JWT_PARENT = "parent";
    public static final String LANG = "lang";
    public static final String DEFAULT_LANG = "zh_CN";

    public static final String JWT_REFRESH_TOKEN = "refresh_token";

    public static final String LOGIN_COOKIE_KEY = "hasLogin";

    public static final String JWT_PARENT_TOKEN = "ptoken";

    public static final String CHANGE_PWD_BY_THIRD = "changePwd:%s:%d";

    public static final String ACCESS_TOKEN_KEY = "accessToken:";
}
