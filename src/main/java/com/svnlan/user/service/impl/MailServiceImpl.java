package com.svnlan.user.service.impl;

import com.svnlan.user.domain.Email;
import com.svnlan.user.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: sulijuan
 * @Description:
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    public String USER_NAME;
    @Value("${mail.encode.fail}")
    private String[] encodeFailMail;

    @Override
    public void send(Email mail) {
        LOGGER.info("发送邮件：{}",mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(USER_NAME);
        message.setTo(mail.getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        try {
            javaMailSender.send(message);
        }catch (Exception e){
            LOGGER.error("sendEmail 发送邮件：error{}",e.getMessage());
        }
    }

    @Override
    public void sendEncodeFailNotify(Email mail) {
        LOGGER.info("发送邮件：{}",mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(USER_NAME);
        message.setTo(encodeFailMail);
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        try {
            javaMailSender.send(message);
        }catch (Exception e){
            LOGGER.error("sendEmail 发送邮件：error{}",e.getMessage());
        }
    }
}
