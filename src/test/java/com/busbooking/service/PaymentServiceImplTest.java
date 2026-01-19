package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.request.PaymentRequest;
import com.busbooking.dto.response.PaymentResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Payment;
import com.busbooking.entity.Seat;
import com.busbooking.enums.BookingStatus;
import com.busbooking.enums.PaymentMode;
import com.busbooking.enums.PaymentStatus;
import com.busbooking.exception.BookingNotFoundException;
import com.busbooking.exception.PaymentAlreadyExistsException;
import com.busbooking.exception.PaymentNotFoundException;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.PaymentRepository;
import com.busbooking.repository.SeatRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Booking booking;
    private Seat seat;
    private Passenger passenger;
    private Payment payment;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup seat
        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setSeatFare(100.0);

        // Setup passenger
        passenger = new Passenger();
        passenger.setPassengerId(1L);
        passenger.setSeat(seat);
        passenger.setName("John Doe");

        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        // Setup booking
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPassengers(passengers);
        booking.setStatus(BookingStatus.INITIATED);

        // Setup payment request
        paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(1L);
        paymentRequest.setPaymentMode(PaymentMode.CARD);

        // Setup payment
        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setBooking(booking);
        payment.setAmount(100.0);
        payment.setPaymentMode(PaymentMode.CARD);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTime(LocalDateTime.now());
    }

    // ================= MAKE PAYMENT =================
    @Test
    void makePayment_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingBookingId(1L)).thenReturn(false);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        PaymentResponse response = paymentService.makePayment(paymentRequest);

        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS, response.getPaymentStatus());
        assertEquals(BookingStatus.BOOKED, booking.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void makePayment_bookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    void makePayment_alreadyExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingBookingId(1L)).thenReturn(true);

        assertThrows(PaymentAlreadyExistsException.class,
                () -> paymentService.makePayment(paymentRequest));
    }

    // ================= GET PAYMENT BY ID =================
    @Test
    void getPaymentById_success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPaymentById(1L);

        assertEquals(payment.getPaymentId(), response.getPaymentId());
    }

    @Test
    void getPaymentById_notFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.getPaymentById(1L));
    }

    // ================= GET PAYMENT BY BOOKING ID =================
    @Test
    void getPaymentByBookingId_success() {
        when(paymentRepository.findByBookingBookingId(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPaymentByBookingId(1L);

        assertEquals(payment.getPaymentId(), response.getPaymentId());
    }

    @Test
    void getPaymentByBookingId_notFound() {
        when(paymentRepository.findByBookingBookingId(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.getPaymentByBookingId(1L));
    }

    // ================= GET ALL PAYMENTS =================
    @Test
    void getAllPayments_success() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<PaymentResponse> responses = paymentService.getAllPayments();

        assertEquals(1, responses.size());
        assertEquals(payment.getPaymentId(), responses.get(0).getPaymentId());
    }
}
