package org.sang.socketIO.init;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sang.redis.RedisUtil;
import org.sang.socketIO.KafkaConsumerListenerHandler;
import org.sang.socketIO.service.MessagePushService;
import org.sang.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 17:04
 */
@Component
@SpringBootConfiguration
@Order(0)
@Slf4j
public class SocketIORunner implements CommandLineRunner {

    public final String SOCKET_IO_ROOMS = "SOCKET_IO_ROOMS";

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SocketIOServer socketServer;
    @Autowired
    private KafkaConsumerListenerHandler kafkaConsumerListenerHandler;

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) throws Exception {
        try {
            SocketIONamespace socketNamespace = socketServer.addNamespace("/socket.io");
            //初始化自定义socketServer @Bean("socketServer")
            messagePushService.init(socketServer);
            //添加客户端连接监听器
            socketNamespace.addConnectListener(socketIOClient -> {
                //socketIo的前后端交互
                try {
                    String remoteAddress = socketIOClient.getRemoteAddress().toString();
                    //获取ip
                    String clientIp = remoteAddress.substring(1, remoteAddress.indexOf(":"));
                    log.info("客户端已连接:{}", clientIp);
//                    String token = getToken(socketIOClient);
//                    if (StringUtils.isBlank(token)) {
//                        log.error("客户端（ip:[{}]）握手数据未包含token，连接已断开！", clientIp);
//                        socketIOClient.disconnect();
//                        return;
//                    }
//                    校验解析token,处理数据
                    String userId = "";
                    //设置推送房间
                    socketIOClient.joinRoom(userId);
                    //设置用户信息
                    socketIOClient.set("userId", userId);
                    redisUtil.hSet(SOCKET_IO_ROOMS, userId, JsonUtil.object2json(socketIOClient.getAllRooms()));
                    log.info("客户端（ip:[{}]）userId=[{}]成功加入以下房间:{}", clientIp, userId, JsonUtil.object2json(socketIOClient.getAllRooms()));
                } catch (Exception e) {
                    log.error("SocketIOClient onConnect error", e);
                    e.printStackTrace();
                }
            });
            //添加客户端断开连接事件
            socketNamespace.addDisconnectListener(socketIOClient->{
                try {
                    String remoteAddress = socketIOClient.getRemoteAddress().toString();
                    String clientIp = remoteAddress.substring(1, remoteAddress.indexOf(":"));
                    //获取用户信息
                    String userId = socketIOClient.get("userId");
                    //获取所以房间
                    Set<String> allRooms = socketIOClient.getAllRooms();
                    for (String room : allRooms) {
                        socketIOClient.leaveRoom(room);
                    }
                    if (StringUtils.isNotEmpty(userId)) {
                        socketIOClient.leaveRoom(userId);
                    }
                    redisUtil.hDel(SOCKET_IO_ROOMS, userId);
                    log.info("客户端（ip:[{}]）userId=[{}]已断开与socket服务器的连接", clientIp, userId);
                } catch (Exception e) {
                    log.info("SocketIOClient onDisconnect error", e);
                    e.printStackTrace();
                }finally {
                    socketIOClient.disconnect();
                }
            });
            //启动
            socketServer.start();
            //启动kafka监听容器
            kafkaConsumerListenerHandler.startAllListener();
        } catch (Exception e) {
            log.error("StartupRunner exception", e);
            e.printStackTrace();
        }
    }

    /**
     * SocketIO自销毁(避免重启项目服务端口占用问题)
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception {
        if (socketServer != null) {
            socketServer.stop();
            socketServer = null;
        }
        log.info("socket.io销毁成功！");
    }

    private String getToken(SocketIOClient socketIOClient) throws Exception {
        Map<String, List<String>> urlParams = socketIOClient.getHandshakeData().getUrlParams();
        if (urlParams != null && urlParams.containsKey("token")) {
            List<String> tokens = urlParams.get("token");
            if (tokens != null && !tokens.isEmpty()) {
                return tokens.get(0);
            }
        }
        return null;
    }
}
