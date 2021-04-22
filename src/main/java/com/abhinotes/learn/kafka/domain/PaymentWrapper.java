package com.abhinotes.learn.kafka.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWrapper {

    private String source;
    private String parentTransactionId;
    private Payment payment;
}
