package org.sang.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 17:59
 * @description: redis队列消息监听设置类
 */
@Component
public class RedisSubListenerConfig {

    @Autowired
    RedisMessageListener redisMessageListener;

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean("redisMessageContainer")
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //添加消息处理器listenerAdapter，设置订阅的topic
        container.addMessageListener(listenerAdapter, new PatternTopic("socketIoMessage"));
        //这个container 可以添加多个 messageListener
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器
     * 利用反射技术调用消息处理器的业务方法
     *
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(redisMessageListener);
    }
}

