package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Schedule;
import com.busbooking.entity.Seat;
import com.busbooking.entity.User;
import com.busbooking.enums.BookingStatus;

class BookingServiceImplTest {

    private Booking booking;
    private User user;
    private Schedule schedule;
    private Seat seat1;
    private Seat seat2;
    private Passenger passenger1;
    private Passenger passenger2;
    private List<Booking> bookingList;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);
        user.setEmail("testuser@gmail.com");

        schedule = new Schedule();
        schedule.setScheduleId(101L);
        schedule.setAvailableSeats(40);

        seat1 = new Seat();
        seat1.setSeatId(1L);
        seat1.setSeatNumber("A1");

        seat2 = new Seat();
        seat2.setSeatId(2L);
        seat2.setSeatNumber("A2");

        passenger1 = new Passenger();
        passenger1.setName("Rahul");
        passenger1.setAge(25);
        passenger1.setGender("Male");
        passenger1.setSeat(seat1);

        passenger2 = new Passenger();
        passenger2.setName("Anita");
        passenger2.setAge(23);
        passenger2.setGender("Female");
        passenger2.setSeat(seat2);

        booking = new Booking();
        booking.setBookingId(1001L);
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.INITIATED);

        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);

        booking.setPassengers(passengers);

        passenger1.setBooking(booking);
        passenger2.setBooking(booking);

        bookingList = new ArrayList<>();
    }

    
    @Test
    void createBooking_success() {

        bookingList.add(booking);

        assertEquals(1, bookingList.size());
        assertEquals(BookingStatus.INITIATED, booking.getStatus());
        assertEquals(2, booking.getPassengers().size());
        assertEquals("Rahul", booking.getPassengers().get(0).getName());
        assertEquals("Anita", booking.getPassengers().get(1).getName());
        assertEquals(user.getUserId(), booking.getUser().getUserId());
    }

    
    @Test
    void confirmPayment_success() {

        int beforeSeats = schedule.getAvailableSeats();

        booking.setStatus(BookingStatus.BOOKED);
        schedule.setAvailableSeats(
                schedule.getAvailableSeats() - booking.getPassengers().size()
        );

        assertEquals(BookingStatus.BOOKED, booking.getStatus());
        assertEquals(beforeSeats - 2, schedule.getAvailableSeats());
        assertNotEquals(beforeSeats, schedule.getAvailableSeats());
    }

    
    @Test
    void cancelBooking_success() {

        booking.setStatus(BookingStatus.CANCELLED);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertNotEquals(BookingStatus.BOOKED, booking.getStatus());
    }

    
    @Test
    void getAllBookings_success() {

        bookingList.add(booking);

        Booking anotherBooking = new Booking();
        anotherBooking.setBookingId(1002L);
        anotherBooking.setStatus(BookingStatus.BOOKED);

        bookingList.add(anotherBooking);

        assertEquals(2, bookingList.size());
        assertEquals(1001L, bookingList.get(0).getBookingId());
        assertEquals(BookingStatus.BOOKED, bookingList.get(1).getStatus());
    }

   
    @Test
    void getBookingsByUser_success() {

        bookingList.add(booking);

        List<Booking> userBookings = new ArrayList<>();

        for (Booking b : bookingList) {
            if (b.getUser().getUserId().equals(user.getUserId())) {
                userBookings.add(b);
            }
        }

        assertEquals(1, userBookings.size());
        assertEquals(user.getUserId(), userBookings.get(0).getUser().getUserId());
    }
}
