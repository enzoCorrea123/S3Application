package com.cloud.s3.cloudapplication.repository;

import com.cloud.s3.cloudapplication.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
