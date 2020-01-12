package org.sang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        kafkaProducerSevice.sendMsg("test","ceshi");
    }
}
