package com.example.DemoMockKafka.web.controller;

import com.example.DemoMockKafka.model.KafkaMessage;
import com.example.DemoMockKafka.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class KafkaController {

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private final KafkaMessageService kafkaMessageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody KafkaMessage kafkaMessage) {
        kafkaTemplate.send(topicName, kafkaMessage);

        return ResponseEntity.ok("Message send to Kafka");
    }

    @GetMapping("/{id}")
    public ResponseEntity<KafkaMessage> getById(@PathVariable Long id) {
        if (kafkaMessageService.getById(id).isEmpty())
            return ResponseEntity.notFound().build();
        else {
            return ResponseEntity.ok(kafkaMessageService.getById(id).orElseThrow());
        }
    }

    @GetMapping
    public ResponseEntity<List<KafkaMessage>> getAll() {
        return ResponseEntity.ok(kafkaMessageService.getAll());
    }
}
