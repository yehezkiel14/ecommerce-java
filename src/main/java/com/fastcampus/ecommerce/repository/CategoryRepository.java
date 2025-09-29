package com.fastcampus.ecommerce.repository;

import com.fastcampus.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = """
      SELECT * FROM category
      WHERE lower("name") like :name
      """, nativeQuery = true)
    List<Category> findByName(String name);
}
