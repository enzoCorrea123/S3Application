package com.cloud.s3.cloudapplication.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.s3.cloudapplication.config.AwsConfig;
import com.cloud.s3.cloudapplication.config.KafkaProducerConfig;
import com.cloud.s3.cloudapplication.dto.FileRequestGetDTO;
import com.cloud.s3.cloudapplication.dto.FileRequestPostDTO;
import com.cloud.s3.cloudapplication.model.File;
import com.cloud.s3.cloudapplication.model.Task;
import com.cloud.s3.cloudapplication.repository.FileRepository;
import com.cloud.s3.cloudapplication.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FileService implements FileServiceInt {
    FileRepository repository;
    TaskRepository taskRepository;
    TaskService taskService;
    AwsConfig config;
    KafkaProducerConfig kafkaProducerConfig;
    private KafkaTemplate<String, String> kafkaTemplate;
    @KafkaListener(topics = "topic_enzo2", groupId = "my-group")
    public void listenGroupMyGroup(String message) {
        System.out.println("Mensagem recebida no grupo my-group: " + message);
    }

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("topic_enzo2", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Mensagem enviada=[" + message +
                        "] com offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Mensagem não enviada=[" +
                        message + "] por casa do : " + ex.getMessage());
            }
        });
    }


    @Override
    public File cadastrar(MultipartFile multipartFile, Integer id) {
        File file;
        try {
            String key = UUID.randomUUID().toString();
            AmazonS3 amazonS3 = getAmazonS3();
            if (amazonS3.doesBucketExistV2(config.getAwsBuketName())) {
                file = new File();
                Task task = taskService.getTask(id);
                file.setRef(key);
                file.setData(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                file.setTask(task);

                java.io.File arquivoJava = java.io.File.createTempFile("tmp", multipartFile.getOriginalFilename());
                multipartFile.transferTo(arquivoJava);

                amazonS3.putObject(config.getAwsBuketName(), key, arquivoJava);

                List<File> tempFiles = task.getFiles();
                file = repository.save(file);
                tempFiles.add(file);
                task.setFiles(tempFiles);
                taskRepository.save(task);
                //kafka producer
                sendMessage("Arquivo " + file.getIdFile() + " adicionado na task " + task.getIdTask());
                return file;
            } else {
                throw new RuntimeException("Não entrou no if");
            }

        } catch (S3Exception e) {
            throw new RuntimeException("Erro no S3: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro no código: " + e.getMessage());
        }
    }

    @Override
    public String getFile(Integer idTask, Integer idFile) {
        AmazonS3 amazonS3 = getAmazonS3();
        List<S3ObjectSummary> objects = getListObjects();

        File fileTmp = repository.findById(idFile).get();
        File file = taskRepository.findById(idTask).get().getFiles().get(fileTmp.getIdFile()-1);

        String urlImage = null;
        for (S3ObjectSummary os : objects) {
            if (file.getRef().equals(os.getKey())) {
                urlImage =  os.getKey();
            }
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(config.getAwsBuketName(), urlImage);
        generatePresignedUrlRequest.withMethod(HttpMethod.GET).withExpiration(new Date(System.currentTimeMillis() + 3600000));

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public List<FileRequestGetDTO> getAllFiles(Integer idTask) {
        AmazonS3 amazonS3 = getAmazonS3();
        List<S3ObjectSummary> objects = getListObjects();
        List<String> urls = new LinkedList<>();
        List<File> files = repository.findAllByTask_IdTask(idTask);

        GeneratePresignedUrlRequest generatePresignedUrlRequest;
        List<Integer> ids = new ArrayList<>();
        for (File file : files) {
            for (S3ObjectSummary os : objects) {
                if (file.getRef().equals(os.getKey())) {
                    generatePresignedUrlRequest = new GeneratePresignedUrlRequest(config.getAwsBuketName(), os.getKey());
                    generatePresignedUrlRequest.withMethod(HttpMethod.GET).withExpiration(new Date(System.currentTimeMillis() + 3600000));
                    urls.add(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString());
                }
            }
            ids.add(file.getIdFile());
        }

        List<FileRequestGetDTO> fileRequestGetDTOS = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            fileRequestGetDTOS.add(new FileRequestGetDTO(ids.get(i), urls.get(i)));
        }
        //kafka consumer
        listenGroupMyGroup("Arquivos da task " + idTask + " foram listados");
        return fileRequestGetDTOS;
    }

    @Override
    public String deleteFile(Integer idFile) {
        File file = repository.findById(idFile).get();
        List<S3ObjectSummary> objects = getListObjects();
        System.out.println(file);
        for(S3ObjectSummary object : objects){
            if(object.getKey().equals(file.getRef())){
                AmazonS3 amazonS3 = getAmazonS3();
                amazonS3.deleteObject(config.getAwsBuketName(), file.getRef());
                repository.delete(file);
                return "Arquivo deletado com sucesso";
            }
        }
        throw new RuntimeException("Arquivo não encontrado");
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
