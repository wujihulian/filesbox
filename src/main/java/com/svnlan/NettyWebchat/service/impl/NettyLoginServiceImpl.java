package com.svnlan.NettyWebchat.service.impl;

import com.svnlan.NettyWebchat.Common.CommonHandler;
import com.svnlan.NettyWebchat.Domain.ChannelSupervise;
import com.svnlan.NettyWebchat.Domain.ClientInfo;
import com.svnlan.NettyWebchat.Domain.CommonReturnMessage;
import com.svnlan.NettyWebchat.Domain.RoomMsg;
import com.svnlan.NettyWebchat.dto.ScanLoginMessage;
import com.svnlan.NettyWebchat.dto.ScanLoginQRDTO;
import com.svnlan.NettyWebchat.dto.ScanLoginResult;
import com.svnlan.NettyWebchat.dto.TempAuthDTO;
import com.svnlan.NettyWebchat.service.NettyBroadcastService;
import com.svnlan.NettyWebchat.service.NettyLoginService;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.BusinessTypeEnum;
import com.svnlan.enums.ScanLoginActionEnum;
import com.svnlan.enums.ScanLoginMsgTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description: 扫码登录处理
 * @author
 */
@Service
public class NettyLoginServiceImpl implements NettyLoginService {

    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${scan.login.code.expire}")
    private Long scanLoginCodeExpire;
    @Resource
    private NettyBroadcastService newBroadcastService;

    @Override
    public void connect(ChannelHandlerContext ctx, String channelName) {
        BeanUtil beanUtil = SpringUtil.getBean(BeanUtil.class);
        StringRedisTemplate stringRedisTemplate = beanUtil.getStringRedisTemplate();
        //查询key是否存在
        String val = stringRedisTemplate.opsForValue().get(GlobalConfig.SCAN_LOGIN_CODE_REDIS_KEY + channelName);
        //二维码已经失效，请重新扫描（或不存在）
        if (StringUtil.isEmpty(val)){
            LogUtil.error("[扫码登录]建立连接时检测二维码已经失效>>>channelName:" + channelName);
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx) {
        ClientInfo clientInfo = CommonHandler.getClientInfo(ctx);
        String roomName = clientInfo.getRoomName();
        //
        HashOperations hashOperations = this.stringRedisTemplate.opsForHash();
        String channelId = ctx.channel().id().asLongText();

        //删除内存值
        if (ChannelSupervise.scanLoginRooms.get(roomName) != null) {
            //
            ChannelSupervise.scanLoginRooms.get(roomName).remove(ctx.channel());
        }

        //删除REDIS缓存
        //扫码登录的 channelId与标识ID(loginId)的关联关系
        String lcRelationRedisKey = String.format(GlobalConfig.ScanLoginIdChannelRelation, roomName);

        //删除扫码登录的 APP token与 生成的临时授权码的关联关系的REDIS缓存
        String tempAuthRelationRedisKey = String.format(GlobalConfig.ScanLoginTempAuthRelation, roomName);
        Map<String, String> lcMap = hashOperations.entries(lcRelationRedisKey);
        Iterator iterator = lcMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            String tempChannelId = entry.getKey();
            String loginId = entry.getValue();
            if(tempChannelId.equals(channelId) && hashOperations.hasKey(tempAuthRelationRedisKey, loginId)) {
                hashOperations.delete(tempAuthRelationRedisKey, loginId);
            }
        }

        //删除channelId与标识ID(loginId)的关联关系的REDIS缓存
        hashOperations.delete(lcRelationRedisKey, channelId);

        ScanLoginResult scanLoginResult = new ScanLoginResult();
        scanLoginResult.setAction("disconnect");
        scanLoginResult.setCode("200");
        scanLoginResult.setMessage("disconnect success");
        RoomMsg roomMsg = new RoomMsg();
        roomMsg.setRoomName(roomName);
        roomMsg.setMessage(JsonUtils.beanToJson(scanLoginResult));
        roomMsg.setChannelId(channelId);
        newBroadcastService.sendScanLoginMessage(roomMsg);
    }

