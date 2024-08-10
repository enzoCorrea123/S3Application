package com.cloud.s3.cloudapplication.controller;

import com.cloud.s3.cloudapplication.dto.FileRequestPostDTO;
import com.cloud.s3.cloudapplication.model.File;
import com.cloud.s3.cloudapplication.service.FileService;
import com.cloud.s3.cloudapplication.service.FileServiceInt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
@CrossOrigin("*")
public class FileController {
    private final FileService service;
    @PostMapping("/{id}")
    public ResponseEntity<File> postMethod(@RequestBody MultipartFile multipartFile, @PathVariable Integer id){
        File file = service.cadastrar(multipartFile, id);
        return ResponseEntity.ok(file);
    }
    @GetMapping("/{idTask}/{idFile}")
    public ResponseEntity<String> getMethod(@PathVariable Integer idTask, @PathVariable Integer idFile){
        return ResponseEntity.ok(service.getFile(idTask, idFile));
    }
    @GetMapping("/{idTask}")
    public ResponseEntity<List<String>> getAllFiles(@PathVariable Integer idTask){
        return ResponseEntity.ok(service.getAllFiles(idTask));
    }
    @DeleteMapping("/{idFile}")
    public ResponseEntity<String> deleteMethod(@PathVariable Integer idFile){
        try{
            return new ResponseEntity<>(service.deleteFile(idFile), HttpStatus.NO_CONTENT);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
