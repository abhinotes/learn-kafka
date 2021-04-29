package com.abhinotes.learn.kafka.client;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaMessageConsumer {

    @KafkaListener(
            topicPartitions = { @TopicPartition(topic = "payment-request-topic", partitions = { "0" })},
            groupId = "consumerGroup1ForPaymentWrapper",
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "true")
    public void consume(@Payload List<PaymentWrapper> paymentWrapperList){
        log.info("\n/Consume payment-request-topic Partition ---->>>>>>/\n"+paymentWrapperList);
    }
}
