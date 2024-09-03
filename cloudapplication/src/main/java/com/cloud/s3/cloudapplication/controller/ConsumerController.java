package com.cloud.s3.cloudapplication.controller;

import com.cloud.s3.cloudapplication.dto.ConsumerGetDTO;
import com.cloud.s3.cloudapplication.service.ConsumerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.* ;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/consumer")
@CrossOrigin("*")
public class ConsumerController {
    private final ConsumerService service;
    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMessages() {
        List<String> messages = service.getMessages();
        return ResponseEntity.ok(messages);
    }

}
