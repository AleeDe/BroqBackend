package com.example.shared_auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.FoodOrder;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByUserId(Long userId);
}
