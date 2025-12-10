package com.its.learning.controller;

import com.its.learning.dto.ApiResponse;
import com.its.learning.dto.response.CourseDto;
import com.its.learning.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enroll")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/course/{courseId}/student/{studentId}")
    public ApiResponse<CourseDto> enroll(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ApiResponse.success(enrollmentService.enroll(studentId, courseId), "Enrolled");
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<CourseDto>> getEnrolled(@PathVariable Long studentId) {
        return ApiResponse.success(enrollmentService.getEnrolledCourses(studentId), "Enrolled courses");
    }

    @PostMapping("/course/{courseId}/student/{studentId}/star")
    public ApiResponse<String> star(@PathVariable Long courseId, @PathVariable Long studentId, @RequestParam boolean value) {
        enrollmentService.toggleStar(studentId, courseId, value);
        return ApiResponse.success("ok", "Star updated");
    }

    @PostMapping("/course/{courseId}/student/{studentId}/save")
    public ApiResponse<String> save(@PathVariable Long courseId, @PathVariable Long studentId, @RequestParam boolean value) {
        enrollmentService.toggleSaved(studentId, courseId, value);
        return ApiResponse.success("ok", "Save updated");
    }
}

