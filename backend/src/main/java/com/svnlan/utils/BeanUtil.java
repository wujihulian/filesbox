package com.svnlan.utils;

import com.svnlan.NettyWebchat.service.NettyLoginService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * @Author:
 * @Description:
 */
@Component
public class BeanUtil {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RestTemplate addressedRestTemplate;
    @Resource
    private NettyLoginService nettyLoginService;

    @Resource
    private ServletContext servletContext;

    private WebApplicationContext webApplicationContext;


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public RestTemplate getAddressedRestTemplate() {
        return addressedRestTemplate;
    }


    public NettyLoginService getNettyLoginService() {
        return nettyLoginService;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public WebApplicationContext getWebApplicationContext() {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return webApplicationContext;
    }

    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
}
