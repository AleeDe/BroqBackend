package com.example.shared_auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByAvailableTrue();
    List<Food> findByNameContainingIgnoreCase(String name);
}
