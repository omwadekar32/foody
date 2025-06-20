package com.fooddelivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username; // Typically email for username
    private String name; // User's full name
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String name, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.name = name;
        this.roles = roles;
    }
}