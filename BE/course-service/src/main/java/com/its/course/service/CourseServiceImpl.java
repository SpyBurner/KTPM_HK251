package com.its.course.service;

import com.its.course.dto.request.CreateCourseRequest;
import com.its.course.dto.request.UpdateCourseRequest;
import com.its.course.dto.response.CourseDto;
import com.its.course.entity.Course;
import com.its.course.exception.AppException;
import com.its.course.exception.ErrorCode;
import com.its.course.mapper.CourseMapper;
import com.its.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseDto createCourse(CreateCourseRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Title is required");
        }
        Course entity = courseMapper.toEntity(request);
        Course saved = courseRepository.save(entity);
        return courseMapper.toDto(saved);
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Course not found"));
        return courseMapper.toDto(c);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(Long id, UpdateCourseRequest request) {
        Course entity = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Course not found"));
        courseMapper.updateEntity(request, entity);
        Course saved = courseRepository.save(entity);
        return courseMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Course not found");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseDto> listCourses(String title) {
        List<Course> list;
        if (title == null || title.trim().isEmpty()) {
            list = courseRepository.findAll();
        } else {
            list = courseRepository.findByTitleContainingIgnoreCase(title);
        }
        return list.stream().map(courseMapper::toDto).collect(Collectors.toList());
    }

    // ===== IMPLEMENTATION MỚI =====
    @Override
    public List<CourseDto> getCoursesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Course> courses = courseRepository.findAllById(ids);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }


}