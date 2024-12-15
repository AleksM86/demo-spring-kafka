package com.example.DemoMockKafka.model;

import lombok.Data;

@Data
public class KafkaMessage {
    private Long id;
    private String message;
}
