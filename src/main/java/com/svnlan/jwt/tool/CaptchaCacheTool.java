package com.svnlan.jwt.tool;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @description
 */
@Component
public class CaptchaCacheTool {

    private static final Logger logger = LoggerFactory.getLogger("error");

    @Resource
    RedisTemplate redisTemplate;


    /**
     * @Description:设置验证码图片的缓存,根据http-response中设置的cookie.captcha的值来组成Redis的key.
     * @param map : 包含http相应中的cookie值和验证码的文本内容.
     * @Return:  void
     * @Modified:
     */
    public void setCaptchaAlphabetsCache(Map<String, Object> map){
        if (CollectionUtils.isEmpty(map)){
            logger.error("setCaptchaAlphabetsCache mapEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_CODE_VALUE_RETURNKEY)){
            logger.error("setCaptchaAlphabetsCache CodeValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_COOKIE_VALUE_RETURNKEY)){
            logger.error("setCaptchaAlphabetsCache CookieValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
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


            String s = valueOperations.get(key);
            logger.info("captcha cache get : {}", s);
        }catch (Exception  e){
            logger.error("outCaptcha setCaptchaRedisValueCache failed. key : {}, value : {}", cookieKey, alphabets);
            throw new SvnlanRuntimeException(CodeMessageEnum.codeExpired.getCode());
        }
    }


    /**
     * @Description:获取Redis中的验证码文本内容.
     * @param request : http请求
     * @Return:  java.lang.String
     * @Modified:
     */
    public String getRedisCaptchaAlphabets(HttpServletRequest request, String key){
        if (ObjectUtils.isEmpty(request)){
            logger.error("getRedisCaptchaAlphabet requestEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        Cookie cookie = this.getRequestCookieByKey(request, key);
        if (null == cookie){
            logger.error("getRedisCaptchaAlphabet CookieKeyEmpty.");
            // 没有对应key的cookie
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        String captchaKey = cookie.getValue();
        if (StringUtil.isEmpty(captchaKey)){
            logger.error("getRedisCaptchaAlphabet cookieValueEmpty.");
            // cookie对应的value为空
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        String redisKey = key + "_" + captchaKey;
        if (!redisTemplate.hasKey(redisKey)){
            logger.error("getRedisCaptchaAlphabet redisNoKey  key: {}", redisKey);
            throw new SvnlanRuntimeException(CodeMessageEnum.codeExpired.getCode());
        }

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        return  operations.get(redisKey);
    }

    /*
     * @Description:从请求的cookie中的captcha的值组成验证码图片Redis的key
     * @param request : 请求
     * @Return:  java.lang.String
     * @Modified:
     */
    private String getCaptchaRedisKeyFromRequest(HttpServletRequest request, String key){
        if (ObjectUtils.isEmpty(request)){
            logger.error("getRedisCaptchaAlphabet requestEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        Cookie cookie = this.getRequestCookieByKey(request, key);
        if (null == cookie){
            logger.error("getRedisCaptchaAlphabet CookieKeyEmpty.");
            // 没有对应key的cookie
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        String captchaKey = cookie.getValue();
        if (StringUtil.isEmpty(captchaKey)){
            logger.error("getRedisCaptchaAlphabet cookieValueEmpty.");
            // cookie对应的value为空
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }

        return key + "_" + captchaKey;
    }


    /**
     * @Description:删除某个验证码的Redis键值对.(在验证码检查正确之后)
     * @param request : http请求
     * @Return:  boolean
     * @Modified:
     */
    public boolean deleteRedisCaptchaKey(HttpServletRequest request, String k){
        boolean s = false;
        String key = this.getCaptchaRedisKeyFromRequest(request, k);
        if (redisTemplate.hasKey(key)){

            s = redisTemplate.delete(key);
        }
        logger.info("deleteRedisCaptchaKey key : {}, status : {}" , key, s);
        return s;
    }


    /*
     * @Description:获取http请求中的cookie指定的键值
     * @param request : http请求
     * @param key :  指定的键值
     * @Return:  javax.servlet.http.Cookie
     * @Modified:
     */
    public Cookie getRequestCookieByKey(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if (null != cookies){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)){
                    return cookie;
                }
            }
        }
        return null;
    }


    /**
     * @Description:验证用户输入的验证码和服务端缓存的验证码的内容是否一致.
     * @param request : http请求
     * @param inputValue : 用户输入的验证码内容
     * @Return:  boolean
     * @Modified:
     */
    public boolean checkCaptchaAlphabets(HttpServletRequest request, String inputValue){
        // 可以直接从request获取用户输入的验证码内容,但是考虑到各种请求的method以及参数的接收和编码问题,这里直接接收参数inputValue,直接用框架接收的值吧.
        if (null == inputValue){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (null == request){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        String redisValue = this.getRedisCaptchaAlphabets(request, GlobalConfig.CAPTCHA_COOKIE_KEY);
        if (null == redisValue){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        logger.info("checkCaptchaAlphabets correctCaptcha : {}", redisValue);
        // 比较验证码的时候都转为小写,不区分大小写.
        if (redisValue.toLowerCase().equals(inputValue.trim().toLowerCase())){
            // 如果当前验证码被验证正确了,那么删除缓存,不能重复使用.
            this.deleteRedisCaptchaKey(request, GlobalConfig.CAPTCHA_COOKIE_KEY);
            return true;
        }
        return false;
    }

    /**
     * @Description:设置验证码图片的缓存,根据http-response中设置的cookie.captcha的值来组成Redis的key.
     * @param map : 包含http相应中的cookie值和验证码的文本内容.
     * @Return:  void
     * @Modified:
     */
    public void setCaptchaCodeAlphabetsCache(Map<String, Object> map){
        if (CollectionUtils.isEmpty(map)){
            logger.error("setCaptchaCodeAlphabetsCache mapEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_CODE_CODE_VALUE_RETURNKEY)){
            logger.error("setCaptchaCodeAlphabetsCache CodeValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (!map.containsKey(GlobalConfig.CAPTCHA_CODE_COOKIE_VALUE_RETURNKEY)){
            logger.error("setCaptchaCodeAlphabetsCache CookieValueEmpty.");
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        String cookieKey = (String)map.get(GlobalConfig.CAPTCHA_CODE_COOKIE_VALUE_RETURNKEY); // cookie value
        String alphabets = (String)map.get(GlobalConfig.CAPTCHA_CODE_CODE_VALUE_RETURNKEY); // code
        // 设置Redis缓存,输入的时候校检验证码
        try{
            String key =  GlobalConfig.CAPTCHA_CODE_COOKIE_KEY + "_" + cookieKey;
            logger.info("captchaCode redis key : {}, value : {}", key, alphabets);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            if (redisTemplate.hasKey(key)){
                logger.error("setCaptchaCodeAlphabetsCache keyExists : {}, {}", key, alphabets);
            }
            valueOperations.set(key, alphabets, GlobalConfig.CAPTCHA_CODE_TTL, TimeUnit.SECONDS);


            String s = valueOperations.get(key);
            logger.info("captchaCode cache get : {}", s);
        }catch (Exception  e){
            logger.error("captchaCode setCaptchaCodeAlphabetsCache failed. key : {}, value : {}", cookieKey, alphabets);
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
    }
    /**
     * @Description:验证用户输入的验证码和服务端缓存的验证码的内容是否一致.
     * @param request : http请求
     * @param inputValue : 用户输入的验证码内容
     * @Return:  boolean
     * @Modified:
     */
    public boolean checkCaptchaCodeAlphabets(HttpServletRequest request, String inputValue){
        // 可以直接从request获取用户输入的验证码内容,但是考虑到各种请求的method以及参数的接收和编码问题,这里直接接收参数inputValue,直接用框架接收的值吧.
        if (null == inputValue){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        if (null == request){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        String redisValue = this.getRedisCaptchaAlphabets(request, GlobalConfig.CAPTCHA_CODE_COOKIE_KEY );
        if (null == redisValue){
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        logger.info("checkCaptchaCodeAlphabets correctCaptcha : {}", redisValue);
        // 比较验证码的时候都转为小写,不区分大小写.
        if (redisValue.toLowerCase().equals(inputValue.trim().toLowerCase())){
            // 如果当前验证码被验证正确了,那么删除缓存,不能重复使用.
            this.deleteRedisCaptchaKey(request, GlobalConfig.CAPTCHA_CODE_COOKIE_KEY );
            return true;
        }
        return false;
    }
}


