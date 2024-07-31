package com.cloud.s3.cloudapplication.service;

import com.cloud.s3.cloudapplication.dto.TaskRequestPostDTO;
import com.cloud.s3.cloudapplication.model.Task;

public interface TaskServiceInt {
    Task cadastrar(TaskRequestPostDTO dto);

    Task getTask(Integer id);
}
