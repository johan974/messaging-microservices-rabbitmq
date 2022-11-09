package com.github.pedroluiznogueira.microservices.messaging.service;

import java.util.Collections;
import java.util.List;

public interface AbonnementenQueueService {

    default void addQueue(AbonnementQueue abonnementQueue) {
    }

    default void deleteQueue(String queueName) {
    }

    default void activateQueue(String queueName) {
    }

    default void deactivateQueue(String queueName) {
    }

    default Boolean isQueueActive(String queueName) {
        return Boolean.TRUE;
    }

    default List<AbonnementQueue> getAbonnementQueues() {
        return Collections.emptyList();
    }

    default void sendMessage(AbonnementQueue abonnementQueue, String testMessage) {
    }
}

