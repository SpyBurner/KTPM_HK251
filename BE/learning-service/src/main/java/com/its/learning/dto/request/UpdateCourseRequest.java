package com.its.learning.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    @Size(min = 3, max = 255, message = "TITLE_INVALID")
    private String title;

    private String description;

    private Boolean active;
}
