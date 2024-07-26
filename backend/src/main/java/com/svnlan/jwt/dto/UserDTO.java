package com.svnlan.jwt.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 */
@Data
public class UserDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String password;

    private String passwordTwo;
    /**
     保持登录状态
     */
    private Boolean saveCookie;
    /**
     密码是否加密
     */
    private Boolean encrypted = true;
    /**

     */
    private Integer redirectType;
    /**
    是否校验图形验证码
     */
    private Short isGraphicCode = 1;
    /**
     图形验证码
     */
    private String captchaInput;
    /**
     前一次触发4011时,需用户填写的验证码
     */
    private String verifyCode;
    /**
     前一次触发4011时给出的key
     */
    private String verifyKey;

    private Long userID;

    private String type;
    private String x;
    private String y;
}
