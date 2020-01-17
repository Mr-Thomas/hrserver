package org.sang.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.sang.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
public class KafkaProducerSevice {
    @Autowired
    private KafkaConfig kafkaConfig;
    private KafkaProducer<String, String> producer;

    @PostConstruct
    private void  run(){
        log.info("初始化:producer");
        Map<String, Object> configs = new HashMap<String, Object>();
        //设置集群地址
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        //设置key器
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //设置值序列化器
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //设置重试次数
        configs.put(ProducerConfig.LINGER_MS_CONFIG, 500l);
        producer = new KafkaProducer<>(configs);
    }

    /**
     * 往kafka发送消息
     * @param topic
     * @param msg
     */
    public void sendMsg(String topic,String msg){
        ProducerRecord<String, String> record = new ProducerRecord<>(topic,null, msg);
        try {
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}
