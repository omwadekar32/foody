CREATE DATABASE IF NOT EXISTS food_delivery_db;

USE food_delivery_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- e.g., 'CUSTOMER', 'ADMIN'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    cuisine VARCHAR(100), -- Added based on frontend
    image_url VARCHAR(2048),
    rating DECIMAL(2,1), -- e.g., 4.5
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100), -- Added based on frontend
    image_url VARCHAR(2048),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL, -- e.g., 'PENDING', 'PREPARING', 'DELIVERED', 'CANCELLED'
    total_price DECIMAL(10,2) NOT NULL,
    shipping_address TEXT, -- Added for checkout details
    customer_name VARCHAR(255), -- Added for checkout details
    customer_phone VARCHAR(20), -- Added for checkout details
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_order DECIMAL(10,2) NOT NULL, -- Price of the dish when the order was placed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(id) -- Consider ON DELETE SET NULL or RESTRICT based on business logic
);

-- Indexes for performance
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_restaurant_location ON restaurants(location);
CREATE INDEX idx_restaurant_cuisine ON restaurants(cuisine);
CREATE INDEX idx_dish_restaurant_id ON dishes(restaurant_id);
CREATE INDEX idx_order_user_id ON orders(user_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_item_order_id ON order_items(order_id);
CREATE INDEX idx_order_item_dish_id ON order_items(dish_id);

-- Sample Data (Optional - for testing)
/*
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@example.com', '$2a$10$your_encoded_password_here', 'ADMIN'), -- Replace with a real hashed password
('Customer One', 'customer1@example.com', '$2a$10$your_encoded_password_here', 'CUSTOMER');

INSERT INTO restaurants (name, location, cuisine, image_url, rating) VALUES
('The Pizza Place', '123 Main St, Anytown', 'Italian', 'https://via.placeholder.com/300x200.png?text=Pizza+Place', 4.5),
('Taco Fiesta', '456 Oak Rd, Anytown', 'Mexican', 'https://via.placeholder.com/300x200.png?text=Taco+Fiesta', 4.2),
('Sushi Heaven', '789 Pine Ln, Anytown', 'Japanese', 'https://via.placeholder.com/300x200.png?text=Sushi+Heaven', 4.8);

INSERT INTO dishes (restaurant_id, name, description, price, category, image_url) VALUES
(1, 'Margherita Pizza', 'Classic tomato, mozzarella, and basil pizza.', 12.99, 'Pizza', 'https://via.placeholder.com/150.png?text=Margherita'),
(1, 'Pepperoni Pizza', 'Loaded with pepperoni and mozzarella.', 14.50, 'Pizza', 'https://via.placeholder.com/150.png?text=Pepperoni'),
(2, 'Chicken Tacos', 'Three grilled chicken tacos with salsa and guacamole.', 9.75, 'Tacos', 'https://via.placeholder.com/150.png?text=Chicken+Tacos'),
(3, 'Salmon Nigiri', 'Two pieces of fresh salmon over sushi rice.', 6.50, 'Sushi', 'https://via.placeholder.com/150.png?text=Salmon+Nigiri');
*/