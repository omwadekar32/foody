package com.fooddelivery.controller;

import com.fooddelivery.dto.ApiResponse;
import com.fooddelivery.dto.DishDto;
import com.fooddelivery.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api") // Base path
public class DishController {

    @Autowired
    private DishService dishService;

    // Public: Get all dishes for a specific restaurant
    @GetMapping("/dishes")
    public ResponseEntity<List<DishDto>> getDishesByRestaurant(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(dishService.getDishesByRestaurantIdAndCategory(restaurantId, category));
        }
        return ResponseEntity.ok(dishService.getDishesByRestaurantId(restaurantId));
    }

    // Public: Get a specific dish by its ID
    @GetMapping("/dishes/{id}")
    public ResponseEntity<?> getDishById(@PathVariable Long id) {
        return dishService.getDishById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Dish not found with id: " + id)));
    }

    // Admin: Create a new dish for a specific restaurant
    @PostMapping("/admin/dishes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDish(@Valid @RequestBody DishDto dishDto) {
        // restaurantId must be present in DishDto for creation
        if (dishDto.getRestaurantId() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Restaurant ID is required to create a dish."));
        }
        try {
            DishDto createdDish = dishService.createDish(dishDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
        } catch (RuntimeException e) { // Catch specific exceptions like RestaurantNotFound
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error creating dish: " + e.getMessage()));
        }
    }

    // Admin: Update an existing dish
    @PutMapping("/admin/dishes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDish(@PathVariable Long id, @Valid @RequestBody DishDto dishDto) {
         if (dishDto.getRestaurantId() == null) {
            // If restaurantId is not part of the update payload for an existing dish,
            // it implies the dish's restaurant association isn't changing.
            // The service layer should handle this logic (e.g., fetch existing dish to get restaurantId if needed).
            // For now, we assume if it's an update, the service might only update fields other than restaurantId
            // or require restaurantId to re-validate context.
            // Let's assume service handles fetching existing restaurant context if restaurantId is not in DTO.
        }
        return dishService.updateDish(id, dishDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Dish not found with id: " + id + " for update.")));
    }

    // Admin: Delete a dish
    @DeleteMapping("/admin/dishes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDish(@PathVariable Long id) {
        if (dishService.deleteDish(id)) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Dish deleted successfully."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Dish not found with id: " + id + " for deletion."));
    }
}