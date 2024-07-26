package com.svnlan.NettyWebchat.Common;

import com.svnlan.utils.BeanUtil;
import com.svnlan.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

/**
 * @Author:
 * @Description:
 */

public class SpringManager {
    //单例
    private static SpringManager instance = new SpringManager();

    private ApplicationContext ctx;
    private WebApplicationContext mvcContext;
    private DispatcherServlet dispatcherServlet;

    private SpringManager() {
//        ctx = new ClassPathXmlApplicationContext("spring.xml");
        ctx = SpringUtil.getApplicationContext();
//        mvcContext = new XmlWebApplicationContext();
//        mvcContext.setConfigLocation("classpath:spring-mvc.xml");
//        mvcContext.setParent(ctx);
        BeanUtil beanUtil = SpringUtil.getBean(BeanUtil.class);
        ServletContext servletContext = beanUtil.getServletContext();
        mvcContext = beanUtil.getWebApplicationContext();
        MockServletConfig servletConfig = new MockServletConfig(servletContext, "dispatcherServlet");
        dispatcherServlet = new DispatcherServlet(mvcContext);
        try {
            dispatcherServlet.init(servletConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SpringManager getInstance(){
        return instance;
    }

    public ApplicationContext getSpringContext(){
        return ctx;
    }

    public WebApplicationContext getMvcContext(){
        return mvcContext;
    }

    public DispatcherServlet getDispatcherServlet(){
        return dispatcherServlet;
    }
}