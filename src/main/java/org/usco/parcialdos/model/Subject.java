package org.usco.parcialdos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", length = 30, nullable = false)
    private String subjectName;

    @Column(name = "subject_description", length = 100, nullable = false)
    private String subjectDescription;

    @Column(name = "subject_classroom", nullable = false)
    private Integer subjectClassroom;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @JoinColumn(name = "teacher_id", nullable = false, referencedColumnName = "id")
    @ManyToOne
    private UserEntity teacher;

}
