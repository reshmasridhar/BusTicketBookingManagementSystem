package com.busbooking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.request.ReviewRequest;
import com.busbooking.dto.response.ReviewResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Review;
import com.busbooking.entity.User;
import com.busbooking.enums.BookingStatus;
import com.busbooking.exception.CustomReviewException;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.ReviewRepository;
import com.busbooking.repository.UserRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ReviewResponse addReview(ReviewRequest request, String userEmail) {

        // 1 Fetch booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new CustomReviewException("Booking not found"));

       
        

        //  Check booking status
        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new CustomReviewException(
                    "Review allowed only for confirmed bookings");
        }

        //  Check journey completion
        if (!booking.getSchedule()
                .getJourneyDate()
                .isBefore(LocalDate.now())) {

            throw new CustomReviewException(
                    "Trip not completed yet");
        }

        // Prevent duplicate reviews
        if (reviewRepository
                .existsByBooking_BookingId(request.getBookingId())) {

            throw new CustomReviewException(
                    "Review already submitted for this booking");
        }

        // Fetch user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new CustomReviewException("User not found"));

        // Create review
        Review review = new Review();
        review.setBooking(booking);
        review.setBus(booking.getSchedule().getBus());
        review.setUser(user);
        review.setRating(request.getRating());
        review.setReview(request.getReview());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        return mapToResponse(saved);
    }

    @Override
    public List<ReviewResponse> getReviewsByBusId(Long busId) {
        return reviewRepository.findByBus_BusId(busId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setReviewId(review.getReviewId());
        response.setRating(review.getRating());
        response.setReview(review.getReview());
        response.setUserName(review.getUser().getName());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
