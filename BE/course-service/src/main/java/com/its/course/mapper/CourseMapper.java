package com.its.course.mapper;

import com.its.course.dto.request.CreateCourseRequest;
import com.its.course.dto.request.UpdateCourseRequest;
import com.its.course.dto.response.CourseDto;
import com.its.course.entity.Course;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private final ContentMapper contentMapper;

    public CourseMapper(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    public Course toEntity(CreateCourseRequest req) {
        if (req == null) return null;
        Course c = new Course();
        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        c.setInstructorId(req.getInstructorId());
        c.setActive(req.getActive() == null || req.getActive());
        c.setCreatedAt(LocalDateTime.now());
        return c;
    }

    public void updateEntity(UpdateCourseRequest req, Course entity) {
        if (req == null || entity == null) return;
        if (req.getTitle() != null) entity.setTitle(req.getTitle());
        if (req.getDescription() != null) entity.setDescription(req.getDescription());
        if (req.getActive() != null) entity.setActive(req.getActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    public CourseDto toDto(Course entity) {
        if (entity == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setInstructorId(entity.getInstructorId());
        dto.setActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setChangeUserId(entity.getChangeUserId());
        dto.setThumbnailFileId(entity.getThumbnailFileId());
        if (entity.getChapters() != null) {
            dto.setChapters(entity.getChapters().stream().map(contentMapper::toChapterDto).collect(Collectors.toList()));
        }
        return dto;
    }
}
