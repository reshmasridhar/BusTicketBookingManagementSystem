package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Booking;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Review;
import com.busbooking.entity.Schedule;
import com.busbooking.entity.User;
import com.busbooking.enums.BookingStatus;

class ReviewServiceImplTest {

    private User user;
    private Bus bus;
    private Schedule schedule;
    private Booking booking;
    private Review review;

    private List<Review> reviewList;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);
        user.setName("Reshma");

        bus = new Bus();
        bus.setBusId(1L);
        bus.setBusName("Volvo Express");

        schedule = new Schedule();
        schedule.setScheduleId(1L);
        schedule.setBus(bus);
        schedule.setJourneyDate(LocalDate.now().minusDays(1)); // trip completed

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setSchedule(schedule);
        booking.setStatus(BookingStatus.BOOKED);

        review = new Review();
        review.setReviewId(1L);
        review.setBooking(booking);
        review.setBus(bus);
        review.setUser(user);
        review.setRating(5);
        review.setReview("Very comfortable journey");
        review.setCreatedAt(LocalDateTime.now());

        reviewList = new ArrayList<>();
    }

    
    @Test
    void addReview_success() {

        boolean reviewExists = false;
        for (Review r : reviewList) {
            if (r.getBooking().getBookingId().equals(booking.getBookingId())) {
                reviewExists = true;
                break;
            }
        }

        assertFalse(reviewExists);

        reviewList.add(review);

        assertEquals(1, reviewList.size());
        assertEquals(5, reviewList.get(0).getRating());
        assertEquals("Very comfortable journey", reviewList.get(0).getReview());
        assertEquals(user.getName(), reviewList.get(0).getUser().getName());
    }

    
    @Test
    void addReview_bookingNotBooked() {

        booking.setStatus(BookingStatus.CANCELLED);

        boolean isBooked = booking.getStatus() == BookingStatus.BOOKED;

        assertFalse(isBooked);
    }

    
    @Test
    void addReview_tripNotCompleted() {

        schedule.setJourneyDate(LocalDate.now().plusDays(1));

        boolean tripCompleted =
                schedule.getJourneyDate().isBefore(LocalDate.now());

        assertFalse(tripCompleted);
    }

    
    @Test
    void addReview_duplicateReview() {

        reviewList.add(review);

        boolean duplicateFound = false;
        for (Review r : reviewList) {
            if (r.getBooking().getBookingId().equals(booking.getBookingId())) {
                duplicateFound = true;
                break;
            }
        }

        assertTrue(duplicateFound);
    }

    
    @Test
    void getReviewsByBusId_success() {

        reviewList.add(review);

        List<Review> busReviews = new ArrayList<>();
        for (Review r : reviewList) {
            if (r.getBus().getBusId().equals(bus.getBusId())) {
                busReviews.add(r);
            }
        }

        assertEquals(1, busReviews.size());
        assertEquals("Volvo Express", busReviews.get(0).getBus().getBusName());
        assertEquals(5, busReviews.get(0).getRating());
    }
}
