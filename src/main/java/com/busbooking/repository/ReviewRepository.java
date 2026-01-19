package com.busbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.busbooking.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Check if booking already reviewed
    boolean existsByBooking_BookingId(Long bookingId);

    // Get all reviews of a bus
    List<Review> findByBus_BusId(Long busId);

    // Calculate average rating of a bus
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bus.busId = :busId")
    Double getAverageRatingByBusId(Long busId);

    // Count ratings for a bus
    long countByBus_BusId(Long busId);
}
