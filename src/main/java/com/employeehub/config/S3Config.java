package com.employeehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    // Credentials resolved via the default provider chain — the EC2
    // instance role (aws-elasticbeanstalk-ec2-role) in deployed
    // environments, or the developer's local AWS CLI profile locally. No
    // access keys are configured in the app itself.
    private static final Region REGION = Region.US_EAST_2;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(REGION).build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder().region(REGION).build();
    }
}
