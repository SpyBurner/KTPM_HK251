package com.its.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private int code = 1000;

    private String message;
    private T result;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().result(data).build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder().result(data).message(message).build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder().code(9999).message(message).build();
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder().code(9999).message(message).result(null).build();
    }
}