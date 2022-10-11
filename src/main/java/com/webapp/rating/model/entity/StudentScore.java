package com.webapp.rating.model.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "STUDENT_SCORE")
public class StudentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_ID")
    Long studentId;
    @Column(name = "NAME")
    String name;
    @Column(name = "SUBJECT")
    String subject;
    @Column(name = "CATEGORY")
    String category;
    @Column(name = "SUBMISSION_DATE")
    LocalDate submissionDate;
    @Column(name = "POINTS")
    Integer points;
}
