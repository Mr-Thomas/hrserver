package org.sang.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.sang.bean.RedisMessageVo;
import org.sang.socketIO.service.MessagePushService;
import org.sang.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:55
 * redis消息队列消息处理类，处理topic为socketIoMessage的消息
 */
@Slf4j
@Component
public class RedisMessageListener implements MessageListener {

    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private ThreadPoolExecutor webPushThreadPool;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("RedisMessageListener 收到监听消息...");
        try {
            webPushThreadPool.execute(() -> {
                //序列化redis队列中的消息，并调用wsConnectManager.push方法推送消息
                RedisMessageVo redisMessageVo = JsonUtil.fastJsonParse(JsonUtil.fastJsonParse(message.toString(), String.class), RedisMessageVo.class);
                if (ObjectUtils.isEmpty(redisMessageVo)) {
                    return;
                }
                messagePushService.pushMessage(redisMessageVo.getMessageData(), redisMessageVo.getRoomLists(), redisMessageVo.getMessageType());
            });
        } catch (Exception e) {
            log.error("RedisMessageListener exception", e);
        }
    }
}
