package com.its.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Base DTO class for common fields
 */
@Data
public abstract class BaseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}