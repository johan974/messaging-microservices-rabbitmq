package com.github.pedroluiznogueira.microservices.messaging.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MessageForQueue {
    private String routingKey;
    private User message;
}
