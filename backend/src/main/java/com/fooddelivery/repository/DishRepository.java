package com.fooddelivery.repository;

import com.fooddelivery.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByRestaurantId(Long restaurantId);

    List<Dish> findByRestaurantIdAndCategoryContainingIgnoreCase(Long restaurantId, String category);

    List<Dish> findByNameContainingIgnoreCaseAndRestaurantId(String name, Long restaurantId);
}