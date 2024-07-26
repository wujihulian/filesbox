package com.svnlan.enums;

/**
 * @Author:
 * @Description: 用户具体类型
 * @Date:
 */
public enum UserTypeExactEnum {
    SYSTEM_ADMIN("1", "系统管理员"),
    GENERAL_ADMIN("2", "普通管理员"),
    USER("3", "用户"),
    ;

    private String typeCode;
    private String typeName;

    UserTypeExactEnum(String typeCode, String typeName){
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * @description: 由code得对应的枚举
     * @param typeCode
     * @return com.svnlan.jwt.enums.UserTypeExactEnum
     * @author
     */
    public static UserTypeExactEnum getUserTypeExactEnumByCode(String typeCode) {
        UserTypeExactEnum resultEnum = null;
        for (UserTypeExactEnum tempEnum : UserTypeExactEnum.values()) {
            if(tempEnum.getTypeCode().equals(typeCode)) {
                resultEnum = tempEnum;
                break;
            }
        }
        return resultEnum;
    }

}