    @Override
    public void receiveMessage(ChannelHandlerContext ctx, String message) {
        String prefix = "[扫码登录]收到处理消息>>>";
        ClientInfo clientInfo = CommonHandler.getClientInfo(ctx);
        String roomName = clientInfo.getRoomName();
        //校验房间号不能为空
        if (StringUtil.isEmpty(roomName)){
            ChannelSupervise.sendCommonReturn(ctx, "400", "rcv");
            return;
        }

        String channelId = ctx.channel().id().asLongText();
        String paramTip = String.format("<<<房间号:%s，内容:%s，channelId：%s", roomName, message, channelId);
        LogUtil.info(prefix + " start" + paramTip);

        //校验二维码（房间号）是否已失效
        BeanUtil beanUtil = SpringUtil.getBean(BeanUtil.class);
        StringRedisTemplate stringRedisTemplate = beanUtil.getStringRedisTemplate();
        String redisKey = GlobalConfig.SCAN_LOGIN_CODE_REDIS_KEY + roomName;
        //查询key是否存在
        Object qrCodeObj = stringRedisTemplate.opsForValue().get(redisKey);
        Date qrGmtCreate = null;
        if(null == qrCodeObj) {
            CommonReturnMessage commonReturnMessage = new CommonReturnMessage(BusinessTypeEnum.SCAN_LOGIN.getCode(),
                    CodeMessageEnum.QR_INVALID.getCode(), "二维码已失效");
            LogUtil.error(prefix + "二维码已失效" + paramTip);
            ChannelSupervise.sendToUser(ctx, JsonUtils.beanToJson(commonReturnMessage));
            return;
        } else {
            ScanLoginQRDTO scanLoginQRDTO = JsonUtils.jsonToBean(StringUtil.changeNullToEmpty(qrCodeObj), ScanLoginQRDTO.class);
            qrGmtCreate = scanLoginQRDTO.getGmtCreate();
        }

        if(StringUtil.isEmpty(message)) {
            ChannelSupervise.sendCommonReturn(ctx, BusinessTypeEnum.SCAN_LOGIN.getCode(),
                    CodeMessageEnum.shareErrorParam.getCode(), "消息为空无效");
        }
        ScanLoginMessage scanLoginMessage = null;
        try {
            scanLoginMessage = JsonUtils.jsonToBean(message, ScanLoginMessage.class);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "消息内容格式有误" + paramTip);
            ChannelSupervise.sendCommonReturn(ctx, BusinessTypeEnum.SCAN_LOGIN.getCode(),
                    CodeMessageEnum.shareErrorParam.getCode(), "消息内容格式有误");
        }

