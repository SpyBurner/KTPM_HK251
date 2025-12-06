package com.its.media.service;

import com.its.media.dto.request.MediaFileUploadRequest;
import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.MediaFileDetailResponse;
import com.its.media.dto.response.PreSignedUrlResponse;
import com.its.media.entity.File;
import com.its.media.exception.AppException;
import com.its.media.exception.ErrorCode;
import com.its.media.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final FileRepository fileRepository;
    private final ICloudStorageService cloudStorageService;

    @Override
    public PreSignedUrlResponse generatePresignedUrl(PreSignedUrlRequest request) {
        try {
            log.info("Generating presigned URL for file: {}", request.getFileName());
            PreSignedUrlResponse response = cloudStorageService.generatePresignedUrl(request);
            log.info("Successfully generated presigned URL for file: {}", request.getFileName());
            return response;
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for file: {}", request.getFileName(), e);
            throw new AppException(ErrorCode.PRESIGNED_URL_GENERATION_FAILED);
        }
    }

    @Transactional
    @Override
    public Void uploadMediaFile(MediaFileUploadRequest request, String username) {
        try {
            log.info("Starting file upload for user: {}, file: {}", username, request.getName());

            if (request.isProfilePicture()) {
                deleteOldProfilePicture(username);
            }

            File newFile = File.builder()
                    .name(request.getName())
                    .bucketId(request.getPublicUrl())
                    .username(request.isProfilePicture() ? username : null)
                    .sectionId(request.isProfilePicture() ? null : request.getSectionId())
                    .profilePicture(request.isProfilePicture())
                    .build();

            fileRepository.save(newFile);
            log.info("Successfully uploaded file: {} for user: {}", request.getName(), username);
            return null;
        } catch (Exception e) {
            log.error("Failed to upload file: {} for user: {}", request.getName(), username, e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private void deleteOldProfilePicture(String username) {
        try {
            log.info("Deleting old profile picture for user: {}", username);
            File oldProfile = fileRepository.findByUsernameAndProfilePictureTrue(username);

            if (oldProfile == null) {
                log.info("No old profile picture found for user: {}", username);
                return;
            }

            cloudStorageService.deleteFile(oldProfile.getBucketId());
            fileRepository.delete(oldProfile);
            log.info("Successfully deleted old profile picture for user: {}", username);
        } catch (Exception e) {
            log.error("Failed to delete old profile picture for user: {}", username, e);
            throw new AppException(ErrorCode.FILE_DELETION_FAILED);
        }
    }

    @Override
    public Void deleteMediaFile(String fileId) {
        try {
            log.info("Deleting file with ID: {}", fileId);
            UUID uuid = UUID.fromString(fileId);

            File file = fileRepository.findById(uuid).orElse(null);
            if (file == null) {
                log.warn("File not found with ID: {}", fileId);
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            cloudStorageService.deleteFile(file.getBucketId());
            fileRepository.delete(file);
            log.info("Successfully deleted file with ID: {}", fileId);
            return null;
        } catch (IllegalArgumentException e) {
            log.error("Invalid file ID format: {}", fileId, e);
            throw new AppException(ErrorCode.INVALID_FILE_ID);
        } catch (AppException e) {
            throw e; // Re-throw AppException as-is
        } catch (Exception e) {
            log.error("Failed to delete file with ID: {}", fileId, e);
            throw new AppException(ErrorCode.FILE_DELETION_FAILED);
        }
    }

    @Override
    public MediaFileDetailResponse getUserProfilePicture(String username) {
        try {
            log.info("Getting profile picture for user: {}", username);
            File file = fileRepository.findByUsernameAndProfilePictureTrue(username);

            if (file == null) {
                log.warn("Profile picture not found for user: {}", username);
                throw new AppException(ErrorCode.PROFILE_PICTURE_NOT_FOUND);
            }

            MediaFileDetailResponse response = MediaFileDetailResponse.builder()
                    .id(file.getId())
                    .name(file.getName())
                    .bucketId(file.getBucketId())
                    .username(username)
                    .build();

            log.info("Successfully retrieved profile picture for user: {}", username);
            return response;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get profile picture for user: {}", username, e);
            throw new AppException(ErrorCode.FILE_RETRIEVAL_FAILED);
        }
    }
}