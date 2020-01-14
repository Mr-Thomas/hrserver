package org.sang.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:58
 */
@ConfigurationProperties(prefix = "message.web.push.thread-pool")
@SpringBootConfiguration
@Data
public class WebPushThreadPoolConfig {
    /**
     * 核心线程数
     */
    private int corePoolSize = 2;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 5;

    /**
     * 队列容量
     */
    private int queueCapacity = 20;

    /**
     * 线程保活时间，秒为单位
     */
    private int keepAliveSeconds = 60;
}
