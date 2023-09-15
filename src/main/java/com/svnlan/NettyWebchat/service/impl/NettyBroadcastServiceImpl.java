package com.svnlan.NettyWebchat.service.impl;

import com.svnlan.NettyWebchat.Domain.ChannelSupervise;
import com.svnlan.NettyWebchat.Domain.RoomMsg;
import com.svnlan.NettyWebchat.service.NettyBroadcastService;
import com.svnlan.utils.*;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Description:
 */
@Service
public class NettyBroadcastServiceImpl implements NettyBroadcastService {


    @Override
    public void sendScanLoginMessage(RoomMsg roomMsg) {
        String roomMame = roomMsg.getRoomName();
        if (StringUtil.isEmpty(roomMsg.getChannelId())) {
            if (ChannelSupervise.scanLoginRooms.get(roomMame) != null) {
                for (Channel channel : ChannelSupervise.scanLoginRooms.get(roomMame).keySet()) {
                    ChannelSupervise.sendToUser(channel, roomMsg.getMessage());
                }
            }
        } else {
            if (ChannelSupervise.scanLoginRooms.get(roomMame) != null) {
                for (Channel channel : ChannelSupervise.scanLoginRooms.get(roomMame).keySet()) {
                    if (channel.id().asLongText().equals(roomMsg.getChannelId())) {
                        ChannelSupervise.sendToUser(channel, roomMsg.getMessage());
                    }
                }
            }
        }
    }

}
