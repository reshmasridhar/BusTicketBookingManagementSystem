package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Payment;
import com.busbooking.entity.Seat;
import com.busbooking.enums.BookingStatus;
import com.busbooking.enums.PaymentMode;
import com.busbooking.enums.PaymentStatus;

class PaymentServiceImplTest {

    private Booking booking;
    private Passenger passenger1;
    private Passenger passenger2;
    private Seat seat1;
    private Seat seat2;
    private Payment payment;

    private List<Payment> paymentList;

    @BeforeEach
    void setUp() {

        seat1 = new Seat();
        seat1.setSeatId(1L);
        seat1.setSeatFare(500.0);

        seat2 = new Seat();
        seat2.setSeatId(2L);
        seat2.setSeatFare(600.0);

        passenger1 = new Passenger();
        passenger1.setName("Resh");
        passenger1.setSeat(seat1);

        passenger2 = new Passenger();
        passenger2.setName("Meena");
        passenger2.setSeat(seat2);

        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPassengers(passengers);
        booking.setStatus(BookingStatus.INITIATED);

        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setBooking(booking);

        paymentList = new ArrayList<>();
    }

    
    @Test
    void makePayment_success() {

        double totalAmount = 0;
        for (Passenger p : booking.getPassengers()) {
            totalAmount += p.getSeat().getSeatFare();
        }

        payment.setAmount(totalAmount);
        payment.setPaymentMode(PaymentMode.CARD);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTime(LocalDateTime.now());

        booking.setStatus(BookingStatus.BOOKED);
        paymentList.add(payment);

        assertEquals(1, paymentList.size());
        assertEquals(1100.0, payment.getAmount());
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
        assertEquals(BookingStatus.BOOKED, booking.getStatus());
        assertNotNull(payment.getPaymentTime());
    }

    
    @Test
    void getPaymentById_success() {

        paymentList.add(payment);

        Payment foundPayment = null;
        for (Payment p : paymentList) {
            if (p.getPaymentId().equals(1L)) {
                foundPayment = p;
                break;
            }
        }

        assertNotNull(foundPayment);
        assertEquals(1L, foundPayment.getPaymentId());
    }

    
    @Test
    void getPaymentByBookingId_success() {

        paymentList.add(payment);

        Payment foundPayment = null;
        for (Payment p : paymentList) {
            if (p.getBooking().getBookingId().equals(1L)) {
                foundPayment = p;
                break;
            }
        }

        assertNotNull(foundPayment);
        assertEquals(1L, foundPayment.getBooking().getBookingId());
    }

    
    @Test
    void getAllPayments_success() {

        paymentList.add(payment);

        Payment payment2 = new Payment();
        payment2.setPaymentId(2L);
        payment2.setBooking(booking);
        payment2.setAmount(800.0);
        payment2.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentList.add(payment2);

        assertEquals(2, paymentList.size());
        assertNotEquals(paymentList.get(0).getPaymentId(),
                        paymentList.get(1).getPaymentId());
    }
}
