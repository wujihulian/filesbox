package com.svnlan.user.service;


import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.domain.Email;

import java.util.List;

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

    JSONObject sendAtt(Email mail, List<IOSourceVo> list);
    JSONObject sendCheck(Email email, JSONObject mailObject);

}
