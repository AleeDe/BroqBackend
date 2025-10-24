package com.example.shared_auth.repository;



import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shared_auth.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);
    List<Room> findByType(String type);
    List<Room> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Room> findByCapacity(Integer capacity);

}
