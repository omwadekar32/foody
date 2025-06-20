package com.fooddelivery.controller;

import com.fooddelivery.dto.ApiResponse;
import com.fooddelivery.dto.CartDto;
import com.fooddelivery.dto.CartItemDto; // For request body of adding/updating item
import com.fooddelivery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')") // Cart operations for logged-in users
public class CartController {

    @Autowired
    private CartService cartService;

    // The CartDto is passed in the request body for most operations,
    // representing the client's current cart state.

    @PostMapping("/get") // Endpoint to "get" or process/validate a cart
    public ResponseEntity<CartDto> getCart(@RequestBody(required = false) CartDto cartDto) {
        // If cartDto is null (e.g. client has no cart yet), service should handle creating a new one.
        CartDto processedCart = cartService.getCart(cartDto == null ? new CartDto() : cartDto);
        return ResponseEntity.ok(processedCart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CartDto currentCart,
                                           @RequestParam Long dishId,
                                           @RequestParam int quantity) {
        try {
            CartDto updatedCart = cartService.addItemToCart(currentCart, dishId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PutMapping("/update") // Could also be a POST
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody CartDto currentCart,
                                                    @RequestParam Long dishId,
                                                    @RequestParam int quantity) {
         try {
            CartDto updatedCart = cartService.updateItemQuantity(currentCart, dishId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/remove") // Using POST for remove as it modifies state, could be DELETE with params
    public ResponseEntity<?> removeItemFromCart(@RequestBody CartDto currentCart,
                                                @RequestParam Long dishId) {
        try {
            CartDto updatedCart = cartService.removeItemFromCart(currentCart, dishId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<CartDto> clearCart(@RequestBody CartDto currentCart) {
        CartDto clearedCart = cartService.clearCart(currentCart);
        return ResponseEntity.ok(clearedCart);
    }
}