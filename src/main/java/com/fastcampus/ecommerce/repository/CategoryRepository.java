package com.fastcampus.ecommerce.repository;

import com.fastcampus.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository extends JpaRepository<Category, Long> {
}
