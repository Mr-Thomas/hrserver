package org.sang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sang.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 11:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {
    private final String KEY = "DEVICE_KEY";

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void addRedis() {
        List list = Arrays.asList("1", "2", "3", "4", "5");
        list.forEach(ids -> {
            long currentTimestamp = System.currentTimeMillis();
            log.info("currentTimestamp:{}", currentTimestamp);
            redisUtil.add(KEY, ids, currentTimestamp);
        });
    }
}
