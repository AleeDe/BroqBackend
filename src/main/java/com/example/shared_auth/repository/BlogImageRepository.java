package com.example.shared_auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.BlogImage;

import java.util.List;

public interface BlogImageRepository extends JpaRepository<BlogImage, Long> {
    List<BlogImage> findByBlogId(Long blogId);
}
