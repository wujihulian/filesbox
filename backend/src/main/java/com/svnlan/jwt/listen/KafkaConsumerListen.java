package com.svnlan.jwt.listen;


import com.svnlan.jwt.dto.ClearTokenDTO;
import com.svnlan.jwt.service.LogLoginService;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author:
 * @Description: kafka消息队列的监听器
 * @Date:
 */
@Component
public class KafkaConsumerListen {

    @Resource
    private JWTService jwtService;
    @Resource
    private LogLoginService logLoginService;


}
