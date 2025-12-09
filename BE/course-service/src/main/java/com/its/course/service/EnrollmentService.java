package com.its.course.service;

import com.its.course.dto.response.CourseDto;
import com.its.course.entity.Course;
import com.its.course.entity.StudentEnrollCourse;
import com.its.course.exception.AppException;
import com.its.course.exception.ErrorCode;
import com.its.course.mapper.CourseMapper;
import com.its.course.repository.CourseRepository;
import com.its.course.repository.StudentEnrollCourseRepository;
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
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseDto enroll(Long studentId, Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND, "Course not found");
        }
        if (!enrollRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            StudentEnrollCourse enroll = new StudentEnrollCourse();
            enroll.setStudentId(studentId);
            enroll.setCourseId(courseId);
            enroll.setEnrolledAt(LocalDateTime.now());
            enrollRepo.save(enroll);
        }
        Course c = courseRepository.findById(courseId).orElseThrow();
        return courseMapper.toDto(c);
    }

    public List<CourseDto> getEnrolledCourses(Long studentId) {
        List<StudentEnrollCourse> list = enrollRepo.findByStudentId(studentId);
        List<Long> courseIds = list.stream().map(StudentEnrollCourse::getCourseId).collect(Collectors.toList());
        List<Course> courses = courseRepository.findAllById(courseIds);
        return courses.stream().map(courseMapper::toDto).collect(Collectors.toList());
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

