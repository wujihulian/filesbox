package com.svnlan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:
 * @Description: 日志工具类
 */
public class LogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    /**
       * @Description: 记录debug日志
       * @params:  [messages]
       * @Return:  void
       * @Modified:
       */
    public static void debug(String... messages) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(mergeStrings(messages));
        }
    }



    /**
       * @Description: 记录Info日志
       * @params:  [messages]
       * @Return:  void
       * @Modified:
       */
    public static void info(String... messages) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(mergeObjects(messages));
        }
    }

    /**
       * @Description: 记录warn日志
       * @params:  [messages]
       * @Return:  void
       * @Modified:
       */
    public static void warn(String... messages) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(mergeObjects(messages));
        }
    }

    /**
       * @Description: 记录错误日志
       * @params:  [messages]
       * @Return:  void
       * @Modified:
       */
    public static void error(String... messages) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(mergeStrings(messages));
        }

    }

    /**
       * @Description: 记录错误日志
       * @params:  [e, messages]
       * @Return:  void
       * @Modified:
       */
    public static void error(Throwable e, String... messages) {
        if (LOGGER.isErrorEnabled()) {
            if (e == null) {
                LOGGER.error(mergeStrings(messages));
            } else {
                LOGGER.error(mergeStrings(messages), e);
            }
        }
    }

    /**
       * @Description: 将string数组合并为一个字符串返回
       * @params:  [strings]
       * @Return:  java.lang.String
       * @Modified:
       */
    private static String mergeStrings(String[] strings) {
        if (strings == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
       * @Description: 将string数组合并为一个字符串返回
       * @params:  [objects]
       * @Return:  java.lang.String
       * @Modified:
       */
    private static String mergeObjects(Object[] objects) {
        if (objects == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                continue;
            }
            if (obj instanceof String) {
                stringBuilder.append(obj);
            } else {
                stringBuilder.append(obj.toString());
            }
        }
        return stringBuilder.toString();
    }



}
