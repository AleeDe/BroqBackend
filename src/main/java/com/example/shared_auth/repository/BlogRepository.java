package com.example.shared_auth.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.Blog;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthorId(Long authorId);
    List<Blog> findByTitleContainingIgnoreCase(String title);
}
