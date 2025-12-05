package com.its.course.repository;

import com.its.course.entity.StudentFinishChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFinishChapterRepository extends JpaRepository<StudentFinishChapter, Long> {
    List<StudentFinishChapter> findByStudentId(Long studentId);
    List<StudentFinishChapter> findByChapterId(Long chapterId);
    boolean existsByStudentIdAndChapterId(Long studentId, Long chapterId);
}

