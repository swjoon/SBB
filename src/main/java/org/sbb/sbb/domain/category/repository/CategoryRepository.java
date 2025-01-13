package org.sbb.sbb.domain.category.repository;

import org.sbb.sbb.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategory(String name);
}
