package com.its.learning.service;

import com.its.learning.dto.request.BatchCourseRequest;
import com.its.learning.dto.response.CourseDto;
import com.its.learning.entity.StudentEnrollCourse;
import com.its.learning.exception.AppException;
import com.its.learning.exception.ErrorCode;
import com.its.learning.repository.StudentEnrollCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentEnrollCourseRepository enrollRepo;
    private final ICourseService courseService;


    @Transactional
    public CourseDto enroll(Long studentId, Long courseId) {
        CourseDto course = courseService
                .getCourseById(courseId)
                .getResult();

        if (course == null) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND, "Course not found");
        }

        boolean exists = enrollRepo.existsByStudentIdAndCourseId(studentId, courseId);
        if (!exists) {
            StudentEnrollCourse enroll = new StudentEnrollCourse();
            enroll.setStudentId(studentId);
            enroll.setCourseId(courseId);
            enroll.setEnrolledAt(LocalDateTime.now());
            enrollRepo.save(enroll);
        }

        return course;
    }

    public List<CourseDto> getEnrolledCourses(Long studentId) {
        List<StudentEnrollCourse> enrollments = enrollRepo.findByStudentId(studentId);
        List<Long> courseIds = enrollments.stream()
                .map(StudentEnrollCourse::getCourseId)
                .collect(Collectors.toList());

        if (courseIds.isEmpty()) {
            return List.of();
        }

        BatchCourseRequest request = BatchCourseRequest.builder()
                .courseIds(courseIds)
                .build();

        return courseService.getBatchCourses(request).getResult();
    }

    @Transactional
    public void toggleStar(Long studentId, Long courseId, boolean starred) {
        StudentEnrollCourse e = enrollRepo.findByStudentId(studentId).stream()
                .filter(x -> x.getCourseId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Enrollment not found"));
        e.setIsStarred(starred);
        enrollRepo.save(e);
    }

    @Transactional
    public void toggleSaved(Long studentId, Long courseId, boolean saved) {
        StudentEnrollCourse e = enrollRepo.findByStudentId(studentId).stream()
                .filter(x -> x.getCourseId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Enrollment not found"));
        e.setIsSaved(saved);
        enrollRepo.save(e);
    }
}

