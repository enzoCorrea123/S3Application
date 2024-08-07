package com.cloud.s3.cloudapplication.controller;

import com.cloud.s3.cloudapplication.dto.TaskRequestPostDTO;
import com.cloud.s3.cloudapplication.model.Task;
import com.cloud.s3.cloudapplication.service.TaskService;
import com.cloud.s3.cloudapplication.service.TaskServiceInt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
@CrossOrigin("*")
public class TaskController {
    private TaskService service;
    @PostMapping
    public ResponseEntity<Task> taskPost(@RequestBody TaskRequestPostDTO dto){
        return new ResponseEntity<>(service.cadastrar(dto), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> taskGet(@PathVariable Integer id){
        Task task = service.getTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<List<Task>> taskGet(){
        return new ResponseEntity<>(service.getAllTasks(), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> taskDelete(@PathVariable Integer id){
        service.deleteTask(id);
        return new ResponseEntity<>("Task deleted", HttpStatus.NO_CONTENT);
    }
}
