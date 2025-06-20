package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    @NotNull(message = "Dish ID cannot be null")
    private Long dishId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // These fields can be populated when retrieving cart details for display
    private String dishName;
    private BigDecimal price; // Current price of the dish
    private String imageUrl;
    private Long restaurantId;
    private String restaurantName;
}