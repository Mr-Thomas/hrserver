package org.sang.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.sang.config.WebPushThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:57
 */
@Component
@Slf4j
public class ThreadPoolFactory {
    @Autowired
    private WebPushThreadPoolConfig webPushThreadPoolConfig;
    @Bean("webPushThreadPool")
    public ThreadPoolExecutor initWebPushThreadPool() {
        return new ThreadPoolExecutor(webPushThreadPoolConfig.getCorePoolSize(), webPushThreadPoolConfig.getMaxPoolSize(),
                webPushThreadPoolConfig.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>(webPushThreadPoolConfig.getQueueCapacity()),
                new ThreadFactoryBuilder().setNameFormat("web-push-pool-%d").build(), new WebCustomPolicy());
    }

    public static class WebCustomPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.error("Web线程池队列已满，由调用者处理! ThreadPoolExecutor:{}", e.toString());
            super.rejectedExecution(r, e);
        }
    }
}
