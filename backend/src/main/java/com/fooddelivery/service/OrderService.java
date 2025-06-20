package com.fooddelivery.service;

import com.fooddelivery.dto.OrderDto;
import com.fooddelivery.dto.OrderItemDto;
import com.fooddelivery.model.Dish;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.User;
import com.fooddelivery.repository.DishRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    // Helper to convert OrderItemDto to OrderItem entity
    private OrderItem convertOrderItemDtoToEntity(OrderItemDto dto, Order order) {
        Dish dish = dishRepository.findById(dto.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + dto.getDishId()));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setDish(dish);
        item.setQuantity(dto.getQuantity());
        // Price at order should be the current price of the dish when order is placed
        item.setPriceAtOrder(dish.getPrice());
        return item;
    }

    // Helper to convert OrderItem entity to OrderItemDto
    private OrderItemDto convertOrderItemToDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setDishId(item.getDish().getId());
        dto.setDishName(item.getDish().getName());
        dto.setDishImageUrl(item.getDish().getImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtOrder(item.getPriceAtOrder());
        return dto;
    }

    // Helper to convert Order entity to OrderDto
    private OrderDto convertOrderToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setUserName(order.getUser().getName());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerPhone(order.getCustomerPhone());
        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream().map(this::convertOrderItemToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found for order creation."));

        Order order = new Order();
        order.setUser(currentUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(orderDto.getStatus() != null ? orderDto.getStatus() : "PENDING"); // Default status
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setCustomerName(orderDto.getCustomerName() != null ? orderDto.getCustomerName() : currentUser.getName());
        order.setCustomerPhone(orderDto.getCustomerPhone()); // Assuming phone is part of user profile or provided in DTO

        List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                .map(itemDto -> convertOrderItemDtoToEntity(itemDto, order))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        // Calculate total price
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            totalPrice = totalPrice.add(item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())));
        }
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return convertOrderToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersForCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));
        return orderRepository.findByUserIdOrderByOrderDateDesc(currentUser.getId()).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderByIdForCurrentUser(Long orderId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));
        return orderRepository.findById(orderId)
                .filter(order -> order.getUser().getId().equals(currentUser.getId())) // Ensure user owns the order
                .map(this::convertOrderToDto);
    }

    // Admin: Get all orders
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    // Admin: Get specific order by ID
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertOrderToDto);
    }


    // Admin: Update order status
    @Transactional
    public Optional<OrderDto> updateOrderStatus(Long orderId, String status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = orderRepository.save(order);
                    return convertOrderToDto(updatedOrder);
                });
    }
}