package com.its.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {
    private Long id;
    private String title;
    private String summary;
    private Integer orderIndex;
    private List<SectionDto> sections;
}
