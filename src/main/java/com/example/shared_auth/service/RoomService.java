package com.example.shared_auth.service;



import java.util.List;

import com.example.shared_auth.dto.RoomRequest;
import com.example.shared_auth.dto.RoomResponse;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request);
    RoomResponse updateRoom(Long id, RoomRequest request);
    void deleteRoom(Long id);
    List<RoomResponse> findAllRooms();
    RoomResponse findRoomById(Long id);
}
