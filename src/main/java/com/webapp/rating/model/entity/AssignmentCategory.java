package com.webapp.rating.model.entity;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ASSIGNMENT_CATEGORY")
public class AssignmentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    Long categoryId;
    @Column(name = "CATEGORY_TYPE")
    String categoryType;
    @Column(name = "CATEGORY_WEIGHT")
    Integer categoryWeight;
}
