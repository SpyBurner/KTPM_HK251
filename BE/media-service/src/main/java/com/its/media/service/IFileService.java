package com.its.media.service;

import com.its.media.dto.request.MediaFileUploadRequest;
import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.MediaFileDetailResponse;
import com.its.media.dto.response.PreSignedUrlResponse;


public interface IFileService {
    PreSignedUrlResponse generatePresignedUrl(PreSignedUrlRequest request);
    Void uploadMediaFile(MediaFileUploadRequest request, String username);
    Void deleteMediaFile(String fileName);
    MediaFileDetailResponse getUserProfilePicture(String username);
}
