package com.svnlan.user.service;


import com.svnlan.user.domain.Email;

/**
 * @Author: sulijuan
 * @Description:
 */
public interface MailService {

    /**
       * @Description: 纯文本
       * @params:  [mail]
       * @Return:  void
       * @Author:  sulijuan
       * @Modified:
       */
    void send(Email mail);

    void sendEncodeFailNotify(Email mail);
}
