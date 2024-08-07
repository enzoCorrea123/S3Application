package com.cloud.s3.cloudapplication.service;

import com.cloud.s3.cloudapplication.dto.TaskRequestPostDTO;
import com.cloud.s3.cloudapplication.model.Task;
import com.cloud.s3.cloudapplication.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements TaskServiceInt{
    TaskRepository repository;
    @Override
    public Task cadastrar(TaskRequestPostDTO dto) {
        Task task = new Task();
        task.setTitulo(dto.titulo());
        return repository.save(task);
    }

    @Override
    public Task getTask(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public String deleteTask(Integer id) {
        Task task = repository.findById(id).get();
        repository.delete(task);
        return "Task deleted";
    }
}
