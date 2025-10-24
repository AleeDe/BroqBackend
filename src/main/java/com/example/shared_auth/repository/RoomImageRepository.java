package com.example.shared_auth.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.RoomImage;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findByRoomId(Long roomId);
}
