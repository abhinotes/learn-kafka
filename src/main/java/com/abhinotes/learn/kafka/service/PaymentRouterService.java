package com.abhinotes.learn.kafka.service;

import com.abhinotes.learn.kafka.domain.PaymentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentRouterService {

    private static final CharSequence SPACE = " ";
    private final String requestTopic;
    private final String errorTopic;
    private final String routingBaseTopic;

    private static final List<String> VALID_SOURCES = new ArrayList<String>();

    /**
     * Just for easy reference , Actual implementation can be from database , property files etc
     */
    static {
        VALID_SOURCES.add("Mobile");
        VALID_SOURCES.add("Swipe");
        VALID_SOURCES.add("Web");
    }

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
       final KStream<String,PaymentWrapper> paymentReader = routeBuilder.stream(requestTopic);
        paymentReader.to((key, paymentWrapper, recordContext) -> getTopicNameToRoute(paymentWrapper));

        return paymentReader;
    }

    private String getTopicNameToRoute(PaymentWrapper paymentWrapper) {
     String toTopic =    routingBaseTopic.concat(paymentWrapper.getSource());

        if(VALID_SOURCES.contains(paymentWrapper.getSource())) {
                log.info("Routing Payment with ref {} to route {} ",
                        paymentWrapper.getPayment().getTransactionId(), toTopic);
                return toTopic;
            }

        log.info("Invalid destination topic to route {} hence routing to Error Topic {}",
                toTopic, errorTopic);
        return errorTopic;
    }

}
