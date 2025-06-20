package com.fooddelivery.controller;

import com.fooddelivery.dto.ApiResponse;
import com.fooddelivery.dto.RestaurantDto;
import com.fooddelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api") // Base path for all restaurant related endpoints
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // Public endpoint to get all restaurants or filter them
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String cuisine) {
        List<RestaurantDto> restaurants = restaurantService.findRestaurants(name, location, cuisine);
        return ResponseEntity.ok(restaurants);
    }

    // Public endpoint to get a specific restaurant by ID
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Restaurant not found with id: " + id)));
    }

    // Admin endpoint to create a new restaurant
    @PostMapping("/admin/restaurants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        try {
            RestaurantDto createdRestaurant = restaurantService.createRestaurant(restaurantDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error creating restaurant: " + e.getMessage()));
        }
    }

    // Admin endpoint to update an existing restaurant
    @PutMapping("/admin/restaurants/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.updateRestaurant(id, restaurantDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Restaurant not found with id: " + id + " for update.")));
    }

    // Admin endpoint to delete a restaurant
    @DeleteMapping("/admin/restaurants/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        if (restaurantService.deleteRestaurant(id)) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Restaurant deleted successfully."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Restaurant not found with id: " + id + " for deletion."));
    }
}