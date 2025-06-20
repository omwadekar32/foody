package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {

    private Long id;

    @NotNull(message = "Restaurant ID cannot be null for a dish")
    private Long restaurantId; // Required for context, especially in requests

    @NotBlank(message = "Dish name cannot be blank")
    @Size(max = 255)
    private String name;

    @Size(max = 10000) // Assuming description can be long
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @Size(max = 100)
    private String category;

    @Size(max = 2048)
    private String imageUrl;

    // For responses, you might want to include restaurant name
    private String restaurantName;
}