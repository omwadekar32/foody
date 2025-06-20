package com.fooddelivery.service;

import com.fooddelivery.dto.DishDto;
import com.fooddelivery.dto.RestaurantDto;
import com.fooddelivery.model.Dish;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Helper to convert Entity to DTO
    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setLocation(restaurant.getLocation());
        dto.setCuisine(restaurant.getCuisine());
        dto.setImageUrl(restaurant.getImageUrl());
        dto.setRating(restaurant.getRating());
        if (restaurant.getDishes() != null) {
            dto.setDishes(restaurant.getDishes().stream().map(this::convertDishToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    // Helper to convert Dish Entity to DishDto (basic version)
    private DishDto convertDishToDto(Dish dish) {
        DishDto dto = new DishDto();
        dto.setId(dish.getId());
        dto.setRestaurantId(dish.getRestaurant().getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        dto.setImageUrl(dish.getImageUrl());
        // dto.setRestaurantName(dish.getRestaurant().getName()); // Could be added if needed
        return dto;
    }


    @Transactional(readOnly = true)
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantDto> getRestaurantById(Long id) {
        return restaurantRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> findRestaurants(String name, String location, String cuisine) {
        // This is a simple implementation. A more robust solution might use Specifications or Querydsl.
        if (name != null && !name.isEmpty()) {
            return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        }
        if (cuisine != null && !cuisine.isEmpty() && location != null && !location.isEmpty()) {
             return restaurantRepository.findByCuisineAndLocation(cuisine, location).stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        }
        if (cuisine != null && !cuisine.isEmpty()) {
            return restaurantRepository.findByCuisineContainingIgnoreCase(cuisine).stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        }
        if (location != null && !location.isEmpty()) {
            return restaurantRepository.findByLocationContainingIgnoreCase(location).stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        }
        return getAllRestaurants(); // Default to all if no specific criteria
    }


    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDto.getName());
        restaurant.setLocation(restaurantDto.getLocation());
        restaurant.setCuisine(restaurantDto.getCuisine());
        restaurant.setImageUrl(restaurantDto.getImageUrl());
        restaurant.setRating(restaurantDto.getRating());
        // Dishes are typically managed separately or via DishService
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToDto(savedRestaurant);
    }

    @Transactional
    public Optional<RestaurantDto> updateRestaurant(Long id, RestaurantDto restaurantDto) {
        return restaurantRepository.findById(id)
                .map(existingRestaurant -> {
                    existingRestaurant.setName(restaurantDto.getName());
                    existingRestaurant.setLocation(restaurantDto.getLocation());
                    existingRestaurant.setCuisine(restaurantDto.getCuisine());
                    existingRestaurant.setImageUrl(restaurantDto.getImageUrl());
                    existingRestaurant.setRating(restaurantDto.getRating());
                    Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
                    return convertToDto(updatedRestaurant);
                });
    }

    @Transactional
    public boolean deleteRestaurant(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }
}