package org.sang.kafka;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.sang.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

/**
 * @Author: Mr.Thomas
 * @Date: 2020/1/18 15:15
 */
@Component
@Slf4j
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("unchecked")
    public void sendMessage(String topicName, Object deviceMessage) {
        try {
            Map<String, Object> headerParam = Maps.newHashMap();
            headerParam.put(KafkaHeaders.TOPIC, topicName);
            Message message = new GenericMessage<>(deviceMessage, headerParam);
            ListenableFuture listenableFuture = kafkaTemplate.send(message);
            listenableFuture.addCallback(o ->
                            log.warn("send message to kafka success !!! topicName= " + topicName + ",return " + JsonUtil.object2json(o))
                    , throwable ->
                            log.error("send message to kafka fail !!! topicName= " + topicName + " error " + throwable.getMessage()));
        } catch (Exception e) {
            log.error("send message to kafka error topicName= " + topicName, e);
        }
    }
}
