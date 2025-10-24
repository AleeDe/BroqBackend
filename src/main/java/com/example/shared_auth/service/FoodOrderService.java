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
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final FoodRepository foodRepository;
    private final FoodOrderItemRepository foodOrderItemRepository;
    private final UserService userService;

    public FoodOrderResponse createOrder(FoodOrderRequest request, String username) {
        Long userId = userService.getIdByUsername(username);
        User user = new User();
        user.setId(userId);

        BigDecimal total = BigDecimal.ZERO;

        FoodOrder order = FoodOrder.builder()
                .user(user)
                .diningMode(request.getDiningMode())
                .orderTime(Instant.now())
                .status("PENDING")
                .build();

        // initialize totalPrice to zero so the initial INSERT doesn't violate the
        // NOT NULL constraint on the total_price column
        order.setTotalPrice(BigDecimal.ZERO);

        FoodOrder savedOrder = foodOrderRepository.save(order);

        for (FoodOrderItemRequest itemReq : request.getItems()) {
            Food food = foodRepository.findById(itemReq.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food", itemReq.getFoodId()));

            BigDecimal itemTotal = food.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(itemTotal);

            FoodOrderItem item = FoodOrderItem.builder()
                    .food(food)
                    .foodOrder(savedOrder)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(food.getPrice())
                    .build();

            foodOrderItemRepository.save(item);
        }

        savedOrder.setTotalPrice(total);
        foodOrderRepository.save(savedOrder);

        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<FoodOrderResponse> getUserOrders(String username) {
        Long userId = userService.getIdByUsername(username);
        return foodOrderRepository.findByUserId(userId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FoodOrderResponse getOrderById(Long id, String username) {
        Long userId = userService.getIdByUsername(username);
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodOrder", id));

        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied for this order");
        }

        return toResponse(order);
    }

    private FoodOrderResponse toResponse(FoodOrder order) {
        List<FoodOrderItemResponse> items = (order.getItems() == null ? Collections.<FoodOrderItem>emptyList() : order.getItems())
                .stream()
                .map(i -> FoodOrderItemResponse.builder()
                        .foodId(i.getFood().getId())
                        .foodName(i.getFood().getName())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return FoodOrderResponse.builder()
                .orderId(order.getId())
                .orderTime(order.getOrderTime())
                .diningMode(order.getDiningMode())
                .totalPrice(order.getTotalPrice())
                .items(items)
                .status(order.getStatus())
                .build();
    }


    public List<FoodOrderResponse> getAllOrders() {
        return foodOrderRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

        public void updateOrderStatus(Long id, String status) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodOrder", id));
                if (status == null) throw new IllegalArgumentException("Status must not be null");
                        order.setStatus(status);
        foodOrderRepository.save(order);
    }
}
