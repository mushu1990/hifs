package com.hifs.hicore.config;

import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationFailedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartupListener.class);

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        if (event.getException() instanceof RemotingConnectException) {
            // 捕获指定的异常并处理
            log.error("启动时捕获到 RemotingConnectException 异常，但不会导致应用崩溃: {}", event.getException().getMessage());
        }
    }
}
