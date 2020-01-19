package org.sang.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.sang.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 16:42
 */
@Component
@Slf4j
@SpringBootConfiguration
public class AlarmInfoConsumer {
    @Autowired
    private RedisUtil redisUtil;

    @KafkaListener(topics = "${kafka.topic.alarmPushTopic}", groupId = "${kafka.group.alarmPushTopic}",
            containerFactory = "kafkaJsonListenerContainerFactory")
    public void processMessage(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        try {
            log.info("预警topic:{}; kafkaKey:{}; 收到报警kafka消息:{};",consumerRecord.topic(),consumerRecord.key(),consumerRecord.value());
            redisUtil.convertAndSend("socketIoMessage", value);
        } catch (Exception e) {
            log.error("device DeviceMsgConsumer processMessage exception ", e);
        }
    }

}

