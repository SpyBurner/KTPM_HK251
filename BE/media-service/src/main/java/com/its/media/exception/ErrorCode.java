package com.its.media.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    PRESIGNED_URL_GENERATION_FAILED(1002, "Failed to generate presigned URL", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_FAILED(1003, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETION_FAILED(1004, "File deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND(1005, "File not found", HttpStatus.NOT_FOUND),
    INVALID_FILE_ID(1006, "Invalid file ID", HttpStatus.BAD_REQUEST),
    PROFILE_PICTURE_NOT_FOUND(1007, "Profile picture not found", HttpStatus.NOT_FOUND),
    FILE_RETRIEVAL_FAILED(1008, "Failed to retrieve file", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}