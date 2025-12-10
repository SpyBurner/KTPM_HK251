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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
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
    public PreSignedUrlResponse generateDownloadPresignedUrl(String publicUrl, String fileName) {
        try {
            // Extract S3 key từ public URL
            // Input: https://soft-architecture.s3.ap-southeast-1.amazonaws.com/videos/test.mp4
            // Output: videos/test.mp4
            String s3Key = extractKeyFromPublicUrl(publicUrl);

            log.info("Generating download presigned URL for file: {} with S3 key: {}", fileName, s3Key);

            // Tạo presigned GET request với S3 key
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                    builder -> builder
                            .signatureDuration(Duration.ofMinutes(15))
                            .getObjectRequest(GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(s3Key)  // Dùng S3 key đã extract: "videos/test.mp4"
                                    .build()
                            )
            );

            String presignedUrl = presignedRequest.url().toString();
            log.info("Successfully generated download presigned URL for file: {}", fileName);

            return PreSignedUrlResponse.builder()
                    .endpoint(presignedUrl)
                    .objectKey(s3Key)
                    .publicUrl(publicUrl)
                    .build();

        } catch (Exception e) {
            log.error("Failed to generate download presigned URL for file: {} with URL: {}", fileName, publicUrl, e);
            throw new RuntimeException("Failed to generate download presigned URL: " + e.getMessage(), e);
        }
    }

    /**
     * Extract S3 key từ public URL
     * @param publicUrl URL đầy đủ của file trong S3
     * @return S3 key (ví dụ: "videos/test.mp4")
     */
    private String extractKeyFromPublicUrl(String publicUrl) {
        try {
            // Format 1: https://bucket-name.s3.region.amazonaws.com/videos/test.mp4
            if (publicUrl.contains(".amazonaws.com/")) {
                String[] parts = publicUrl.split(".amazonaws.com/");
                if (parts.length > 1) {
                    return parts[1];
                }
            }

            // Format 2: https://s3.region.amazonaws.com/bucket-name/videos/test.mp4
            if (publicUrl.contains("s3.") && publicUrl.contains("/" + bucketName + "/")) {
                String[] parts = publicUrl.split("/" + bucketName + "/");
                if (parts.length > 1) {
                    return parts[1];
                }
            }

            // Nếu không match format nào, log warning và return nguyên
            log.warn("Could not extract S3 key from URL: {}, using as-is", publicUrl);
            return publicUrl;

        } catch (Exception e) {
            log.error("Error extracting key from URL: {}", publicUrl, e);
            throw new RuntimeException("Invalid S3 URL format: " + publicUrl, e);
        }
    }

    @Override
    public void deleteFile(String fileKey) {
        try {
            // Nếu fileKey là URL, extract key trước
            String s3Key = fileKey.contains("amazonaws.com")
                    ? extractKeyFromPublicUrl(fileKey)
                    : fileKey;

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build());
            log.info("Successfully deleted S3 file: {}", s3Key);
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