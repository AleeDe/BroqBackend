package com.example.shared_auth.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.FoodImage;

import java.util.List;

public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
    List<FoodImage> findByFoodId(Long foodId);
}
