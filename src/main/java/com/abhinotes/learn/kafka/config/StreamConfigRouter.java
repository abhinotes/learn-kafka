package com.abhinotes.learn.kafka.config;

import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamConfigRouter {

    @Autowired
    StreamsConfigDefault streamsConfigDefault;

    @Bean("paymentRouterApplication")
    public StreamsBuilderFactoryBean routerStreamBuilderFactoryBean() {
        Map<String, Object> config = new HashMap<>();
        streamsConfigDefault.setDefaults(config);
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "paymentRouter");
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 30000);
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
    }

}
