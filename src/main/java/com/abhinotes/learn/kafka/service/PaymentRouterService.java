package com.abhinotes.learn.kafka.service;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class PaymentRouterService {

    private static final CharSequence SPACE = " ";
    private final String requestTopic;
    private final String errorTopic;
    private final String routingBaseTopic;

    @Autowired
    public PaymentRouterService(@Value("${app.stream.router.request-topic}") String requestTopic,
                                @Value("${app.stream.router.error-topic}") String errorTopic,
                                @Value("${app.stream.router.routing-base-topic}") String routingBaseTopic) {
        this.requestTopic = requestTopic;
        this.errorTopic = errorTopic;
        this.routingBaseTopic = routingBaseTopic;
    }

    @Bean("paymentRouterStreamTopology")
    public KStream<String, PaymentWrapper> startRoutingProcessing(@Qualifier("paymentRouterApplication") StreamsBuilder routeBuilder) {

//        final KStream<String, PaymentWrapper> toSquare = builder.stream( requestTopic, Consumed.with(Serdes.String(), ));
//        toSquare.map((key, value) -> { // do something with each msg, square the values in our case
//            return KeyValue.pair(key, value * value);
//        }).to("squared", Produced.with(Serdes.String(), Serdes.Long())); // send downstream to another topic

        final KStream<String,PaymentWrapper> paymentReader = routeBuilder.stream(requestTopic);
        paymentReader.to((key, paymentWrapper, recordContext) -> getTopicNameToRoute(paymentWrapper));

        return paymentReader;
    }

    private String getTopicNameToRoute(PaymentWrapper paymentWrapper) {

    if(paymentWrapper == null || paymentWrapper.getSource() == null ||
            paymentWrapper.getSource().contains(SPACE)) {
        return errorTopic;
    }
        // You can query database or hit cache to find target Topic basis source identifier.
        return routingBaseTopic.concat(paymentWrapper.getSource());
    }

}
