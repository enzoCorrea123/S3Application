package com.cloud.s3.cloudapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public record TaskRequestPostDTO(String titulo, MultipartFile file){
}
