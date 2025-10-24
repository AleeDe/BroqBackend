package com.example.shared_auth.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.ActivityImage;

import java.util.List;

public interface ActivityImageRepository extends JpaRepository<ActivityImage, Long> {
    List<ActivityImage> findByActivityId(Long activityId);
}
