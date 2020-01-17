package org.sang.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 17:05
 */
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "socket.io")
public class SocketIOConfig {

    @Value("${socket.io.host}")
    private String host;

    @Value("${socket.io.port}")
    private Integer port;

//    @Value("${socket.io.bossCount}")
    private int bossCount = 1;

//    @Value("${socket.io.workCount}")
    private int workCount = 100;

//    @Value("${socket.io.allowCustomRequests}")
    private boolean allowCustomRequests;
    /**
     * 协议升级超时时间（ms），默认10秒。HTTP握手升级为ws协议超时时间
     */
//    @Value("${socket.io.upgradeTimeout}")
    private int upgradeTimeout = 1000000;

    /**
     * Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
     */
//    @Value("${socket.io.pingTimeout}")
    private int pingTimeout = 6000000;

    /**
     * Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
     */
//    @Value("${socket.io.pingInterval}")
    private int pingInterval = 25000;

    @Bean("socketServer")
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        return new SocketIOServer(config);
    }
}
