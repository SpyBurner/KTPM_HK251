package com.its.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {
    private Long id;
    private String name;
    private String text;
    private Integer orderIndex;

    // IDs of files from file-service
    private Set<String> resourceIds;
}
