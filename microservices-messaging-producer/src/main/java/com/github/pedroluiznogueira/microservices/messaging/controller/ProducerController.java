package com.github.pedroluiznogueira.microservices.messaging.controller;

import com.github.pedroluiznogueira.microservices.messaging.domain.MessageForQueue;
import com.github.pedroluiznogueira.microservices.messaging.domain.QueueInstance;
import com.github.pedroluiznogueira.microservices.messaging.domain.User;
import com.github.pedroluiznogueira.microservices.messaging.service.AbonnementListener;
import com.github.pedroluiznogueira.microservices.messaging.service.ProducerService;
import com.github.pedroluiznogueira.microservices.messaging.service.RabbitQueueService;
import com.github.pedroluiznogueira.microservices.messaging.service.RabbitQueueServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// Design: 1 exchange and 1 binding => 1 abonnement listener
@RestController
@RequestMapping("/api/")
public class ProducerController {
    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    private ProducerService producerService;
    private RabbitQueueService rabbitQueueService;
    private Map<String, SimpleMessageListenerContainer> listeners = new HashMap<>();

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Autowired
    public ProducerController(ProducerService producerService, RabbitQueueServiceImpl rabbitQueueService) {
        this.producerService = producerService;
        this.rabbitQueueService = rabbitQueueService;
    }

    @Value("${app.message}")
    private String response;

    @PostMapping("/produce")
    public ResponseEntity<String> sendMessage(@RequestBody User user) {
        producerService.sendMessage(user);
        logger.info("user sent: " + user);
        return ResponseEntity.ok(response);
    }

    @PostMapping( "/addqueue")
    public ResponseEntity<String> addDynamicQueue(@RequestBody QueueInstance queueInstance) {
        rabbitQueueService.addNewQueue(queueInstance.getQueueName(), exchangeName, routingKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/produceforqueue")
    public ResponseEntity<String> produceForDynamicQueue(@RequestBody MessageForQueue messageForQueue) {
        producerService.sendMessage(messageForQueue.getMessage(), exchangeName, routingKey);
        logger.info("user sent: " + messageForQueue);
        return ResponseEntity.ok(response);
    }
}
