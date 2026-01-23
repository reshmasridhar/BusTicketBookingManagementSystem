package com.busbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.busbooking.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

   
    boolean existsByBooking_BookingId(Long bookingId);

    List<Review> findByBus_BusId(Long busId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bus.busId = :busId")
    Double getAverageRatingByBusId(Long busId);

    long countByBus_BusId(Long busId);
}
