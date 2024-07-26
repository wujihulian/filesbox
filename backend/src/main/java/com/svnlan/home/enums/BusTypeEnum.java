package com.svnlan.home.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 9:21
 */
public enum  BusTypeEnum {

    //图片
    IMAGE(1, "image"),

    CLOUD(2, "cloud"),
    //头像
    AVATAR(3, "avatar"),

    ATTACHMENT(4, "attachment"),

    // 资讯图片
    HOMEPAGE_IMAGE(5 ,"homepage_image"),
    //资讯附件
    HOMEPAGE_ATTACHMENT(6, "homepage_attachment"),
    //资讯视频
    HOMEPAGE_VIDEO(7, "homepage_video"),
    // 资讯导入
    BROCHURE_ATTACHMENT(8, "brochure_attachment"),
    //主页装扮缩略图
    DESIGN_THUMB(9, "design_thumb"),
    //主页装扮logo
    DESIGN_LOGO(10, "design_logo"),
    ;

    private static Map<String, BusTypeEnum> enumTypeMap = new HashMap<>();
    private static Map<Integer, BusTypeEnum> enumCodeMap = new HashMap<>();

    private String busType;
    private Integer typeCode;

    BusTypeEnum(Integer typeCode, String busType){
        this.typeCode = typeCode;
        this.busType = busType;
    }


    public static BusTypeEnum getFromTypeName(String typeName){
        if (enumTypeMap.containsKey(typeName)){
            return enumTypeMap.get(typeName);
        }
        for (BusTypeEnum r : BusTypeEnum.values()){
            if (r.getBusType().equals(typeName)){
                enumTypeMap.put(typeName, r);
                return r;
            }
        }
        return null;
    }
    public static BusTypeEnum getFromTypeCode(Integer typeCode){
        if (enumCodeMap.containsKey(typeCode)){
            return enumCodeMap.get(typeCode);
        }
        for (BusTypeEnum r : BusTypeEnum.values()){
            if (r.getTypeCode().equals(typeCode)){
                enumCodeMap.put(typeCode, r);
                return r;
            }
        }
        return null;
    }

    // 校验是否是资讯
    public static Boolean checkIsInfoType(String id){
        List<String> typeList = Arrays.asList(HOMEPAGE_IMAGE.getBusType(), HOMEPAGE_ATTACHMENT.getBusType(), HOMEPAGE_VIDEO.getBusType());
        if(typeList.contains(id)) {
            return true;
        }
        return false;
    }

    public String getBusType() {
        return busType;
    }

    public Integer getTypeCode() {
        return typeCode;
    }
    public String getTypeCodeString() {
        return typeCode.toString();
    }
}
