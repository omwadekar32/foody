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
public class OrderItemDto {

    private Long id; // Only for response

    @NotNull(message = "Dish ID cannot be null")
    private Long dishId;

    private String dishName; // For response
    private String dishImageUrl; // For response

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price at order cannot be null")
    private BigDecimal priceAtOrder; // Can be set by backend or validated if provided
}