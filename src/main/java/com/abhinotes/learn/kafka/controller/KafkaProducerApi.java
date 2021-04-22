package com.abhinotes.learn.kafka.controller;

import com.abhinotes.learn.kafka.client.KafkaMessageProducer;
import com.abhinotes.learn.kafka.domain.RestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerApi {

    private final KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    public KafkaProducerApi(KafkaMessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @PostMapping("kafka/post/{sendToTopic}")
    String postMessage( @RequestBody RestMessage restMessage, @PathVariable String sendToTopic) {
        return kafkaMessageProducer.produce(restMessage,sendToTopic);
    }

}
