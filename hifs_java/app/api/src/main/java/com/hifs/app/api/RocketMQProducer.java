package com.hifs.app.api;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class RocketMQProducer {
    private final String topic = "demo-topic";
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 1.同步发送消息
    public void sendSyncMessage(String message) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build());
        System.out.printf("同步发送结果: %s ", message);
    }

    // 2.异步发送消息
    public void sendAsyncMessage(String message) {
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("异步发送成功: %s ", sendResult);
            }


            @Override
            public void onException(Throwable throwable) {
                System.out.printf("异步发送失败: %s ", throwable.getMessage());
            }
        });
    }

    // 3.单向发送消息
    public void sendOneWayMessage(String message) {
        rocketMQTemplate.sendOneWay(topic, MessageBuilder.withPayload(message).build());
        System.out.println("单向消息发送成功");
    }
}

