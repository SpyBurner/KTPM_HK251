package com.its.course.mapper;

import com.its.course.dto.response.ChapterDto;
import com.its.course.dto.response.SectionDto;
import com.its.course.entity.Chapter;
import com.its.course.entity.Section;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ContentMapper {

    public ChapterDto toChapterDto(Chapter ch) {
        if (ch == null) return null;
        ChapterDto dto = new ChapterDto();
        dto.setId(ch.getId());
        dto.setTitle(ch.getTitle());
        dto.setSummary(ch.getSummary());
        dto.setOrderIndex(ch.getOrderIndex());
        dto.setSections(ch.getSections().stream().map(this::toSectionDto).collect(Collectors.toList()));
        return dto;
    }

    public SectionDto toSectionDto(Section s) {
        if (s == null) return null;

        SectionDto dto = new SectionDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setText(s.getText());
        dto.setOrderIndex(s.getOrderIndex());

        dto.setResourceIds(s.getResourceIds());
        return dto;
    }

}

