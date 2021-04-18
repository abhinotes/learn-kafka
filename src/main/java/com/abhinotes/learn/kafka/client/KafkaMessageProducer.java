package com.abhinotes.learn.kafka.client;

import com.abhinotes.learn.kafka.domain.MyMessage;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class KafkaMessageProducer {

    private final KafkaTemplate<String, MyMessage> kafkaTemplate;

    @Autowired
    public KafkaMessageProducer(KafkaTemplate<String, MyMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String produce(MyMessage myMessage, String sendToTopic) {

        ListenableFuture<SendResult<String, MyMessage>> future =
                kafkaTemplate.send(sendToTopic, myMessage.getMessageKey(), myMessage);

        future.addCallback(new KafkaSendCallback<String, MyMessage>() {
            @Override
            public void onSuccess(SendResult<String, MyMessage> result) {
                log.info("Success!! \n Message Key {}  \n Timestamp : {} " +
                                "\n Topic : {} \n Offset : {} \n Partition : {} \n" ,
                        result.getProducerRecord().key(),
                        result.getRecordMetadata().timestamp(),
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
            }
            @Override
            public void onFailure(KafkaProducerException ex) {
                ProducerRecord<String, MyMessage> failed = ex.getFailedProducerRecord();
                log.error("Error while publishing record : {}" , failed.toString() , ex);
            }
        });

        return "Execution Success!!";
    }


}
