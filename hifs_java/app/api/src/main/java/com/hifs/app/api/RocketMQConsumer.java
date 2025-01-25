package com.hifs.app.api;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "demo-topic", consumerGroup = "consumer-group", messageModel = MessageModel.CLUSTERING)
public class RocketMQConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.printf("收到消息: %s ", s);
    }
}

