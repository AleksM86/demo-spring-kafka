package com.example.DemoMockKafka.service;

import com.example.DemoMockKafka.model.KafkaMessage;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KafkaMessageService {

    private final List<KafkaMessage> messages = new ArrayList<>();

    public void add(KafkaMessage kafkaMessage){
        messages.add(kafkaMessage);
    }

    public Optional<KafkaMessage> getById(Long id){
        return messages.stream().filter(it -> it.getId().equals(id)).findFirst();
    }

    public List<KafkaMessage> getAll(){
        return messages;
    }
}
