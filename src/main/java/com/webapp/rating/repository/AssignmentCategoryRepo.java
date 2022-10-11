package com.webapp.rating.repository;

import com.webapp.rating.model.entity.AssignmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentCategoryRepo extends JpaRepository<AssignmentCategory, Long> {
}
