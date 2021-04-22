package com.abhinotes.learn.kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestMessage {
    private String key;
    private PaymentWrapper paymentWrapper;
}
