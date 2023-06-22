package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Boolean existsByDescription(String description);

    Category findCategoriesByDescription(String description);

    @Query(value = "SELECT cat.*  FROM categories cat WHERE cat.name = ?1 AND cat.description=?2",nativeQuery = true)
    Category findCategoryByNameAndDescription(String name,String description);
}
