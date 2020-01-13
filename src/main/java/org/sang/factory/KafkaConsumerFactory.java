package org.sang.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.sang.config.KafkaConfig;
import org.sang.utils.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class KafkaConsumerFactory {

    private static Map<String,KafkaConsumer<String,String>> consumerMap = new HashMap<>();

    private static KafkaConsumerFactory factory;

    public KafkaConsumerFactory() {
    }

    public static KafkaConsumerFactory getInstance(){
        if(null==factory){
            factory = new KafkaConsumerFactory();
        }
        return factory;
    }

    public KafkaConsumer<String,String> getKafkaConsumer(String topic){
        if(consumerMap.containsKey(topic)){
            return consumerMap.get(topic);
        }else {
            KafkaConsumer<String,String> consumer = createKafkaConsumer(topic);
            consumerMap.put(topic,consumer);
            return consumer;
        }
    }

    private KafkaConsumer<String, String> createKafkaConsumer(String topic) {
        KafkaConfig kafkaConfig = (KafkaConfig) StringUtil.getBean("kafkaConfig");
        Map<String,Object> configs = new HashMap<>();
        //设置集群地址
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        //设置key器
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //设置值序列化器
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,Boolean.FALSE.toString());
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,kafkaConfig.getMaxPollRecords());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,kafkaConfig.getAutoOffsetReset());

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    public KafkaConsumer<String, String> getKafkaConsumer(String topic,String groupId){
        if(consumerMap.containsKey(topic+"_"+groupId)){
            return consumerMap.get(topic);
        }else {
            KafkaConsumer<String,String> consumer = createKafkaConsumer(topic,groupId);
            consumerMap.put(topic+"_"+groupId,consumer);
            return consumer;
        }
    }
    private KafkaConsumer<String, String> createKafkaConsumer(String topic,String groupId) {
        KafkaConfig kafkaConfig = (KafkaConfig) StringUtil.getBean("kafkaConfig");
        Map<String,Object> configs = new HashMap<>();
        //设置集群地址
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        //设置key器
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //设置值序列化器
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,Boolean.FALSE.toString());
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,kafkaConfig.getMaxPollRecords());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,kafkaConfig.getAutoOffsetReset());

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }
}
