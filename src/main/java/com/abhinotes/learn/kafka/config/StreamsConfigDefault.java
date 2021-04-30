package com.abhinotes.learn.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamsConfigDefault {

    private final String brokerHosts;
    private final String appid;
    private final String streamThreads;
    private final String replicationFactor;

    public StreamsConfigDefault(@Value("${bootstrap-servers}") String brokerHosts,
                                @Value("${app.id}") String appid,
                                @Value("${app.stream.thread}") String streamThreads,
                                @Value("${app.stream.replication-factor}") String replicationFactor) {
        this.brokerHosts = brokerHosts;
        this.appid = appid;
        this.streamThreads = streamThreads;
        this.replicationFactor = replicationFactor;
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, appid);
        setDefaults(config);
        return new StreamsConfig(config);
    }

    public void setDefaults(Map<String, Object> config) {
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerHosts);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, FailOnInvalidTimestamp.class);
        //config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, streamThreads);
        config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.abhinotes.learn.kafka.domain");
        //Consumer Configs
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    }

}