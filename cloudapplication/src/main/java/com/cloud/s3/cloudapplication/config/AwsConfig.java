package com.cloud.s3.cloudapplication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Configuration
@Component
@Getter
@PropertySource("classpath:aws.properties")
public class AwsConfig {
    @Value("${aws.access.key}")
    public String awsKeyID;
    @Value("${aws.secret.key}")
    public String awsSecretKeyID;
    @Value("${aws.bucket.name}")
    public String awsBuketName;

}
