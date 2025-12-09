package com.its.media.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Cấu hình cho AWS S3
 * Chỉ được load khi cloud.storage.provider=s3
 */
@Configuration
@ConditionalOnProperty(
        name = "cloud.storage.provider",
        havingValue = "s3",
        matchIfMissing = true
)
@Slf4j
public class S3Config implements CloudStorageConfig {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${cloud.storage.provider:s3}")
    private String provider;

    @Override
    public void configure() {
        log.info("Configuring AWS S3 storage with region: {}", region);
    }

    @Override
    public boolean isEnabled() {
        return "s3".equalsIgnoreCase(provider);
    }

    @Override
    public String getProviderName() {
        return "AWS S3";
    }

    private StaticCredentialsProvider credentialProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    @Bean
    public S3Presigner s3Presigner() {
        log.info("Creating S3Presigner bean for region: {}", region);
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialProvider())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        log.info("Creating S3Client bean for region: {}", region);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialProvider())
                .build();
    }
}