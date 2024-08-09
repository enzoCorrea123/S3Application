package com.cloud.s3.cloudapplication.repository;

import com.cloud.s3.cloudapplication.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findAllByTask_IdTask (Integer idTask);
}
