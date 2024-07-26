package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.ByteArrayDataSource;
import com.sun.mail.smtp.SMTPAddressFailedException;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.Email;
import com.svnlan.user.service.MailService;
import com.svnlan.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Author: sulijuan
 * @Description:
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    @Resource
    private JavaMailSender javaMailSender;
    /*@Value("${spring.mail.host}")
    public String HOST;
    @Value("${spring.mail.username}")
    public String USER_NAME;
    @Value("${spring.mail.password}")
    public String PASSWORD;*/
    @Value("${mail.encode.fail}")
    private String[] encodeFailMail;
    @Resource
    SystemOptionDao systemOptionDao;


    public void sendMail(String to, String message, String title) {
        sendMail(to, message, title,false, null);
    }

    public JSONObject sendMail(String to, String message, String title, boolean isAtt, List<IOSourceVo> list) {

        String emailConfigStr = systemOptionDao.getSystemConfigByKey("emailConfig");
        if (!StringUtils.hasText(emailConfigStr)) {
            LOGGER.error("发送邮件失败，请先添加邮箱设置");
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        JSONObject mailObj = null;
        try {
            mailObj = JSONObject.parseObject(emailConfigStr);
        }catch (Exception e){
            LOGGER.error("发送邮件失败，请先添加邮箱设置 error ", e.getMessage());
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        if (ObjectUtils.isEmpty(mailObj)
                || ObjectUtils.isEmpty(mailObj.getString("server"))
                || ObjectUtils.isEmpty(mailObj.getString("email"))
                || ObjectUtils.isEmpty(mailObj.getString("password"))
                ){
            LOGGER.error("发送邮件失败，请先添加邮箱设置 e");
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        return sendMailSSL(to, message, title, isAtt, list, mailObj);
    }

    public JSONObject sendMailSSL(String to, String message, String title, boolean isAtt, List<IOSourceVo> list, JSONObject mailObj) {
        String host = mailObj.getString("server");
        String user_name = mailObj.getString("email");
        String password = mailObj.getString("password");
        String port = !ObjectUtils.isEmpty(mailObj.getString("port")) ? mailObj.getString("port") : "465";

        JSONObject re = new JSONObject();
        re.put("code","0");
        re.put("msg","发送成功");
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            //设置邮件会话参数
            Properties props = new Properties();
            //邮箱的发送服务器地址
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            String encrypt = mailObj.getString("encrypt");
            if (!ObjectUtils.isEmpty(encrypt) && "3".equals(encrypt)){
                props.setProperty("mail.smtp.starttls.enable","true");
            }
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            //邮箱发送服务器端口,这里设置为 465 端口
            props.setProperty("mail.smtp.port", port);
            props.setProperty("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.auth", "true");
            //获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user_name, password);
                }
            });
            //通过会话,得到一个邮件,用于发送
            Message msg = new MimeMessage(session);
            //设置发件人
            msg.setFrom(new InternetAddress(user_name));
            //设置收件人,to为收件人,cc为抄送,bcc为密送
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));

            // 设置邮件标题
            msg.setSubject(title);
            //设置发送的日期
            msg.setSentDate(new Date());
            /*//设置邮件消息
            msg.setText(message);*/
            // 发送附件
            if (isAtt){
                //邮件正文部分
                BodyPart textBodyPart = new MimeBodyPart();
                StringBuilder sb = new StringBuilder();
                sb.append("<h1>请查收附件</h1>");
                for (IOSourceVo source : list) {
                    sb.append(source.getName() );
                    sb.append("<br />");
                }
                textBodyPart.setContent(sb.toString(), "text/html;charset=utf-8");

                //组合正文+附件
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textBodyPart);
                BodyPart imageBodyPart = null;
                for (IOSourceVo source : list) {
                    //邮件附件部分
                    imageBodyPart = new MimeBodyPart();
                    imageBodyPart.setFileName(source.getName());
                    imageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(Files.readAllBytes(Paths.get(source.getPath())), "application/octet-stream")));
                    imageBodyPart.setHeader("Content-ID", "<" + source.getId() + ">");

                    multipart.addBodyPart(imageBodyPart);
                }
                //设置邮件内容
                msg.setContent(multipart);
            }else {
                // 设置邮件的内容体
                msg.setContent(message, "text/html;charset=UTF-8");
            }
            //调用Transport的send方法去发送邮件
            Transport.send(msg);


        } catch (SMTPAddressFailedException e) {
            re.put("code",e.getReturnCode());
            re.put("msg", e.getMessage());
            LOGGER.error("SMTPAddressFailedException isAtt" + isAtt + " sendEmail2 发送邮件：error{}",e.getMessage());
        } catch (MessagingException e) {
            re.put("code","400");
            re.put("msg", e.getMessage());
            LOGGER.error("MessagingException isAtt" + isAtt + " sendEmail2 发送邮件：error{}",e.getMessage());
        } catch (Exception e) {
            re.put("code","400");
            re.put("msg", e.getMessage());
            LOGGER.error("Exception isAtt" + isAtt + " sendEmail2 发送邮件：error{}",e.getMessage());
        }
        return re;
    }

    @Override
    public void send(Email mail) {
        //sendOld(mail);
        sendMail(mail.getEmail(), mail.getContent(), mail.getSubject());
    }

    public void sendOld(Email mail) {
        JSONObject mailObj = this.getMailConfig();
        if (ObjectUtils.isEmpty(mailObj)
                || ObjectUtils.isEmpty(mailObj.getString("server"))
                || ObjectUtils.isEmpty(mailObj.getString("email"))
                || ObjectUtils.isEmpty(mailObj.getString("password"))
                ){
            LOGGER.error("发送邮件失败，请先添加邮箱设置 send");
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        LOGGER.info("发送邮件：{}",mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailObj.getString("email"));
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
        JSONObject mailObj = this.getMailConfig();
        if (ObjectUtils.isEmpty(mailObj)
                || ObjectUtils.isEmpty(mailObj.getString("server"))
                || ObjectUtils.isEmpty(mailObj.getString("email"))
                || ObjectUtils.isEmpty(mailObj.getString("password"))
                ){
            LOGGER.error("发送邮件失败，请先添加邮箱设置 sendEncodeFailNotify");
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        LOGGER.info("发送邮件：{}",mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailObj.getString("email"));
        message.setTo(encodeFailMail);
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        try {
            javaMailSender.send(message);
        }catch (Exception e){
            LOGGER.error("sendEmail 发送邮件：error{}",e.getMessage());
        }
    }
    @Override
    public JSONObject sendAtt(Email mail, List<IOSourceVo> list){

        return sendMail(mail.getEmail(), mail.getContent(), mail.getSubject(),true, list);
    }
    @Override
    public JSONObject sendCheck(Email mail, JSONObject mailObj){
        return sendMailSSL(mail.getEmail(), mail.getContent(), mail.getSubject(), false, null, mailObj);
    }

    private JSONObject getMailConfig(){
        String emailConfigStr = systemOptionDao.getSystemConfigByKey("emailConfig");
        if (!StringUtils.hasText(emailConfigStr)) {
            LOGGER.error(" getMailConfig 发送邮件失败，请先添加邮箱设置");
            return null;
        }
        JSONObject mailObj = null;
        try {
            mailObj = JSONObject.parseObject(emailConfigStr);
        }catch (Exception e){
            LOGGER.error("发送邮件失败，请先添加邮箱设置 error ", e.getMessage());

        }
        return mailObj;
    }
}
