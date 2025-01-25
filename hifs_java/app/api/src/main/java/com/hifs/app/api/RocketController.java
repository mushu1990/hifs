package com.hifs.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RocketController {
    @Autowired
    private RocketMQProducer rocketMQProducer;

    @GetMapping("/sendSync")
    public String sendSync(@RequestParam String message) {
        rocketMQProducer.sendSyncMessage(message);


        return "同步消息发送成功";
    }

    @GetMapping("/sendAsync")
    public String sendAsync(@RequestParam String message) {
        rocketMQProducer.sendAsyncMessage(message);
        return "异步消息发送中";
    }

    @GetMapping("/sendOneWay")
    public String sendOneWay(@RequestParam String message) {
        rocketMQProducer.sendOneWayMessage(message);
        return "单向消息发送成功";
    }
}

