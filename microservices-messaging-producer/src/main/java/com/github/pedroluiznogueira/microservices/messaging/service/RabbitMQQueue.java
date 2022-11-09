package com.github.pedroluiznogueira.microservices.messaging.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RabbitMQQueue {
    private String name;
    private boolean durable;
}
