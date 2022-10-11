package com.webapp.rating.repository;

import com.webapp.rating.model.entity.StudentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentScoreRepo extends JpaRepository<StudentScore, Long> {
}
