package com.svnlan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date
 * 请求参数映射
 */
public class HMapper extends JSONObject {
    public <T> List<T> getList(String name, Class<T> clazz) {
        return parseArray(toJSONString(get(name)), clazz);
    }

    /**
     * 普通对象转json对象
     *
     * @param object
     * @return
     */
    public static JSONObject obj2JsonObj(Object object) {
        return JSON.parseObject(JSON.toJSONString(object));
    }

    /**
     * 从当前对象获取指定数组并转化成集合返回
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return 非空集合
     */
    public  <T> List<T> arr2List(String name, Class<T[]> clazz) {
        return toList(getObject(name, clazz));
    }

    /**
     * 从当前对象解析指定类型的对象
     *
     * @param data
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseData(String data, String name, Class<T> clazz) {
        try {
            return JSONObject.parseObject(data).getObject(name, clazz);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T parseData(byte[] data, String name, Class<T> clazz) {
        return parseData(new String(data), name, clazz);
    }

    /**
     * 解析静态json数据中的指定名字的对象到集合
     *
     * @param data
     * @param name
     * @param clazz
     * @param <T>
     * @return 非空集合
     */
    public static <T> List<T> parseData2List(String data, String name, Class<T[]> clazz) {
        try {
            return toList(JSONObject.parseObject(data).getObject(name, clazz));
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static <T> List<T> parseData2List(byte[] data, String name, Class<T[]> clazz) {
        try {
            return parseData2List(new String(data), name, clazz);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    /**
     * 解析静态json数据中的指定名字的对象到数组
     *
     * @param data
     * @param name
     * @param clazz
     * @param <T>
     * @return 数组
     */
    public static <T> T[] parseData2Array(String data, String name, Class<T[]> clazz) {
        try {
            return JSONObject.parseObject(data).getObject(name, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T[] parseData2Array(byte[] data, String name, Class<T[]> clazz) {
        try {
            return parseData2Array(new String(data), name, clazz);
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 私有方法，数组转变长集合
     *
     * @param arr
     * @param <T>
     * @return
     */
    private static <T> List<T> toList(T[] arr) {
        if (arr == null) {
            return new ArrayList<>();
        } else {
            List<T> list = new ArrayList<>();
            list.addAll(Arrays.asList(arr));
            return list;
        }
    }

    /**
     * Map->Hmapper
     *
     * @param map
     * @return
     */

    public static HMapper map2HMapper(Map<String, Object> map) {
        HMapper hMapper = new HMapper();
        map.forEach((k, v) -> hMapper.put(k, v));
        return hMapper;
    }

    /**
     * 获取字符串
     *
     * @param name
     * @param iftrim
     * @return
     */

    public String getString(String name, Boolean iftrim) {
        if (iftrim) {
            return StringUtils.trimWhitespace(getString(name));
        } else {
            return getString(name);
        }
    }


}
