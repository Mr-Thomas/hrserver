package org.sang.socketIO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:14
 * @Description: 启动和停止由@KafkaListener注册的kafka consumer
 */
@Component
@Slf4j
public class KafkaConsumerListenerHandler {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     * 开启监听
     * @param listenerId
     */
    public void startListener(String listenerId) {
        //判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer(listenerId).isRunning()) {
            registry.getListenerContainer(listenerId).start();
        }
        //项目启动的时候监听容器是未启动状态，而resume是恢复的意思不是启动的意思
        registry.getListenerContainer(listenerId).resume();
        log.info(listenerId + "开启监听成功！");
    }

    /**
     * 停止监听
     * @param listenerId
     */
    public void stopListener(String listenerId) {
        registry.getListenerContainer(listenerId).stop();
        log.info(listenerId + "停止监听成功！");
    }

    /**
     * 开启所有监听器
     */
    public void startAllListener() {
        Collection<MessageListenerContainer> allListenerContainers = registry.getAllListenerContainers();
        log.info("当前已注册的监听器数量: [{}]", allListenerContainers.size());
        if (!allListenerContainers.isEmpty()) {
            allListenerContainers.parallelStream().forEach(messageListenerContainer -> startListener(messageListenerContainer.getListenerId()));
        }
    }

    /**
     * 关闭所有监听器
     */
    public void stopAllListener() {
        Collection<MessageListenerContainer> allListenerContainers = registry.getAllListenerContainers();
        log.info("当前已注册的监听器数量: [{}]", allListenerContainers.size());
        if (!allListenerContainers.isEmpty()) {
            allListenerContainers.parallelStream().forEach(messageListenerContainer -> stopListener(messageListenerContainer.getListenerId()));
        }
    }
}
