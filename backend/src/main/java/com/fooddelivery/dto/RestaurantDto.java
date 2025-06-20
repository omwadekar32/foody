package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    private Long id;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(max = 255, message = "Restaurant name must be less than 255 characters")
    private String name;

    @Size(max = 255, message = "Location must be less than 255 characters")
    private String location;

    @Size(max = 100, message = "Cuisine type must be less than 100 characters")
    private String cuisine;

    @Size(max = 2048, message = "Image URL too long")
    private String imageUrl;

    @DecimalMin(value = "0.0", message = "Rating must be non-negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal rating;

    private List<DishDto> dishes; // Optional, for responses that include dishes
}