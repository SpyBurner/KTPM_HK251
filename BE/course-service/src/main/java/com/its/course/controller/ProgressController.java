package com.its.course.controller;

import com.its.course.dto.ApiResponse;
import com.its.course.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/student/{studentId}/chapter/{chapterId}/finish")
    public ApiResponse<String> finish(@PathVariable Long studentId, @PathVariable Long chapterId) {
        progressService.markChapterFinished(studentId, chapterId);
        return ApiResponse.success("ok", "Chapter marked finished");
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ApiResponse<Double> getProgress(@PathVariable Long studentId, @PathVariable Long courseId) {
        double p = progressService.getCourseProgressForStudent(studentId, courseId);
        return ApiResponse.success(p, "Progress retrieved");
    }
}

