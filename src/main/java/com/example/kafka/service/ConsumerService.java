package com.example.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @KafkaListener(topics = "demo_topic", groupId = "demo_group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}
