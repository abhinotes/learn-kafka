package com.abhinotes.learn.kafka.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestMessage {
    private String key;
    private PaymentWrapper paymentWrapper;
}