        //处理接收到的消息
        this.handleMessage(prefix, scanLoginMessage, roomName, ctx, qrGmtCreate);
    }

    /**
     * @description: 处理接收到的消息
     * @param prefix
     * @param scanLoginMessage
     * @param roomName
     * @param ctx
     * @param qrGmtCreate
     * @return
     */
    private void handleMessage(String prefix, ScanLoginMessage scanLoginMessage, String roomName, ChannelHandlerContext ctx, Date qrGmtCreate) {
        String msgType = StringUtil.trim(scanLoginMessage.getMsgType());
        String action = StringUtil.trim(scanLoginMessage.getAction());
        String actionVal = StringUtil.trim(scanLoginMessage.getActionVal());
        String token = StringUtil.trim(scanLoginMessage.getToken());
        String resultAction = action;
        String serverName = StringUtil.trim(scanLoginMessage.getServerName());

        String code = CodeMessageEnum.success.getCode();
        String message = "";
        String tempAuth = scanLoginMessage.getTempAuth();
        String schoolDomain = null;

        //是否原路回消息
        Boolean isReturnMsg = Boolean.TRUE;

        //是否转发消息
        Boolean isTransfer = Boolean.FALSE;
        //转发的接收人loginId标识
        String transferReceiverId = "";
        String transferAction = action;
        String transferCode = "200";
        String transferMessage = "";

        //扫码登录的 channelId与标识ID(loginId)的关联关系
        String lcRelationRedisKey = String.format(GlobalConfig.ScanLoginIdChannelRelation, roomName);
        //扫码登录的 APP token与 生成的临时授权码的关联关系
        String tempAuthRelationRedisKey = String.format(GlobalConfig.ScanLoginTempAuthRelation, roomName);

        HashOperations hashOperations = this.stringRedisTemplate.opsForHash();
        ValueOperations valueOperations = this.stringRedisTemplate.opsForValue();
        try {
            String channelId = ctx.channel().id().asLongText();
            if(!ScanLoginMsgTypeEnum.contains(msgType)) {
                code = CodeMessageEnum.shareErrorParam.getCode();
                message = "msgType值非法";
            }
            LoginUser loginUser = null;
            //若是TV、WEB端
            if(!msgType.equals(ScanLoginMsgTypeEnum.APP_SCAN_LOGIN.getCode())) {
                if(!ScanLoginActionEnum.containsTvWebAction(action)) {
                    code = CodeMessageEnum.success.getCode();
                    message = "action值非法";
                } else {
                    //
                    if(action.equals(ScanLoginActionEnum.FEED_BACK.getCode())) {
                        if(!actionVal.equals("success") && !actionVal.equals("fail")) {
                            code = CodeMessageEnum.shareErrorParam.getCode();
                            message = "actionVal值非法";
                        }
                    }
                }
            } else { //若是APP端
                if(!ScanLoginActionEnum.containsAppAction(action)) {
                    code = CodeMessageEnum.shareErrorParam.getCode();
                    message = "action值非法";
                } else {
                    //校验TOKEN
                    if (StringUtil.isEmpty(token)) {
                        code = CodeMessageEnum.errorAdminAuth.getCode();
                        message = "token不能为空";
                    } else {
                        if (StringUtil.isEmpty(serverName)) {
                            code = CodeMessageEnum.shareErrorParam.getCode();
                            message = "serverName不能为空";
                        } else {
                            //
                            loginUser = this.loginUserUtil.getLoginUser(serverName, token);
                            if (loginUser == null || loginUser.getUserID() == null) {
                                code = CodeMessageEnum.errorAdminAuth.getCode();
                                message = CodeMessageEnum.errorAdminAuth.getMsg();
                            }
                        }
                    }
                }
            }

            String loginId = roomName;
            if("200".equals(code) || "common.success".equals(code)) {
                //若是APP端
                if (msgType.equals(ScanLoginMsgTypeEnum.APP_SCAN_LOGIN.getCode())) {
                    loginId = token;
                }

                //动作
                //若是confirm-确认连接时
                if(ScanLoginActionEnum.CONFIRM.getCode().equals(action)) {
                    //置房间、channel、标识的实例内存
                    ChannelSupervise.scanLoginRooms.putIfAbsent(roomName, new ConcurrentHashMap<>());
                    ChannelSupervise.scanLoginRooms.get(roomName).put(ctx.channel(), loginId);

                    //置redis缓存
                    hashOperations.put(lcRelationRedisKey, channelId, loginId);
                    this.stringRedisTemplate.expire(lcRelationRedisKey, this.scanLoginCodeExpire, TimeUnit.MINUTES);

                    ClientInfo clientInfo = CommonHandler.getClientInfo(ctx);
                    clientInfo.setLogin(true);
                    ctx.channel().attr(AttributeKey.valueOf("clientInfo")).set(clientInfo);

                    //若是APP端，则得转发消息通知下TV或WEB端：扫描成功
                    if(ScanLoginMsgTypeEnum.APP_SCAN_LOGIN.getCode().equals(msgType)) {
                        transferAction = ScanLoginActionEnum.SCAN.getCode();
                        transferMessage = "扫描成功";

                        isTransfer = Boolean.TRUE;
                        transferReceiverId = roomName;
                    }
                }
                //是auth-登录授权（APP端）
                else if(ScanLoginActionEnum.AUTH.getCode().equals(action)) {
                    //若多次授权存在，则返回原有的
                    if(hashOperations.hasKey(tempAuthRelationRedisKey, token)) {
                        tempAuth = StringUtil.changeNullToEmpty(hashOperations.get(tempAuthRelationRedisKey, token));
                    }
                    //否则产生新的临时授权码给TV、WEB
                    else {
                        tempAuth = GlobalConfig.ScanLoginTempAuthRedisKeyPrefix + RandomUtil.getuuid();
                        //置APP token与生成的临时授权码的关联关系 redis缓存
                        hashOperations.put(tempAuthRelationRedisKey, token, tempAuth);
                        Long currentML = System.currentTimeMillis();
                        //剩余有效期
                        Long remainExpire = qrGmtCreate.getTime() + this.scanLoginCodeExpire*60*1000 - currentML;
                        this.stringRedisTemplate.expire(tempAuthRelationRedisKey, remainExpire, TimeUnit.MILLISECONDS);

                        TempAuthDTO tempAuthDTO = new TempAuthDTO();
                        tempAuthDTO.setToken(token);
                        tempAuthDTO.setRoomName(roomName);
                        //置 临时登录授权码与APP登录TOKEN的 REDIS缓存
                        valueOperations.set(tempAuth, JsonUtils.beanToJson(tempAuthDTO), this.scanLoginCodeExpire, TimeUnit.MINUTES);
                    }
                    //app扫码登录对应的网校域名
                    schoolDomain = serverName;

                    //暂不原路回消息给APP
                    isReturnMsg = Boolean.FALSE;

                    //转发消息给TV或WEB端
                    transferAction = ScanLoginActionEnum.AUTH.getCode();
                    transferMessage = "APP授权成功";
                    isTransfer = Boolean.TRUE;
                    transferReceiverId = roomName;
                }
                //是feedBack-反馈登录结果（TV或WEB端）
                else if(ScanLoginActionEnum.FEED_BACK.getCode().equals(action)
                        && !msgType.equals(ScanLoginMsgTypeEnum.APP_SCAN_LOGIN.getCode())) {
                    //action为feedBack时则必填
                    //校验临时授权码
                    if (StringUtil.isEmpty(tempAuth)) {
                        code = CodeMessageEnum.errorAdminAuth.getCode();
                        message = "tempAuth不能为空";
                    } else {
                        Object appTokenObj = valueOperations.get(tempAuth);
                        if(ObjectUtils.isEmpty(appTokenObj)) {
                            code = CodeMessageEnum.TEMP_AUTH_INVALID.getCode();
                            message = CodeMessageEnum.TEMP_AUTH_INVALID.getMsg();
                        }
                        //转发给对应的APP通知下 TV或WEB登录结果
                        else {
                            TempAuthDTO tempAuthDTO = JsonUtils.jsonToBean(StringUtil.changeNullToEmpty(appTokenObj), TempAuthDTO.class);
                            String appToken = tempAuthDTO.getToken();
                            isTransfer = Boolean.TRUE;
                            transferReceiverId = appToken;

                            transferAction = ScanLoginActionEnum.AUTH.getCode();
                            //action为feedBack时，登录成功则放success，否则放fail；
                            if(!"success".equals(actionVal)) {
                                transferCode = CodeMessageEnum.QR_INVALID.getCode();
                                transferMessage = CodeMessageEnum.QR_INVALID.getMsg();
                            }
                            //若是成功，则
                            else {
                                //删除临时授权码的REDIS缓存
                                this.stringRedisTemplate.delete(tempAuth);
                                //删除二维码的缓存使失效
                                this.stringRedisTemplate.delete(GlobalConfig.SCAN_LOGIN_CODE_REDIS_KEY + roomName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, prefix + "未知异常");
            code = CodeMessageEnum.shareErrorParam.getCode();
            message = "未知异常，请重试";
        }

        //原路回消息
        if(isReturnMsg) {
            ScanLoginResult scanLoginResult = new ScanLoginResult();
            scanLoginResult.setAction(resultAction);
            scanLoginResult.setCode(code);
            scanLoginResult.setMessage(message);
            String resultJson = JsonUtils.beanToJson(scanLoginResult);
            LogUtil.info(prefix + "原路回消息结果内容：" + resultJson);
            //发送消息回去
            ChannelSupervise.sendToUser(ctx, resultJson);
        }

        //转发消息
        if(isTransfer) {
            ScanLoginResult scanLoginResult = new ScanLoginResult();
            scanLoginResult.setAction(transferAction);
            scanLoginResult.setCode(transferCode);
            scanLoginResult.setMessage(transferMessage);
            scanLoginResult.setTempAuth(tempAuth);
            scanLoginResult.setSchoolDomain(schoolDomain);

            RoomMsg roomMsg = new RoomMsg();
            roomMsg.setRoomName(roomName);
            roomMsg.setMessage(JsonUtils.beanToJson(scanLoginResult));

            Map<String, String> lcMap = hashOperations.entries(lcRelationRedisKey);
            Iterator iterator = lcMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                String channelId = entry.getKey();
                String loginId = entry.getValue();
                if(transferReceiverId.equals(loginId)) {
                    roomMsg.setChannelId(channelId);
                    String msg = JsonUtils.beanToJson(roomMsg);
                    LogUtil.info(prefix + "转发消息内容：" + msg);
                    newBroadcastService.sendScanLoginMessage(roomMsg);
                }
            }
        }
    }

}
