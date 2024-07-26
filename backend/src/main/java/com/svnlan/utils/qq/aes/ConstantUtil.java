package com.svnlan.utils.qq.aes;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;


/**
 * @Description: XML转换Map
 */
public class ConstantUtil {
    /**
     * 将xml转换为Map。 支持xml标签多层嵌套，并以"."分隔多级标签（不包括根节点）。 不支持XML标签重复时的情况
     *
     * @param xml
     * @param map
     * @return
     */
    public static Map<String, String> parseXmlToMap(String xml, Map<String, String> map) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new StringReader(xml));
            Element root = doc.getRootElement();
            String path = "";
            if (map.containsKey(root.getName().trim())) {
                path = map.get(root.getName().trim());
                map.remove(root.getName().trim());
            }
            for (Iterator i = root.elementIterator(); i.hasNext();) {
                Element element = (Element) i.next();
                if (element.isTextOnly()) {
                    if (path.length() > 0) {
                        map.put(path + element.getName().trim(), element.getTextTrim());
                    } else {
                        map.put(element.getName().trim(), element.getTextTrim());
                    }
                } else {
                    map.put(element.getName().trim(), path+ element.getName().trim() + ".");
                    parseXmlToMap(element.asXML(), map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

