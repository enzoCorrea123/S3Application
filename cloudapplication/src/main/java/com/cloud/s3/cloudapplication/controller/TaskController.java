package com.cloud.s3.cloudapplication.controller;

import com.cloud.s3.cloudapplication.dto.TaskRequestPostDTO;
import com.cloud.s3.cloudapplication.model.Task;
import com.cloud.s3.cloudapplication.service.TaskService;
import com.cloud.s3.cloudapplication.service.TaskServiceInt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private TaskService service;
    @PostMapping
    public ResponseEntity<TaskRequestPostDTO> taskPost(@RequestBody TaskRequestPostDTO dto){
        service.cadastrar(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> taskGet(@PathVariable Integer id){
        Task task = service.getTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);

    }
}
