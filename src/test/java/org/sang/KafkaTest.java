package org.sang;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sang.factory.KafkaConsumerFactory;
import org.sang.kafka.KafkaProducerSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class KafkaTest {

    @Autowired
    private KafkaProducerSevice kafkaProducerSevice;

    @Test
    public void produceTest(){
        kafkaProducerSevice.sendMsg("test","hahaahhahaha");
    }

    @Test
    public void consumerTest(){
        KafkaConsumer<String, String> consumer = KafkaConsumerFactory.getInstance().getKafkaConsumer("test","11");
        ConsumerRecords<String, String> records = consumer.poll(1000L);
        while (true){
            if(records.isEmpty()){
                log.info("未从topic：{}，中取到消息","test");
                continue;
            }
            records.forEach(o ->{
                log.info("消息：{}",o.value());
            });
            consumer.commitAsync();
        }

    }
}
