package com.svnlan.NettyWebchat.service;

import com.svnlan.NettyWebchat.Domain.RoomMsg;

/**
 * @Author:
 * @Description:
 */
public interface NettyBroadcastService {


    /**
     * @description: 发给某房间指定某channel消息
     * @param roomMsg
     * @return void
     */
    void sendScanLoginMessage(RoomMsg roomMsg);


}
