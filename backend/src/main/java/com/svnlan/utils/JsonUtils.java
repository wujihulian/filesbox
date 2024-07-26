package com.svnlan.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:  @Description:
 */
public class JsonUtils {
    /**
     * Bean对象转JSON
     *
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            if (StringUtils.isEmpty(dataFormatString)) {
                return JSONObject.toJSONString(object);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString);
        } else {
            return null;
        }
    }


    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        } else {
            return null;
        }
    }

    /**
     * String转JSON字符串
     *
     * @param key
     * @param value
     * @return
     */
    public static String stringToJsonByFastjson(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        return beanToJson(map, null);
    }


    /**
     * json字符串转map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, Map.class);
    }

    /**
     * json字符串转list
     *
     * @param jsonStr
     * @return
     */
    public static <T> List<T> stringToList(String jsonStr, Class<T> model) {
        List<T> object = (List<T>) JSONArray.parseArray(jsonStr, model);
        return object;
    }
    /**
     * @description 校验字符串是否是 json 格式
     */
    public static boolean isJson(String json) {
        if(ObjectUtils.isEmpty(json)){
            return false;
        }
        boolean isJsonObject = true;
        try {
            JSONObject.parseObject(json);
        } catch (Exception e) {
            isJsonObject = false;
        }
        if(!isJsonObject){ //不是json格式
            return false;
        }
        return true;
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSONArray.parseArray(json, clazz);
    }
}
