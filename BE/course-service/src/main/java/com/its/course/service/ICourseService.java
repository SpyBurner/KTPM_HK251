package com.its.course.service;

import com.its.course.dto.request.CreateCourseRequest;
import com.its.course.dto.request.UpdateCourseRequest;
import com.its.course.dto.response.CourseDto;

import java.util.List;

public interface ICourseService {
    CourseDto createCourse(CreateCourseRequest request);
    CourseDto getCourseById(Long id);
    CourseDto updateCourse(Long id, UpdateCourseRequest request);
    void deleteCourse(Long id);
    List<CourseDto> listCourses(String title);
}

