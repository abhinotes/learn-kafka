package com.abhinotes.learn.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamConfigCountBySource {

    @Autowired
    StreamsConfigDefault streamsConfigDefault;

    @Bean("countBySourceApplication")
    public StreamsBuilderFactoryBean routerStreamBuilderFactoryBean() {
        Map<String, Object> config = new HashMap<>();
        streamsConfigDefault.setDefaults(config);
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "countBySource");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "countBySource-Consumer");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5000);
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
    }

}
