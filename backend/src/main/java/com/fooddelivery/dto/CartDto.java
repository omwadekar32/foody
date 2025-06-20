package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private int totalItems = 0;

    // Helper method to calculate totals, could be called after modifying items
    public void calculateTotals() {
        totalPrice = BigDecimal.ZERO;
        totalItems = 0;
        for (CartItemDto item : items) {
            if (item.getPrice() != null && item.getQuantity() != null) {
                totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                totalItems += item.getQuantity();
            }
        }
    }
}