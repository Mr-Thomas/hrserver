package org.sang;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sang.factory.KafkaConsumerFactory;
import org.sang.kafka.KafkaProducerSevice;
import org.sang.kafka.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class KafkaTest {

    @Autowired
    private KafkaProducerSevice kafkaProducerSevice;

    @Autowired
    private MessageProducer messageProducer;

    @Test
    public void produceTest(){
        kafkaProducerSevice.sendMsg("alarm_push_warn","0055");
    }

    @Test
    public void mesProduceTest(){
        messageProducer.sendMessage("alarm_push_warn","0008");
    }

    @Test
    public void consumerTest(){
        KafkaConsumer<String, String> consumer = KafkaConsumerFactory.getInstance().getKafkaConsumer("alarm_push_warn","11");
        while (true){
            ConsumerRecords<String, String> records = consumer.poll(1000L);
            if(records.isEmpty()){
                log.info("未从topic：{}，中取到消息","test");
                continue;
            }
            records.forEach(o ->{
                log.info("消息:{}",o.value());
            });
            consumer.commitAsync();
        }

    }
}
