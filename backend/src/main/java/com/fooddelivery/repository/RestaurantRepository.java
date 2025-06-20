package com.fooddelivery.repository;

import com.fooddelivery.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Find by location (case-insensitive)
    List<Restaurant> findByLocationContainingIgnoreCase(String location);

    // Find by cuisine (case-insensitive)
    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);

    // Find by name (case-insensitive)
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // Example of a more complex query: find by cuisine and location
    @Query("SELECT r FROM Restaurant r WHERE lower(r.cuisine) LIKE lower(concat('%', :cuisine, '%')) AND lower(r.location) LIKE lower(concat('%', :location, '%'))")
    List<Restaurant> findByCuisineAndLocation(@Param("cuisine") String cuisine, @Param("location") String location);
}