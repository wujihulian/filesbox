package com.svnlan.jwt.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:创建验证码的参数类
 */
@Data
public class CaptchaCreateDTO {
    @NotNull
    @Min(value = 1, message = "图片的宽度不得小于1")
    private int width; // 图片的宽度

    @NotNull
    @Min(value = 1, message = "图片的高度不得小于1")
    private int height; //  图片的高度

    @Min(value = 1, message = "验证码的字符个数不能少于1")
    @Max(value = 8, message = "验证码的字符个数不得大于8")
    private int len = 4; // 返回的验证码图片中字符的个数 默认4

    @Min(value = 0, message = "验证码类型是非负整数")
    private int cType = 0; // 验证码类型,默认0 png 现在还只支持png
}

