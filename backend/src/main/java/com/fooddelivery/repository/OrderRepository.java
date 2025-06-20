package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    List<Order> findByStatus(String status);

    // Find orders by user ID and status
    List<Order> findByUserIdAndStatusOrderByOrderDateDesc(Long userId, String status);
}