package com.hifs.hicore.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class RocketMQConfiguration {
    /**
     * 当 rocketmq.enabled=false 时，创建 Mock Bean 并标记为 @Primary
     * 覆盖自动配置的 RocketMQTemplate
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "false")
    public RocketMQTemplate mockRocketMQTemplate() {
        return new RocketMQTemplate() {
            @Override
            public void afterPropertiesSet() {
                System.out.println("[Mock] RocketMQTemplate 已初始化（无真实连接）");
            }
        };
    }
}

