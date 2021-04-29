package com.abhinotes.learn.kafka.config;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyConsumerConfig {

    private final String brokerHosts;

    public MyConsumerConfig(@Value("${bootstrap-servers}") String brokerHosts) {
        this.brokerHosts = brokerHosts;
    }



    @Bean
    public ConsumerFactory<String, PaymentWrapper> consumerFactory(){
        JsonDeserializer<PaymentWrapper> deserializer = new JsonDeserializer<>(PaymentWrapper.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerHosts);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_one");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "500");


        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentWrapper> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, PaymentWrapper> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }

}
