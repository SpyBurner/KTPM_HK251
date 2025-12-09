package com.its.media.controller;

import com.its.media.dto.request.MediaFileUploadRequest;
import com.its.media.dto.request.PreSignedUrlRequest;
import com.its.media.dto.response.ApiResponse;
import com.its.media.dto.response.MediaFileDetailResponse;
import com.its.media.dto.response.PreSignedUrlResponse;
import com.its.media.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final IFileService fileService;

    @GetMapping("/presigned-url")
    public ApiResponse<PreSignedUrlResponse> generatePresignedUrl(
            @RequestParam("filename") String fileName,
            @RequestParam("filetype") String fileType) {
        PreSignedUrlRequest request = PreSignedUrlRequest.builder()
                .fileName(fileName)
                .fileType(fileType)
                .build();
        return ApiResponse.<PreSignedUrlResponse>builder().result(fileService.generatePresignedUrl(request)).build();
    }

    @PostMapping
    public ApiResponse<Void> uploadMediaFile(
            @RequestBody MediaFileUploadRequest request,
            @RequestHeader("X-User-Id") String username
    ) {
        fileService.uploadMediaFile(request, username);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{filId}")
    public ApiResponse<Void> deleteMediaFile(
            @PathVariable("filId") String fileName) {
        return ApiResponse.<Void>builder().result(fileService.deleteMediaFile(fileName)).build();
    }

    @GetMapping("/{username}")
    public ApiResponse<MediaFileDetailResponse> getMediaFile(
            @PathVariable("username") String username) {
        return ApiResponse.<MediaFileDetailResponse>builder().result(fileService.getUserProfilePicture(username)).build();
    }
}