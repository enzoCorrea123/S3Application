package com.cloud.s3.cloudapplication.service;

import com.cloud.s3.cloudapplication.dto.ConsumerGetDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerService {

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "topic_enzo2", groupId = "my-group")
    public void listenGroupMyGroup(String message) {
        synchronized (messages) {
            messages.add(message);
        }
        System.out.println("Mensagem recebida no grupo my-group: " + message);
    }

    public List<String> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

}
