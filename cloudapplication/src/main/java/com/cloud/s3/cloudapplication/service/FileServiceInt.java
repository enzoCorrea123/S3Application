package com.cloud.s3.cloudapplication.service;

import com.cloud.s3.cloudapplication.dto.FileRequestPostDTO;
import com.cloud.s3.cloudapplication.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileServiceInt {
    File cadastrar(MultipartFile multipartFile, Integer id);
    String getFile(Integer idTask, Integer idFile);
}
