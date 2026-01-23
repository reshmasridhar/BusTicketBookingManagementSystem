package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.request.PaymentRequest;
import com.busbooking.dto.response.PaymentResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Payment;
import com.busbooking.entity.Seat;
import com.busbooking.enums.BookingStatus;
import com.busbooking.enums.PaymentStatus;
import com.busbooking.exception.BookingNotFoundException;
import com.busbooking.exception.PaymentAlreadyExistsException;
import com.busbooking.exception.PaymentNotFoundException;
import com.busbooking.mapper.PaymentMapper;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.PaymentRepository;
import com.busbooking.repository.SeatRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    
    @Transactional
    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        logger.info("Initiating payment for bookingId: {}", request.getBookingId());

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    logger.warn("Booking not found with id: {}", request.getBookingId());
                    return new BookingNotFoundException("Booking not found");
                });

        if (paymentRepository.existsByBookingBookingId(booking.getBookingId())) {
            logger.warn("Payment already exists for bookingId: {}", booking.getBookingId());
            throw new PaymentAlreadyExistsException(
                    "Payment already exists for booking id " + booking.getBookingId());
        }

        double totalAmount = 0;

        for (Passenger passenger : booking.getPassengers()) {

            Seat seat = seatRepository.findById(passenger.getSeat().getSeatId())
                    .orElseThrow(() -> {
                        logger.warn("Seat not found with id: {}", passenger.getSeat().getSeatId());
                        return new RuntimeException("Seat not found");
                    });

            totalAmount += seat.getSeatFare();
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentTime(LocalDateTime.now());

        //simulates payment
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.BOOKED);
            logger.info("Payment successful for bookingId: {}", booking.getBookingId());
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
            logger.warn("Payment failed for bookingId: {}", booking.getBookingId());
        }

        bookingRepository.save(booking);
        Payment saved = paymentRepository.save(payment);

        logger.info("Payment saved successfully with paymentId: {}", saved.getPaymentId());

        return PaymentMapper.toResponse(saved);
    }

   
    @Override
    public PaymentResponse getPaymentById(Long paymentId) {

        logger.info("Fetching payment with id: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.warn("Payment not found with id: {}", paymentId);
                    return new PaymentNotFoundException("Payment not found");
                });

        return PaymentMapper.toResponse(payment);
    }

    
    @Override
    public PaymentResponse getPaymentByBookingId(Long bookingId) {

        logger.info("Fetching payment for bookingId: {}", bookingId);

        Payment payment = paymentRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() -> {
                    logger.warn("Payment not found for bookingId: {}", bookingId);
                    return new PaymentNotFoundException("Payment not found for booking");
                });

        return PaymentMapper.toResponse(payment);
    }

    
    @Override
    public List<PaymentResponse> getAllPayments() {

        logger.info("Fetching all payments");

        List<PaymentResponse> payments = paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());

        logger.info("Total payments found: {}", payments.size());

        return payments;
    }
}
