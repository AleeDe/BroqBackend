package com.example.shared_auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.FoodOrderItem;

import java.util.List;

public interface FoodOrderItemRepository extends JpaRepository<FoodOrderItem, Long> {
    List<FoodOrderItem> findByFoodOrderId(Long foodOrderId);
    List<FoodOrderItem> findByFoodId(Long foodId);
}
