package com.abhinotes.learn.kafka.service;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentSourceCountService {

    private final String requestTopic;
    private final String countTopic;

    @Autowired
    public PaymentSourceCountService(@Value("${app.stream.router.request-topic}") String requestTopic,
                                @Value("${app.stream.count-topic}") String countTopic) {
        this.requestTopic = requestTopic;
        this.countTopic = countTopic;
    }


    @Bean("PaymentSourceCountTopology")
    public KStream<String, PaymentWrapper> startRoutingProcessing(@Qualifier("countBySourceApplication") StreamsBuilder routeBuilder) {
        final KStream<String,PaymentWrapper> paymentReader = routeBuilder.stream(requestTopic);

        // Do Aggregation
        paymentReader.map((key,value) -> new KeyValue<>(value.getSource(), value))
                .groupByKey()
                .count()
                .toStream()
                .to(countTopic, Produced.with(Serdes.String(), Serdes.Long()));

        return paymentReader;
    }

}
