package com.its.course.mapper;

import com.its.course.dto.response.ChapterDto;
import com.its.course.dto.response.SectionDto;
import com.its.course.entity.Chapter;
import com.its.course.entity.Section;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SectionMapper {

    public SectionDto sectionDto(Section ch) {
        if (ch == null) return null;
        SectionDto dto = new SectionDto();
        dto.setId(ch.getId());
        dto.setText(ch.getText());
        dto.setName(ch.getName());
        dto.setOrderIndex(ch.getOrderIndex());
        return dto;
    }
}

