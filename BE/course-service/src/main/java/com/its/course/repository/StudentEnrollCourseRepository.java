package com.its.course.repository;

import com.its.course.entity.StudentEnrollCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentEnrollCourseRepository extends JpaRepository<StudentEnrollCourse, Long> {
    List<StudentEnrollCourse> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}

