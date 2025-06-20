package com.fooddelivery.service;

import com.fooddelivery.dto.DishDto;
import com.fooddelivery.model.Dish;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.DishRepository;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository; // To fetch restaurant for context

    // Helper to convert Entity to DTO
    private DishDto convertToDto(Dish dish) {
        DishDto dto = new DishDto();
        dto.setId(dish.getId());
        dto.setRestaurantId(dish.getRestaurant().getId());
        dto.setRestaurantName(dish.getRestaurant().getName()); // Add restaurant name
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        dto.setImageUrl(dish.getImageUrl());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<DishDto> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DishDto> getDishById(Long id) {
        return dishRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<DishDto> getDishesByRestaurantId(Long restaurantId) {
        return dishRepository.findByRestaurantId(restaurantId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DishDto> getDishesByRestaurantIdAndCategory(Long restaurantId, String category) {
        return dishRepository.findByRestaurantIdAndCategoryContainingIgnoreCase(restaurantId, category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DishDto createDish(DishDto dishDto) {
        Restaurant restaurant = restaurantRepository.findById(dishDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + dishDto.getRestaurantId()));

        Dish dish = new Dish();
        dish.setRestaurant(restaurant);
        dish.setName(dishDto.getName());
        dish.setDescription(dishDto.getDescription());
        dish.setPrice(dishDto.getPrice());
        dish.setCategory(dishDto.getCategory());
        dish.setImageUrl(dishDto.getImageUrl());

        Dish savedDish = dishRepository.save(dish);
        return convertToDto(savedDish);
    }

    @Transactional
    public Optional<DishDto> updateDish(Long id, DishDto dishDto) {
        return dishRepository.findById(id)
                .map(existingDish -> {
                    // Ensure restaurant context is correct if it's part of the update
                    if (!existingDish.getRestaurant().getId().equals(dishDto.getRestaurantId())) {
                        Restaurant restaurant = restaurantRepository.findById(dishDto.getRestaurantId())
                                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + dishDto.getRestaurantId()));
                        existingDish.setRestaurant(restaurant);
                    }
                    existingDish.setName(dishDto.getName());
                    existingDish.setDescription(dishDto.getDescription());
                    existingDish.setPrice(dishDto.getPrice());
                    existingDish.setCategory(dishDto.getCategory());
                    existingDish.setImageUrl(dishDto.getImageUrl());
                    Dish updatedDish = dishRepository.save(existingDish);
                    return convertToDto(updatedDish);
                });
    }

    @Transactional
    public boolean deleteDish(Long id) {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
            return true;
        }
        return false;
    }
}