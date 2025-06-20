package com.fooddelivery.service;

import com.fooddelivery.dto.CartDto;
import com.fooddelivery.dto.CartItemDto;
import com.fooddelivery.model.Dish;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private DishRepository dishRepository;

    // This is a simplified cart service.
    // In a real application, cart management would be more complex,
    // potentially involving user sessions (if not purely client-side) or a persistent cart table.
    // For this example, we'll assume the CartDto is built/managed by the client and sent for processing,
    // or this service helps in validating/enriching a cart DTO.

    /**
     * Adds an item to a given cart DTO or updates its quantity.
     * This method simulates modifying a cart that would typically be managed client-side or in a session.
     * @param cartDto The current state of the cart.
     * @param dishId The ID of the dish to add.
     * @param quantity The quantity to add.
     * @return The updated CartDto.
     */
    public CartDto addItemToCart(CartDto cartDto, Long dishId, int quantity) {
        if (cartDto == null) {
            cartDto = new CartDto();
        }
        if (cartDto.getItems() == null) {
            cartDto.setItems(new ArrayList<>());
        }

        Optional<Dish> dishOpt = dishRepository.findById(dishId);
        if (!dishOpt.isPresent()) {
            throw new RuntimeException("Dish not found with id: " + dishId);
        }
        Dish dish = dishOpt.get();
        Restaurant restaurant = dish.getRestaurant(); // Get restaurant for context

        Optional<CartItemDto> existingItemOpt = cartDto.getItems().stream()
                .filter(item -> item.getDishId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItemDto existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItemDto newItem = new CartItemDto();
            newItem.setDishId(dish.getId());
            newItem.setDishName(dish.getName());
            newItem.setPrice(dish.getPrice()); // Current price
            newItem.setImageUrl(dish.getImageUrl());
            newItem.setQuantity(quantity);
            newItem.setRestaurantId(restaurant.getId());
            newItem.setRestaurantName(restaurant.getName());
            cartDto.getItems().add(newItem);
        }

        cartDto.calculateTotals();
        return cartDto;
    }

    /**
     * Removes an item completely from the cart DTO.
     * @param cartDto The current cart.
     * @param dishId The ID of the dish to remove.
     * @return The updated CartDto.
     */
    public CartDto removeItemFromCart(CartDto cartDto, Long dishId) {
        if (cartDto != null && cartDto.getItems() != null) {
            cartDto.getItems().removeIf(item -> item.getDishId().equals(dishId));
            cartDto.calculateTotals();
        }
        return cartDto;
    }

    /**
     * Updates the quantity of a specific item in the cart DTO.
     * @param cartDto The current cart.
     * @param dishId The ID of the dish to update.
     * @param quantity The new quantity. If 0, removes the item.
     * @return The updated CartDto.
     */
    public CartDto updateItemQuantity(CartDto cartDto, Long dishId, int quantity) {
        if (cartDto == null || cartDto.getItems() == null) {
            return cartDto; // Or throw error
        }

        if (quantity <= 0) {
            return removeItemFromCart(cartDto, dishId);
        }

        Optional<CartItemDto> existingItemOpt = cartDto.getItems().stream()
                .filter(item -> item.getDishId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            existingItemOpt.get().setQuantity(quantity);
        } else {
            // If item not in cart and quantity > 0, could add it (depends on desired behavior)
            // For now, we only update existing. To add, call addItemToCart.
            // Alternatively, could throw new RuntimeException("Item not in cart to update quantity");
            // Or, if quantity > 0, treat as add:
            return addItemToCart(cartDto, dishId, quantity);
        }
        
        cartDto.calculateTotals();
        return cartDto;
    }

    /**
     * Clears all items from the cart DTO.
     * @param cartDto The cart to clear.
     * @return An empty CartDto.
     */
    public CartDto clearCart(CartDto cartDto) {
        if (cartDto != null) {
            cartDto.setItems(new ArrayList<>());
            cartDto.calculateTotals();
        }
        return cartDto == null ? new CartDto() : cartDto;
    }

    /**
     * "Retrieves" a cart. In this simplified model, it just returns the passed DTO,
     * potentially after validation or enrichment if it were more complex.
     * @param cartDto The cart data, possibly from client.
     * @return The processed CartDto.
     */
    public CartDto getCart(CartDto cartDto) {
        // Here you could add logic to re-validate prices, stock, etc.
        // For now, just recalculate totals to ensure consistency.
        if (cartDto == null) {
            return new CartDto();
        }
        // Ensure all items have current prices if not already set (example enrichment)
        for(CartItemDto item : cartDto.getItems()){
            if(item.getPrice() == null){
                dishRepository.findById(item.getDishId()).ifPresent(dish -> {
                    item.setPrice(dish.getPrice());
                    item.setDishName(dish.getName()); // ensure name is fresh
                    item.setImageUrl(dish.getImageUrl()); // ensure image is fresh
                    if(dish.getRestaurant() != null){
                        item.setRestaurantId(dish.getRestaurant().getId());
                        item.setRestaurantName(dish.getRestaurant().getName());
                    }
                });
            }
        }
        cartDto.calculateTotals();
        return cartDto;
    }
}