package com.its.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_finish_chapter", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "chapter_id"}))
public class StudentFinishChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

}

