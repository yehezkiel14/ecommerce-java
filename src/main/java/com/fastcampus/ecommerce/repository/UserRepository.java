package com.fastcampus.ecommerce.repository;

import com.fastcampus.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT * FROM USERS
            WHERE username = :keyword OR
            email = :keyword
            """, nativeQuery = true)
    Optional<User> findByKeyword(String keyword);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = """
            SELECT * FROM USERS
            WHERE lower(username) LIKE :keyword OR
            lower(email) LIKE :keyword
            """, nativeQuery = true)
    Page<User> searchUsers(String keyword, Pageable pageable);
}
