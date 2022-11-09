package com.github.pedroluiznogueira.microservices.messaging.controller;

import com.github.pedroluiznogueira.microservices.messaging.domain.MessageForQueue;
import com.github.pedroluiznogueira.microservices.messaging.domain.QueueInstance;
import com.github.pedroluiznogueira.microservices.messaging.service.AbonnementQueue;
import com.github.pedroluiznogueira.microservices.messaging.service.AbonnementenQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Design: 1 exchange and 1 binding => 1 abonnement listener
@RestController
@RequestMapping("/api/")
public class ProducerController {
    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);
    private AbonnementenQueueService abonnementenQueueService;

    @Autowired
    public ProducerController(AbonnementenQueueService abonnementenQueueService) {
        this.abonnementenQueueService = abonnementenQueueService;
    }

    @Value("${app.message}")
    private String response;

    @PostMapping("/addqueue")
    public ResponseEntity<String> addDynamicQueue(@RequestBody QueueInstance queueInstance) {
        abonnementenQueueService.addQueue(new AbonnementQueue(queueInstance.getQueueName(), AbonnementQueue.Status.ACTIEF));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/produceforqueue")
    public ResponseEntity<String> produceForDynamicQueue(@RequestBody MessageForQueue messageForQueue) {
        abonnementenQueueService.sendMessage(new AbonnementQueue(messageForQueue.getQueueName(), AbonnementQueue.Status.ACTIEF),
                messageForQueue.getMessage());
        logger.info("user sent: " + messageForQueue);
        return ResponseEntity.ok(response);
    }
}
