package org.sang.bean;

import lombok.Data;

import java.util.List;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/14 18:14
 * redis消息队列消息实体
 */
@Data
public class RedisMessageVo {

    private String messageData;

    private List<String> roomLists;

    private String messageType;
}
