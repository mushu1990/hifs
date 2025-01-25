package com.hifs.hicore.config;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


public class RocketMQConfiguration extends RocketMQAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RocketMQConfiguration.class);

    public RocketMQConfiguration(Environment environment) {
        super(environment);
    }

    @Bean
    @Override
    public DefaultMQProducer defaultMQProducer(RocketMQProperties rocketMQProperties) {
        try {
            // 尝试获取默认的 MQProducer 配置
            DefaultMQProducer producer = super.defaultMQProducer(rocketMQProperties);
            producer.setSendMsgTimeout(5000); // 自定义超时时间
            log.info("DefaultMQProducer initialized successfully.");
            return producer;
        } catch (Exception e) {
            // 捕获连接失败或配置错误的异常，并仅打印错误信息
            log.error("Failed to initialize DefaultMQProducer: {}", e.getMessage());
            // 不做处理，继续执行
            return null;  // 返回 null 或者其他默认的行为
        }
    }

    @Bean
    @Override
    public DefaultLitePullConsumer defaultLitePullConsumer(RocketMQProperties rocketMQProperties) throws MQClientException {
        try {
            // 尝试获取默认的 LitePullConsumer 配置
            DefaultLitePullConsumer consumer = super.defaultLitePullConsumer(rocketMQProperties);
            consumer.setPullBatchSize(16); // 自定义拉取批次大小
            log.info("DefaultLitePullConsumer initialized successfully.");
            return consumer;
        } catch (Exception e) {
            // 捕获连接失败或配置错误的异常，并仅打印错误信息
            log.error("Failed to initialize DefaultLitePullConsumer: {}", e.getMessage());
            // 不做处理，继续执行
            return null;  // 返回 null 或者其他默认的行为
        }
    }

    @Bean
    @Override
    public RocketMQTemplate rocketMQTemplate(RocketMQMessageConverter rocketMQMessageConverter) {
        try {
            // 尝试获取默认的 RocketMQTemplate 配置
            RocketMQTemplate rocketMQTemplate = super.rocketMQTemplate(new RocketMQMessageConverter());
            log.info("RocketMQTemplate initialized successfully.");
            return rocketMQTemplate;
        } catch (Exception e) {
            // 捕获初始化 RocketMQTemplate 时的异常，并仅打印错误信息
            log.error("Failed to initialize RocketMQTemplate: {}", e.getMessage());
            // 不做处理，继续执行
            return null;  // 返回 null 或者其他默认的行为
        }
    }
}

