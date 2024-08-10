package com.cloud.s3.cloudapplication.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.s3.cloudapplication.config.AwsConfig;
import com.cloud.s3.cloudapplication.dto.TaskRequestPostDTO;
import com.cloud.s3.cloudapplication.model.File;
import com.cloud.s3.cloudapplication.model.Task;
import com.cloud.s3.cloudapplication.repository.FileRepository;
import com.cloud.s3.cloudapplication.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements TaskServiceInt{
    TaskRepository repository;
    AwsConfig config;
    @Override
    public Task cadastrar(TaskRequestPostDTO dto) {
        Task task = new Task();
        task.setTitulo(dto.titulo());
        task.setFiles(new ArrayList<>());
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
        List<File> files = task.getFiles();
        List<S3ObjectSummary> objects = getListObjects();

        for (File file : files) {
            for(S3ObjectSummary object : objects){
                if(object.getKey().equals(file.getRef())){
                    AmazonS3 amazonS3 = getAmazonS3();
                    amazonS3.deleteObject(config.getAwsBuketName(), file.getRef());
                }
            }
        }

        repository.delete(task);
        return "Task deleted";
    }
    private AmazonS3 getAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(config.getAwsKeyID(), config.getAwsSecretKeyID());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();
    }
    private List<S3ObjectSummary> getListObjects() {
        AmazonS3 amazonS3 = getAmazonS3();
        ListObjectsV2Result result = amazonS3.listObjectsV2(config.getAwsBuketName());
        return result.getObjectSummaries();
    }
}
