package com.its.iam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    // Authentication errors
    USER_NOT_FOUND(1002, "User does not exist", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED(1003, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1004, "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1005, "Invalid username or password", HttpStatus.UNAUTHORIZED),

    // Token errors
    INVALID_TOKEN(1006, "Token is invalid", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(1007, "Token has expired", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN(1008, "Missing or invalid Authorization header", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_BLACKLISTED(1009, "Token is already invalidated", HttpStatus.BAD_REQUEST),

    // Authorization errors
    UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),

    // Validation errors - Username
    USERNAME_REQUIRED(1011, "Username is required", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1012, "Username must be between {min} and {max} characters", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID_FORMAT(1013, "Username can only contain letters, numbers and underscore", HttpStatus.BAD_REQUEST),

    // Validation errors - Password
    PASSWORD_REQUIRED(1014, "Password is required", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1015, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),

    // Validation errors - Email
    EMAIL_REQUIRED(1016, "Email is required", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1017, "Email format is invalid", HttpStatus.BAD_REQUEST),

    // Validation errors - Other fields
    TOKEN_REQUIRED(1018, "Token is required", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1019, "Phone number must be 10-11 digits", HttpStatus.BAD_REQUEST),
    DISPLAY_NAME_INVALID(1020, "Display name must not exceed {max} characters", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1021, "Role does not exist", HttpStatus.NOT_FOUND),

    // Account status errors
    ACCOUNT_LOCKED(1022, "Account is locked until {penaltyTime}", HttpStatus.FORBIDDEN),
    ACCOUNT_INACTIVE(1023, "Account is inactive", HttpStatus.FORBIDDEN);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}