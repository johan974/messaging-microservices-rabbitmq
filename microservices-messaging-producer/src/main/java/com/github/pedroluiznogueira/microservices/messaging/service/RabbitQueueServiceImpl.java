package com.github.pedroluiznogueira.microservices.messaging.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitQueueServiceImpl implements RabbitQueueService {
    private static final Logger log = LoggerFactory.getLogger(RabbitQueueServiceImpl.class);
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void addNewQueue(String queueName, String exchangeName, String routingKey) {
        Queue queue = new Queue(queueName, true, false, false);
        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                routingKey,
                null
        );
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        addQueueToListener(exchangeName, queueName);
    }

    @Override
    public void addQueueToListener(String listenerId, String queueName) {
        log.info("adding queue : {} to listener with id : {}", queueName, listenerId);
        if (Boolean.FALSE.equals(checkQueueExistOnListener(listenerId, queueName))) {
            this.getMessageListenerContainerById(listenerId).addQueueNames(queueName);
            log.info("queue ");
        } else {
            log.info("given queue name : {} not exist on given listener id : {}", queueName, listenerId);
        }
    }

    @Override
    public void removeQueueFromListener(String listenerId, String queueName) {
        log.info("removing queue : {} from listener : {}", queueName, listenerId);
        if (Boolean.TRUE.equals(checkQueueExistOnListener(listenerId, queueName))) {
            this.getMessageListenerContainerById(listenerId).removeQueueNames(queueName);
            log.info("deleting queue from rabbit management");
            this.rabbitAdmin.deleteQueue(queueName);
        } else {
            log.info("given queue name : {} not exist on given listener id : {}", queueName, listenerId);
        }
    }

    @Override
    public Boolean checkQueueExistOnListener(String listenerId, String queueName) {
        try {
            log.info("checking queueName : {} exist on listener id : {}", queueName, listenerId);
            log.info("getting queueNames");
            String[] queueNames = this.getMessageListenerContainerById(listenerId).getQueueNames();
            log.info("queueNames : {}", new Gson().toJson(queueNames));
            if (queueNames != null && queueName.length() > 0) {
                log.info("checking {} exist on active queues", queueName);
                for (String name : queueNames) {
                    log.info("name : {} with checking name: {} ", name, queueName);
                    if (name.equals(queueName)) {
                        log.info("queue name exist on listener, returning true");
                        return Boolean.TRUE;
                    }
                }
                return Boolean.FALSE;
            } else {
                log.info("there is no queue exist on listener");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error("Error on checking queue exist on listener");
            log.error(">> moren info", e);
            return Boolean.FALSE;
        }
    }

    public AbstractMessageListenerContainer getMessageListenerContainerById(String listenerId) {
        log.info("getting message listener container by id : " + listenerId);
        return ((AbstractMessageListenerContainer) this.rabbitListenerEndpointRegistry
                .getListenerContainer(listenerId)
        );
    }

//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
//
//    }

//    @Override
//    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setPrefetchCount(1);
//        factory.setConsecutiveActiveTrigger(1);
//        factory.setConsecutiveIdleTrigger(1);
//        factory.setConnectionFactory(connectionFactory);
//        registrar.setContainerFactory(factory);
//        registrar.setEndpointRegistry(rabbitListenerEndpointRegistry());
//        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//    }
}
