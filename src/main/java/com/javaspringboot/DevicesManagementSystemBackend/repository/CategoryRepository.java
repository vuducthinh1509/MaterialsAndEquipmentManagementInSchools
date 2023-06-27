package com.javaspringboot.DevicesManagementSystemBackend.repository;

import com.javaspringboot.DevicesManagementSystemBackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query(value = "SELECT cat.*  FROM categories cat WHERE cat.name = ?1 AND cat.description=?2",nativeQuery = true)
    Category findCategoryByNameAndDescription(String name,String description);

    @Query(value = "SELECT DISTINCT name FROM categories",nativeQuery = true)
    List<?> getDistinctByName();

    @Query(value = "select distinct c.description from categories c where c.name = :name ;",nativeQuery = true)
    List<String> listDescriptionByCategoryName(String name);

    List<Category> findCategoriesByName(String name);
}
