package com.github.pedroluiznogueira.microservices.messaging.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SysOutMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(SysOutMessageListener.class);

    @Override
    public void onMessage(Message message) {
        logger.info( "Simple SysOut listener: {}", message);
    }
}
