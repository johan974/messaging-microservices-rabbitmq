package com.github.pedroluiznogueira.microservices.messaging.service;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

public interface RabbitQueueService {
    void addNewQueue(String queueName,String exchangeName,String routingKey);
    void addQueueToListener(String listenerId,String queueName);
    void removeQueueFromListener(String listenerId,String queueName);
    Boolean checkQueueExistOnListener(String listenerId,String queueName);
    AbstractMessageListenerContainer getMessageListenerContainerById(String listenerId);
}
