package com.svnlan.common;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/** @Author: @Description: */
public class Result implements Serializable {

    private static final long serialVersionUID = -1L;

    /** 状态码 */
    private Boolean success;

    /** 结果消息 */
    private String message;

    /** 错误码 */
    private String code;

    /** 返回的数据 */
    private Object data;

    private Long timeStamp;

    /**
     * 构造函数
     * @param code
     * @param data
     */
    public Result(String code, Object data) {
        this(true, code, data);
    }
    public Result(boolean success, String code, Object data) {
        this.success = success;
        this.message = I18nUtils.i18n(code);
        this.code = code;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
    }

    public Result(boolean success, String code,String message) {
        this.success = success;
        if (ObjectUtils.isEmpty(message)) {
            this.message = I18nUtils.i18n(code);
        }else {
            this.message = message;
        }
        this.code = code;
        this.data = message;
        this.timeStamp = System.currentTimeMillis();
    }

    public Result(boolean success, String code,String message, boolean isFmt) {
        this.success = success;
        if (isFmt){
            String m = I18nUtils.i18n(code);
            this.message = String.format(m,message);
        }else {
            this.message = I18nUtils.i18n(code);
        }
        this.code = code;
        this.timeStamp = System.currentTimeMillis();
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.code = "-1";
        this.timeStamp = System.currentTimeMillis();
    }

    public Result(boolean success, String code, String message, Object data) {
        this.success = success;
        this.message = message;
        if (ObjectUtils.isEmpty(message)){
            this.message = I18nUtils.i18n(code);
        }
        this.code = code;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 返回自定义异常
     *
     * @param e
     * @return
     */
    public static Result returnSvnException(SvnlanRuntimeException e) {
        return new Result(Boolean.FALSE, e.getErrorCode(), null);
    }

    /**
     * 返回失败
     *
     * @param codeMessageEnum
     * @return
     */
    public static Result returnError(CodeMessageEnum codeMessageEnum) {
        return new Result(Boolean.FALSE, codeMessageEnum.getCode(), null);
    }

    public static Result returnError(String message) {
        return new Result(Boolean.FALSE, message);
    }
    /**
     * 返回成功
     *
     * @param data
     * @return
     */
    public static Result returnSuccess(Object data) {
        return new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), data);
    }

    public static Result returnSuccess() {
        return new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), null);
    }
}
