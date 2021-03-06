package com.abhinotes.learn.kafka.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String transactionId;
    private String creditParty;
    private String debitParty;
    private String clearing;
    private String ccy;
    private Long amount;
}
