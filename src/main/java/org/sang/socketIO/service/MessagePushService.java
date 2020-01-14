package org.sang.socketIO.service;

import com.corundumstudio.socketio.SocketIOServer;

import java.util.List;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:23
 */
public interface MessagePushService {
    void init(SocketIOServer server);

    /**
     * 消息推送
     *
     * @param messageData
     * @param rooms
     * @param messageType
     */
    void pushMessage(String messageData, List<String> rooms, String messageType);
}
