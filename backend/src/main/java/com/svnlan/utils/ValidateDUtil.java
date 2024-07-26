package com.svnlan.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import org.springframework.util.ObjectUtils;

/**
 * @description: 校验工具类
 */
public class ValidateDUtil {

    /**
     * @description: 校验对象不能为空
     * @param obj
     * @return void
     */
    public static void validateObjectNotEmpty(Object obj) {
        if(ObjectUtils.isEmpty(obj)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
    }

    /**
     * @description: 校验字符串的字符个数下限(最少)
     * @param str
     * @param tipName
     * @param minLen
     * @return void
     */
    public static void validateTextMinLen(String str, String tipName, Integer minLen) {
        //
        validateText(str, Boolean.FALSE, tipName, minLen, null);
    }

    /**
     * @description: 校验字符串的字符个数上限（最多）
     * @param str
     * @param tipName
     * @param maxLen
     * @return void
     */
    public static void validateTextMaxLen(String str, String tipName, Integer maxLen) {
        //
        validateText(str, Boolean.FALSE, tipName, null, maxLen);
    }

    /**
     * @description: 校验字符串的字符个数上下限
     * @param str
     * @param tipName
     * @param minLen
     * @param maxLen
     * @return void
     */
    public static void validateTextLen(String str, String tipName, Integer minLen, Integer maxLen) {
        //
        validateText(str, Boolean.FALSE, tipName, minLen, maxLen);
    }

    /**
     * @description: 校验字符串个数上下限（允许为空，不为空时校验）
     * @param str
     * @param tipName
     * @param minLen
     * @param maxLen
     * @return void
     */
    public static void validateTextLenAllowEmpty(String str, String tipName, Integer minLen, Integer maxLen) {
        //
        validateText(str, Boolean.TRUE, tipName, minLen, maxLen);
    }

    /**
     * @description: 校验字符串
     * @param str
     * @param allowEmpty
     * @param tipName
     * @param minLen
     * @param maxLen
     * @return void
     */
    public static void validateText(String str, Boolean allowEmpty, String tipName, Integer minLen, Integer maxLen) {
        if(!allowEmpty) {
            //校验对象不能为空
            validateObjectNotEmpty(str);
        }
        if(null == str) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if(!StringUtil.isEmpty(str)) {
            if(null != minLen) {
                //
                if (str.length() < minLen.intValue()) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
            }
            if(null != maxLen) {
                if (str.length() > maxLen.intValue()) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
            }
        }
    }



    public static void main(String args[]) {
//        //
//        validateObjectNotEmpty("", "id");
        //
        validateTextLen("12345678", "textTD", 5, 6);
    }

}
