package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.exception.domain.CategoryNotFoundException;
import com.javaspringboot.DevicesManagementSystemBackend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Boolean existsByDescription(String description);

    Category findCategoriesByDescription(String description);
}
