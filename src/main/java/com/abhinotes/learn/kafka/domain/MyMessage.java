package com.abhinotes.learn.kafka.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyMessage {

    private String messageKey;
    private String message;
}
