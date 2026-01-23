package com.busbooking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ReviewResponse addReview(ReviewRequest request, String userEmail) {

        logger.info("Adding review for bookingId: {} by user: {}",
                request.getBookingId(), userEmail);

        
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    logger.warn("Booking not found with id: {}", request.getBookingId());
                    return new CustomReviewException("Booking not found");
                });

        
        if (booking.getStatus() != BookingStatus.BOOKED) {
            logger.warn("Review attempt for non-booked bookingId: {}", request.getBookingId());
            throw new CustomReviewException(
                    "Review allowed only for confirmed bookings");
        }

        
        if (!booking.getSchedule()
                .getJourneyDate()
                .isBefore(LocalDate.now())) {

            logger.warn("Review attempted before journey completion for bookingId: {}",
                    request.getBookingId());

            throw new CustomReviewException("Trip not completed yet");
        }

        
        if (reviewRepository
                .existsByBooking_BookingId(request.getBookingId())) {

            logger.warn("Duplicate review attempt for bookingId: {}", request.getBookingId());

            throw new CustomReviewException(
                    "Review already submitted for this booking");
        }

        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", userEmail);
                    return new CustomReviewException("User not found");
                });

        
        Review review = new Review();
        review.setBooking(booking);
        review.setBus(booking.getSchedule().getBus());
        review.setUser(user);
        review.setRating(request.getRating());
        review.setReview(request.getReview());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        logger.info("Review saved successfully with reviewId: {}", saved.getReviewId());

        return mapToResponse(saved);
    }

    @Override
    public List<ReviewResponse> getReviewsByBusId(Long busId) {

        logger.info("Fetching reviews for busId: {}", busId);

        List<ReviewResponse> reviews = reviewRepository.findByBus_BusId(busId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Total reviews found for busId {}: {}", busId, reviews.size());

        return reviews;
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
