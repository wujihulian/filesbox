package com.svnlan.jwt.service;


import com.svnlan.jwt.dto.ImageVerificationDto;
import com.svnlan.jwt.vo.ImageVerificationVo;

import javax.servlet.http.HttpServletResponse;

/**
 * description:  验证码业务处理类
 * @author:
 */
public interface CaptchaService {

    /**
     * 根据类型获取验证码
     * @param imageVerificationDto  图片类型dto
     * @return  图片验证码
     */
    ImageVerificationVo selectImageVerificationCode(ImageVerificationDto imageVerificationDto, HttpServletResponse response);

    /**
     * 校验图片验证码
     * @param x x轴坐标
     * @param y y轴坐标
     * @return 校验结果
     */
    boolean checkVerificationResult(String x, String y);
}
