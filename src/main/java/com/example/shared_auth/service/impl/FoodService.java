package com.example.shared_auth.service.impl;

import com.example.shared_auth.dto.FoodRequest;
import com.example.shared_auth.dto.FoodResponse;
import com.example.shared_auth.entity.Food;
import com.example.shared_auth.entity.FoodImage;
import com.example.shared_auth.exception.ResourceNotFoundException;
import com.example.shared_auth.repository.FoodImageRepository;
import com.example.shared_auth.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodImageRepository foodImageRepository;

    public FoodResponse createFood(FoodRequest request) {
        Food food = Food.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .available(request.getAvailable())
            .category(request.getCategory())
            .build();

        Food saved = foodRepository.save(food);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (String url : request.getImages()) {
                FoodImage img = FoodImage.builder()
                        .url(url)
                        .food(saved)
                        .build();
                foodImageRepository.save(img);
            }
        }

        return toResponse(saved);
    }

    public FoodResponse updateFood(Long id, FoodRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", id));

        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setAvailable(request.getAvailable());
        food.setCategory(request.getCategory());

        // Replace images
        if (request.getImages() != null) {
            foodImageRepository.deleteAll(food.getImages());
            for (String url : request.getImages()) {
                FoodImage img = FoodImage.builder()
                        .url(url)
                        .food(food)
                        .build();
                foodImageRepository.save(img);
            }
        }

        return toResponse(food);
    }

    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", id));
        foodRepository.delete(food);
    }

    @Transactional(readOnly = true)
    public List<FoodResponse> getAllFoods(boolean availableOnly) {
        List<Food> foods = availableOnly ? foodRepository.findByAvailableTrue() : foodRepository.findAll();
        return foods.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FoodResponse getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", id));
        return toResponse(food);
    }

    private FoodResponse toResponse(Food food) {
        List<String> images = food.getImages() != null
                ? food.getImages().stream().map(FoodImage::getUrl).collect(Collectors.toList())
                : List.of();

        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .available(food.isAvailable())
                .images(images)
                .category(food.getCategory())
                .build();
    }
}
