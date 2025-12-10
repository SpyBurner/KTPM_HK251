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
@Table(name = "student_enroll_course", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class StudentEnrollCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "is_starred")
    private Boolean isStarred = false;

    @Column(name = "is_saved")
    private Boolean isSaved = false;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

}

