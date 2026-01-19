package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.request.BookingRequest;
import com.busbooking.dto.request.PassengerRequest;
import com.busbooking.dto.response.BookingResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Schedule;
import com.busbooking.entity.Seat;
import com.busbooking.enums.BookingStatus;
import com.busbooking.exception.BookingNotFoundException;
import com.busbooking.exception.ScheduleNotFoundException;
import com.busbooking.exception.SeatNotFoundException;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.ScheduleRepository;
import com.busbooking.repository.SeatRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequest bookingRequest;
    private Booking booking;
    private Schedule schedule;
    private Seat seat;
    private PassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Schedule setup
        schedule = new Schedule();
        schedule.setScheduleId(1L);

        // Seat setup
        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setSeatFare(100.0);

        // Passenger request
        passengerRequest = new PassengerRequest();
        passengerRequest.setName("John Doe");
        passengerRequest.setAge(30);
        passengerRequest.setGender("Male");
        passengerRequest.setSeatId(1L);

        // Booking request
        bookingRequest = new BookingRequest();
        bookingRequest.setScheduleId(1L);
        bookingRequest.setPassengers(List.of(passengerRequest));

        // Booking entity
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setSchedule(schedule);
        booking.setStatus(BookingStatus.INITIATED);

        // Attach passenger to avoid NPE in BookingMapper
        Passenger passenger = new Passenger();
        passenger.setPassengerId(1L);
        passenger.setName("John Doe");
        passenger.setGender("Male");
        passenger.setSeat(seat);
        passenger.setBooking(booking);

        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        booking.setPassengers(passengers);
    }

    // ================= CREATE BOOKING =================
    @Test
    void createBooking_success() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse response = bookingService.createBooking(bookingRequest);

        assertNotNull(response);
        assertEquals(booking.getBookingId(), response.getBookingId());
        assertEquals(1, response.getPassengers().size());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_scheduleNotFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScheduleNotFoundException.class,
                () -> bookingService.createBooking(bookingRequest));
    }

    @Test
    void createBooking_seatNotFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class,
                () -> bookingService.createBooking(bookingRequest));
    }

    // ================= CONFIRM PAYMENT =================
    @Test
    void confirmPayment_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse response = bookingService.confirmPayment(1L);

        assertEquals(BookingStatus.BOOKED, response.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void confirmPayment_bookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.confirmPayment(1L));
    }

    // ================= CANCEL BOOKING =================
    @Test
    void cancelBooking_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse response = bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, response.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void cancelBooking_bookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.cancelBooking(1L));
    }

    // ================= GET ALL BOOKINGS =================
    @Test
    void getAllBookings_success() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponse> responses = bookingService.getAllBookings();

        assertEquals(1, responses.size());
        assertEquals(booking.getBookingId(), responses.get(0).getBookingId());
        assertEquals(1, responses.get(0).getPassengers().size());
    }
}
