package com.github.pedroluiznogueira.microservices.messaging.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RabbitMqAbonnementenQueueService implements AbonnementenQueueService {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqAbonnementenQueueService.class);

    private static final String EXCHANGE_NAME = "abo-exchange";
    private static final String ROUTING_KEY = "abo-rk";

    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;

    private final RabbitAdmin rabbitAdmin;
    private final AbstractMessageListenerContainer container;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqAbonnementenQueueService(final RabbitAdmin rabbitAdmin, final AbstractMessageListenerContainer container,
                                            final RestTemplate restTemplate, final RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.container = container;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void addQueue(AbonnementQueue abonnementQueue) {
        Queue queue = new Queue(abonnementQueue.getNaam(), true, false, false);
        Binding binding = new Binding(
                abonnementQueue.getNaam(),
                Binding.DestinationType.QUEUE,
                EXCHANGE_NAME,
                ROUTING_KEY,
                null
        );
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        log.info("queue {} is aangemaakt", abonnementQueue.getNaam());
        if (abonnementQueue.isGeactiveerd()) {
            activateQueue(abonnementQueue.getNaam());
        }
    }

    @Override
    public void deleteQueue(String queueName) {
        deactivateQueue(queueName);
        rabbitAdmin.deleteQueue(queueName);
        log.info("queue {} is verwijderd" + queueName);
    }

    @Override
    public void activateQueue(String queueName) {
        if (!isQueueActive(queueName)) {
            container.addQueueNames(queueName);
            log.info("queue {} is geactiveerd", queueName);
        } else {
            log.info("queue {} is al actief", queueName);
        }
    }

    @Override
    public void deactivateQueue(String queueName) {
        if (isQueueActive(queueName)) {
            container.removeQueueNames(queueName);
            log.info("queue {} is gedeactiveerd", queueName);
        } else {
            log.info("queue {} is al inactief", queueName);
        }
    }

    @Override
    public Boolean isQueueActive(String queueName) {
        try {
            String[] queueNames = container.getQueueNames();
            boolean geactiveerd = Arrays.asList(queueNames).contains(queueName);
            log.info("Huidige activeringsstatus van queue {} is {}", queueName, geactiveerd);
            return geactiveerd;
        } catch (Exception e) {
            log.error("Fout bij het controleren of queue {} actief is: ", queueName,e);
            return Boolean.FALSE;
        }
    }

    @Override
    public List<AbonnementQueue> getAbonnementQueues() {
        RabbitMQQueue[] queues = restTemplate.getForObject("http://" + rabbitMqHost + ":15672/api/queues", RabbitMQQueue[].class);
        if (queues != null) {
            return Arrays.stream(queues)
                    .filter(queue -> queue.getName().startsWith("abo-"))
                    .map(queue -> new AbonnementQueue(queue.getName(), isQueueActive(queue.getName()) ? AbonnementQueue.Status.ACTIEF : AbonnementQueue.Status.INACTIEF)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void sendMessage(AbonnementQueue abonnementQueue, String testMessage) {
        rabbitTemplate.convertAndSend(abonnementQueue.getNaam(), testMessage);
    }

    //@PostConstruct
    public void loadQueuesOnStartup() {
        getAbonnementQueues().forEach(queue -> activateQueue(queue.getNaam()));
    }
}
