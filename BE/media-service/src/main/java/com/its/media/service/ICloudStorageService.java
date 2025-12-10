package com.its.media.service;

import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.PreSignedUrlResponse;

public interface ICloudStorageService {
    PreSignedUrlResponse generatePresignedUrl(PreSignedUrlRequest request);
    PreSignedUrlResponse generateDownloadPresignedUrl(String bucketId, String fileName);
    void deleteFile(String fileKey);
    String buildPublicUrl(String key);
    String resolveFolder(String fileType);
}