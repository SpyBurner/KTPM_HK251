package com.its.learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotBlank(message = "TITLE_REQUIRED")
    @Size(min = 3, max = 255, message = "TITLE_INVALID")
    private String title;

    private String description;

    private Long instructorId;

    private Boolean active = true;
}
