package com.example.shared_auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shared_auth.dto.RoomRequest;
import com.example.shared_auth.dto.RoomResponse;
import com.example.shared_auth.entity.Room;
import com.example.shared_auth.entity.RoomImage;
import com.example.shared_auth.entity.RoomType;
import com.example.shared_auth.exception.ResourceNotFoundException;
import com.example.shared_auth.repository.RoomRepository;
import com.example.shared_auth.service.RoomService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Business logic for rooms. Handles image normalization (RoomImage rows).
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        Room room = new Room();
        mapRequestToRoom(request, room);
        // If images are provided, attach them to the managed room instance so cascade persists them
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<RoomImage> imgs = request.getImages().stream()
                    .map(url -> RoomImage.builder().url(url).room(room).build())
                    .collect(Collectors.toList());
            room.setImages(imgs);
        }

        Room saved = roomRepository.save(room);
        return mapRoomToResponse(saved);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
        mapRequestToRoom(request, room);
        // Replace images in-place on the managed collection to preserve Hibernate's collection tracking
        // This avoids replacing the collection instance which triggers the orphanRemoval error.
        room.getImages().clear();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<RoomImage> imgs = request.getImages().stream()
                    .map(url -> RoomImage.builder().url(url).room(room).build())
                    .collect(Collectors.toList());
            // addAll to the managed collection so cascade persists the new images
            room.getImages().addAll(imgs);
        }

        Room updated = roomRepository.save(room);
        return mapRoomToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
        roomRepository.delete(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> findAllRooms() {
        return roomRepository.findAll().stream().map(this::mapRoomToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse findRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", id));
        return mapRoomToResponse(room);
    }

    // ----- mapping helpers -----
    private void mapRequestToRoom(RoomRequest req, Room room) {
        room.setName(req.getName());
        try {
            room.setType(RoomType.valueOf(req.getType().toUpperCase()));
        } catch (Exception ex) {
            // fallback to SINGLE if invalid
            room.setType(RoomType.SINGLE);
        }
        room.setPrice(req.getPrice());
        room.setDescription(req.getDescription());
        room.setCapacity(req.getCapacity());
    }

    private RoomResponse mapRoomToResponse(Room room) {
        RoomResponse r = new RoomResponse();
        r.setId(room.getId());
        r.setName(room.getName());
        r.setType(room.getType() != null ? room.getType().name() : null);
        r.setDescription(room.getDescription());
        r.setPrice(room.getPrice());
        r.setCapacity(room.getCapacity());
        r.setImages(
                room.getImages() == null ? Collections.emptyList() :
                        room.getImages().stream().map(RoomImage::getUrl).collect(Collectors.toList())
        );
        r.setCreatedAt(room.getCreatedAt());
        r.setUpdatedAt(room.getUpdatedAt());
        return r;
    }
}
