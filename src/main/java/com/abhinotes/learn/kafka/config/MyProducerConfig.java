package com.abhinotes.learn.kafka.config;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import com.abhinotes.learn.kafka.domain.RestMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class MyProducerConfig {

    private final String brokerHosts;

    public MyProducerConfig(@Value("${bootstrap-servers}") String brokerHosts) {
        this.brokerHosts = brokerHosts;
    }

    @Bean
    public ProducerFactory<String, PaymentWrapper> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Bean to configure all Producer configurations Java way
     * All possible configurations are at following URL :
     * https://kafka.apache.org/documentation/#producerconfigs
     * @return
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerHosts);

        // Ack , Retries
        props.put(ProducerConfig.RETRIES_CONFIG , 3);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Idempotency check to avoid duplicate technical delivery due to
        // redelivery attempt
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        // Message Serializer configuration
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, PaymentWrapper> kafkaTemplate() {
        return new KafkaTemplate<String, PaymentWrapper>(producerFactory());
    }

}
