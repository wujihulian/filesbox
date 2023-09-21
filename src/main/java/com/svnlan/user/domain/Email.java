package com.svnlan.user.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: sulijuan
 * @Description:
 */
@Data
public class Email {
    //必填参数
    private String email;//接收方邮件
    private String subject;//主题
    private String content;//邮件内容
    //选填
    private String template;//模板
    private HashMap<String, String> kvMap;// 自定义参数
}
