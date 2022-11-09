package com.github.pedroluiznogueira.microservices.messaging.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueuedRequestListener {

    private static final Logger log = LoggerFactory.getLogger(QueuedRequestListener.class);

    public QueuedRequestListener() {
    }

    @Transactional
    @JmsListener(destination = "Johan")
    public void onMessage(Message message) throws JmsException {
        try {
            // var aanleveringId = message.getLongProperty("AanleveringId");
            log.info( "Message: {}", message);
        } catch (Exception e) {
            log.error("Fout bij het lezen van queue", e);
            throw new RuntimeException(e);
        }
    }

    private void error(final long aanleveringId) throws NotFoundException {
        log.error("Mutatiebestand voor aanlevering met id {} niet gevonden", aanleveringId);
        throw new NotFoundException("Mutatiebestand voor aanlevering met id {} niet gevonden");
    }
}
