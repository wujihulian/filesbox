package com.svnlan.utils.captcha.utils;

import com.alibaba.fastjson.JSON;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.utils.RandomUtil;
import com.svnlan.utils.captcha.Captcha;
import com.svnlan.utils.captcha.CaptchaPng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huanghao
 * @Description:
 * @Date: 2019/6/20 3:27 PM
 */
public class CaptchaUtil {
    private static final Logger logger = LoggerFactory.getLogger("error");
    @Resource
    RedisTemplate redisTemplate;



    /**
     * 输出验证码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> out(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = out(5, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> out(int len, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = out(130, 48, len, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param font     字体
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> out(int len, Font font, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = out(130, 48, len, font, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> out(int width, int height, int len, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = out(width, height, len, null, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param font     字体
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> out(int width, int height, int len, Font font, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // key : cookieValue, captchaValue
        Map<String, Object> resultMap = outCaptcha(width, height, len, font, 1, request, response);
        return resultMap;
    }


    /**
     * 输出验证码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> outPng(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object>  map = outPng(4, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> outPng(int len, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = outPng(130, 48, len, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param font     字体
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> outPng(int len, Font font, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object>  map = outPng(130, 48, len, font, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> outPng(int width, int height, int len, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = outPng(width, height, len, null, request, response);
        return map;
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param font     字体
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static Map<String, Object> outPng(int width, int height, int len, Font font, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> resultMap = outCaptcha(width, height, len, font, 0, request, response);
        return resultMap;
    }


    public static Map<String, Object> outPng(int width, int height,Font font, int cType, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> resultMap = outCaptcha(width, height, 4, null, cType, request, response);
        return resultMap;
    }


    /**
     * @Description:设置返回验证码的http-header头
     * @param response : http响应
     * @Return:  void
     * @Author:  huanghao
     * @Date:  2019/6/20 3:35 PM
     * @Modified:
     */
    public static void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /**
     * @Description:将服务端生成的图片流输出到客户端.返回客户端的cookie值和图片中的验证码字符串.
     * @param width : 图片的宽
     * @param height : 图片的高
     * @param len : 验证码的长度
     * @param font : 字体的类型
     * @param cType : 验证码的类型
     * @param request : 请求
     * @param response : 响应
     * @Return:  java.util.Map<java.lang.String,java.lang.Object>
     * @Author:  huanghao
     * @Date:  2019/6/20 4:52 PM
     * @Modified:
     */
    private static Map<String, Object> outCaptcha(int width, int height, int len, Font font, int cType, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setHeader(response);
        Captcha captcha = new CaptchaPng(width, height, len);
        captcha.setCharType(cType);

        logger.info("captchaInfo : {}, cType : {}", JSON.toJSONString(captcha), cType);

        // TODO: 2019/6/20 png, gif和不同的验证码内容切换
//        if (cType == 0) {
//            captcha = new CaptchaPng(width, height, len);
//        } else if (cType == 1) {
//            captcha = new CaptchaGif(width, height, len);
//        }
        if (font != null) {
            captcha.setFont(font);
        }
        String uuid = RandomUtil.getUUID();
        Cookie cookie = new Cookie(GlobalConfig.CAPTCHA_COOKIE_KEY, uuid);
        cookie.setPath("/");
        response.addCookie(cookie);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(GlobalConfig.CAPTCHA_COOKIE_VALUE_RETURNKEY, uuid);
        resultMap.put(GlobalConfig.CAPTCHA_CODE_VALUE_RETURNKEY, captcha.getAlphabets());
//        request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
        captcha.out(response.getOutputStream());
        return resultMap;
    }


    /*
     * @Description:服务端将验证码图片中的字符串缓存在Redis中.下次请求的时候验证.
     * @param map : 包含客户端的cookie值(captcha=value)和验证码图片中的字符串.
     * @Return:  void
     * @Author:  huanghao
     * @Date:  2019/6/20 4:49 PM
     * @Modified:
     */
    public void setCaptchaAlphabetsCache(Map<String, Object> map){
        if (CollectionUtils.isEmpty(map)){
            logger.error("setCaptchaAlphabetsCache mapEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_CODE_VALUE_RETURNKEY)){
            logger.error("setCaptchaAlphabetsCache CodeValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_COOKIE_VALUE_RETURNKEY)){
            logger.error("setCaptchaAlphabetsCache CookieValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        String cookieKey = (String)map.get(GlobalConfig.CAPTCHA_COOKIE_VALUE_RETURNKEY); // cookie value
        String alphabets = (String)map.get(GlobalConfig.CAPTCHA_CODE_VALUE_RETURNKEY); // code
        // 设置Redis缓存,输入的时候校检验证码
        try{
            String key =  GlobalConfig.CAPTCHA_COOKIE_KEY + "_" + cookieKey;
            logger.info("captcha redis key : {}, value : {}", GlobalConfig.CAPTCHA_COOKIE_KEY + "_" + cookieKey, alphabets);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            if (redisTemplate.hasKey(key)){
                logger.error("setCaptchaAlphabetsCache keyExists : {}, {}", key, alphabets);
            }
            valueOperations.set(key, alphabets, GlobalConfig.CAPTCHA_TTL, TimeUnit.SECONDS);
        }catch (Exception  e){
            logger.error("outCaptcha setCaptchaRedisValueCache failed. key : {}, value : {}", cookieKey, alphabets);
            throw new SvnlanRuntimeException(CodeMessageEnum.codeExpired.getCode());
        }
    }

}
