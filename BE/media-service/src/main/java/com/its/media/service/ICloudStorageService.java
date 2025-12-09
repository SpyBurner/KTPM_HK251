package com.its.media.service;

import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.PreSignedUrlResponse;

public interface ICloudStorageService {
    PreSignedUrlResponse generatePresignedUrl(PreSignedUrlRequest request);

    void deleteFile(String fileKey);

    String buildPublicUrl(String fileKey);

    String resolveFolder(String fileType);
}