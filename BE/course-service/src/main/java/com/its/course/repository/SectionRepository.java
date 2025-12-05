package com.its.course.repository;

import com.its.course.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByChapterIdOrderByOrderIndex(Long chapterId);
}

