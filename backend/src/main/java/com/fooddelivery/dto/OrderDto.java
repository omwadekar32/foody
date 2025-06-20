package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "User ID cannot be null for an order")
    private Long userId; // For request payload

    private String userName; // For response payload
    private String userEmail; // For response payload

    private LocalDateTime orderDate;

    @NotBlank(message = "Order status cannot be blank")
    private String status;

    @NotNull(message = "Total price cannot be null")
    private BigDecimal totalPrice;

    @Size(max = 1000)
    private String shippingAddress;

    @Size(max = 255)
    private String customerName;

    @Size(max = 20)
    private String customerPhone;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid // To validate nested OrderItemDto objects
    private List<OrderItemDto> orderItems;
}