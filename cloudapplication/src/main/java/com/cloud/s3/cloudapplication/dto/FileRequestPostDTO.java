package com.cloud.s3.cloudapplication.dto;

import com.cloud.s3.cloudapplication.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class FileRequestPostDTO {
    private Integer idFile;
    private Integer idTask;
    private MultipartFile file;
}
