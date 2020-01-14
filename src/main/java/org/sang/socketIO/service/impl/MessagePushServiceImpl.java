package org.sang.socketIO.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.sang.socketIO.service.MessagePushService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:24
 */
@Service
@Slf4j
public class MessagePushServiceImpl implements MessagePushService {

    private SocketIOServer server;

    @Override
    public void init(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void pushMessage(String messageData, List<String> rooms, String messageType) {
        StringBuilder sendRoom = new StringBuilder();
        for (String room : rooms) {
            server.getRoomOperations(room).sendEvent(messageType, messageData);
            sendRoom.append(room).append(",");
        }
        log.info("线程[{}]向房间[{}]推送messageType=[{}]的消息！", Thread.currentThread().getName(), sendRoom.toString(), messageType);
    }
}
