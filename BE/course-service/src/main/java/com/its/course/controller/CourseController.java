package com.its.course.controller;

import com.its.course.dto.ApiResponse;
import com.its.course.dto.request.CreateCourseRequest;
import com.its.course.dto.request.UpdateCourseRequest;
import com.its.course.dto.response.CourseDto;
import com.its.course.service.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course management REST controller
 */
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping
    public ApiResponse<List<CourseDto>> getAllCourses(@RequestParam(value = "title", required = false) String title) {
        return ApiResponse.success(courseService.listCourses(title), "Courses retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseDto> getCourseById(@PathVariable Long id) {
        return ApiResponse.success(courseService.getCourseById(id), "Course retrieved successfully");
    }

    @PostMapping
    public ApiResponse<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ApiResponse.success(courseService.createCourse(request), "Course created");
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest request) {
        return ApiResponse.success(courseService.updateCourse(id, request), "Course updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("Course deleted", "Deleted");
    }

    @GetMapping("/hehe")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("Course service is running", "Health check passed");
    }
}