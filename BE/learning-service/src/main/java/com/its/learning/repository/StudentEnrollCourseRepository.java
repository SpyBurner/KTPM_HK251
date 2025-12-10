package com.its.learning.repository;

import com.its.learning.entity.StudentEnrollCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentEnrollCourseRepository extends JpaRepository<StudentEnrollCourse, Long> {
    List<StudentEnrollCourse> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}

