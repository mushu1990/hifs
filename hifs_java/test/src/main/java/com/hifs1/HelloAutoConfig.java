package com.hifs1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HelloProperty.class)
@ConditionalOnClass(HelloService.class)
public class HelloAutoConfig {
    @Autowired
    HelloProperty helloProperties;

    @Bean
    HelloService helloService() {
        HelloService helloService = new HelloService();
        helloService.setMessage(helloProperties.getMessage());
        return helloService;
    }
}
