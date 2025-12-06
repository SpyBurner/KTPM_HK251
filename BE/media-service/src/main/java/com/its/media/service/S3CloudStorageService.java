package com.its.media.service;

import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "cloud.storage.provider", havingValue = "s3", matchIfMissing = true)
public class S3CloudStorageService implements ICloudStorageService {

    private final S3Presigner presigner;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Override
    public PreSignedUrlResponse generatePresignedUrl(PreSignedUrlRequest request) {
        String folder = resolveFolder(request.getFileType());
        String key = folder + request.getFileName();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(
                p -> p.signatureDuration(Duration.ofMinutes(15))
                        .putObjectRequest(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .contentType(request.getFileType())
                                .build()
                        )
        );

        return PreSignedUrlResponse.builder()
                .objectKey(key)
                .endpoint(presignedRequest.url().toString())
                .publicUrl(buildPublicUrl(key))
                .build();
    }

    @Override
    public void deleteFile(String fileKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
            log.info("Deleted S3 file: {}", fileKey);
        } catch (Exception e) {
            log.error("Failed to delete S3 file: {}", fileKey, e);
            throw e;
        }
    }

    @Override
    public String buildPublicUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    @Override
    public String resolveFolder(String fileType) {
        fileType = fileType.toLowerCase();

        if (fileType.startsWith("image/")) return "images/";
        if (fileType.startsWith("video/")) return "videos/";
        return "others/";
    }
}