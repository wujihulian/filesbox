package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.utils.RandomUtil;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @author:
 * @Date:
 */
public class ObjUtil {

    /**
     * 获取对象下所有不为空的属性和值
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> checkfield(Object obj) {
        Map<String, Object> map = null;
        try {
            Class<?> objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            map = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                if (val != null) {
                    map.put(field.getName(), val);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void initializefield(Object object) {
        try {
            Class<?> objectClass = object.getClass();
            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String type = field.getType().getSimpleName().toString();
                if (field.get(object) == null) {
                    if (type.equals("String")) {
                        field.set(object, "");
                    } else if (type.equals("Integer")) {
                        field.set(object, new Integer(0));
                    } else if (type.equals("Long")) {
                        field.set(object, new Long(0));
                    } else if (type.equals("Char")) {
                        field.set(object, "");
                    } else if (type.equals("Date")) {
                        field.set(object, new Date());
                    }else if (type.equals("LocalDateTime")) {
                        field.set(object, LocalDateTime.now());
                    }  else if (type.equals("BigDecimal")) {
                        field.set(object, new BigDecimal(0));
                    } else if (type.equals("Double")) {
                        field.set(object, new Double(0));
                    } else if (type.equals("Byte")) {
                        field.set(object, new Byte("0"));
                    } else if (type.equals("Float")) {
                        field.set(object, new Float(0));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @return
     * @description: 得API前缀
     */
    public static String findApiPrefix(HttpServletRequest request) {
        return findApiPrefix(request, RandomUtil.getuuid());
    }

    /**
     * @param request
     * @param taskId
     * @description: 得API前缀
     */
    public static String findApiPrefix(HttpServletRequest request, String taskId) {
        String prefix = "";
        String requestUri = request.getRequestURI();
        prefix = String.format("@日志@%s(%s) >>> ", requestUri, taskId);
        return prefix;
    }

    /**
     * @param obj
     * @description: object转List方法
     */
    public static <T> List<T> objectToList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        // 判断 obj 是否包含 List 类型
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                // 使用Class.cast做类型转换
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 将实体类转化为 jsonObject
     */
    @SneakyThrows
    public static JSONObject toJsonObject(JSONObject jsonObject, Class<?> clazz, Object instance, boolean ignoreNull) {

        Field[] fields = clazz.getDeclaredFields();
        if (Objects.isNull(jsonObject)) {
            jsonObject = new JSONObject();
        }
        if (ignoreNull) {
            for (Field field : fields) {
                field.setAccessible(true);
                Optional<Object> optional = Optional.ofNullable(field.get(instance));
                if (optional.isPresent()) {
                    jsonObject.fluentPut(field.getName(), optional.get());
                }
            }
        }else{
            for (Field field : fields) {
                field.setAccessible(true);
                jsonObject.fluentPut(field.getName(), field.get(instance));
            }
        }
        return jsonObject;
    }

}
