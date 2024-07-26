package com.svnlan.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author:
 * @Description: applicationContext工具
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext  = applicationContext;

        }
    }

    /**
       * @Description: 获取applicationContext
       * @params:  []
       * @Return:  org.springframework.context.ApplicationContext
       * @Modified:
       */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
       * @Description: 通过name获取 Bean.
       * @params:  [name]
       * @Return:  java.lang.Object
       * @Modified:
       */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
       * @Description:
       * @params:  [clazz]
       * @Return:  T
       * @Modified:
       */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);

    }

    /**
       * @Description: 通过class获取Bean.
       * @params:  [name, clazz]
       * @Return:  T
       * @Modified:
       */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
