package com.example.shared_auth.service;

import com.example.shared_auth.dto.*;
import com.example.shared_auth.entity.*;
import com.example.shared_auth.exception.ResourceNotFoundException;
import com.example.shared_auth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityBookingRepository activityBookingRepository;
    private final UserService userService;

    // ----------- ACTIVITY MANAGEMENT ------------

    public ActivityResponse createActivity(ActivityRequest request) {
        Activity activity = Activity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        if (request.getImageUrls() != null) {
            request.getImageUrls().forEach(url -> {
                ActivityImage img = ActivityImage.builder()
                        .url(url)
                        .activity(activity)
                        .build();
                if (activity.getImages() == null) {
                    activity.setImages(new java.util.ArrayList<>());
                }
                activity.getImages().add(img);
            });
        }

        Activity saved = activityRepository.save(activity);
        return toResponse(saved);
    }

    public List<ActivityResponse> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", id));
        return toResponse(activity);
    }

    public ActivityResponse updateActivity(Long id, ActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", id));

        activity.setName(request.getName());
        activity.setDescription(request.getDescription());
        activity.setPrice(request.getPrice());

        // Replace images
        if (activity.getImages() == null) {
            activity.setImages(new java.util.ArrayList<>());
        }
        activity.getImages().clear();
        if (request.getImageUrls() != null) {
            request.getImageUrls().forEach(url -> {
                ActivityImage img = ActivityImage.builder()
                        .url(url)
                        .activity(activity)
                        .build();
                activity.getImages().add(img);
            });
        }

        return toResponse(activityRepository.save(activity));
    }

    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity", id);
        }
        activityRepository.deleteById(id);
    }

    // ----------- ACTIVITY BOOKING ------------

    public ActivityBookingResponse bookActivity(ActivityBookingRequest request, String username) {
        Long userId = userService.getIdByUsername(username);
        User user = new User();
        user.setId(userId);

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity", request.getActivityId()));

        BigDecimal total = activity.getPrice().multiply(BigDecimal.valueOf(request.getParticipants()));

        ActivityBooking booking = ActivityBooking.builder()
                .activity(activity)
                .user(user)
                .participants(request.getParticipants())
                .totalPrice(total)
                .status("PENDING")
                .build();

        ActivityBooking saved = activityBookingRepository.save(booking);

        return ActivityBookingResponse.builder()
                .bookingId(saved.getId())
                .confirmationCode(saved.getConfirmationCode())
                .activityName(activity.getName())
                .participants(saved.getParticipants())
                .totalPrice(saved.getTotalPrice())
                .status(saved.getStatus())
                .createdAt(Instant.now())
                .build();
    }

    public List<ActivityBookingResponse> getUserBookings(String username) {
        Long userId = userService.getIdByUsername(username);
        return activityBookingRepository.findByUserId(userId)
                .stream()
                .map(this::toBookingResponse)
                .collect(Collectors.toList());
    }

    private ActivityResponse toResponse(Activity a) {
    List<String> imageUrls = (a.getImages() == null ? java.util.Collections.<ActivityImage>emptyList() : a.getImages()).stream()
        .map(ActivityImage::getUrl)
        .collect(Collectors.toList());
        return ActivityResponse.builder()
                .id(a.getId())
                .name(a.getName())
                .description(a.getDescription())
                .price(a.getPrice())
                .imageUrls(imageUrls)
                .build();
    }

    private ActivityBookingResponse toBookingResponse(ActivityBooking b) {
        return ActivityBookingResponse.builder()
                .bookingId(b.getId())
                .confirmationCode(b.getConfirmationCode())
                .activityName(b.getActivity().getName())
                .participants(b.getParticipants())
                .totalPrice(b.getTotalPrice())
                .status(b.getStatus())
                .build();
    }


    public void updateBookingStatus(Long bookingId, String status) {
        ActivityBooking booking = activityBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityBooking", bookingId));
        booking.setStatus(status);
        activityBookingRepository.save(booking);
    }

    public List<ActivityBookingResponse> getAllBookings() {
        return activityBookingRepository.findAll()
                .stream()
                .map(this::toBookingResponse)
                .collect(Collectors.toList());
    }
}
