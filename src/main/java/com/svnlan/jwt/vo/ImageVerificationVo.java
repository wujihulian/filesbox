package com.svnlan.jwt.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/7 9:50
 */
@Data
public class ImageVerificationVo {

    /**
     * 验证码类型
     */
    private String type;

    /**
     * 字符验证码
     */
    private String charImage;

    /**
     * 加减乘除验证码
     */
    private String operationImage;

    /**
     * 滑动验证码，源图
     */
    private String originImage;

    /**
     * 滑动验证码，遮罩图
     */
    private String shadeImage;

    /**
     * 滑动验证码，裁剪图
     */
    private String cutoutImage;

    /**
     * 滑动验证码，X轴
     */
    private int x;

    /**
     * 滑动验证码，Y轴
     */
    private int y;
}
