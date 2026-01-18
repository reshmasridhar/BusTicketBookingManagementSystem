package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ================= MAKE PAYMENT =================
    @Transactional
    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));
        
        if (paymentRepository.existsByBookingBookingId(booking.getBookingId())) {
            throw new PaymentAlreadyExistsException(
                    "Payment already exists for booking id " + booking.getBookingId());
        }

        double totalAmount = 0;

        for (Passenger passenger : booking.getPassengers()) {
            Seat seat = seatRepository.findById(passenger.getSeat().getSeatId())
                    .orElseThrow(() ->
                            new RuntimeException("Seat not found"));
            totalAmount += seat.getSeatFare();
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentTime(LocalDateTime.now());

        // SIMULATED PAYMENT GATEWAY
        boolean paymentSuccess = true; // later integrate real gateway

        if (paymentSuccess) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.BOOKED);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
        }
        
        

        bookingRepository.save(booking);
        Payment saved = paymentRepository.save(payment);

        return PaymentMapper.toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public PaymentResponse getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found"));

        return PaymentMapper.toResponse(payment);
    }

    // ================= GET BY BOOKING =================
    @Override
    public PaymentResponse getPaymentByBookingId(Long bookingId) {

        Payment payment = paymentRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found for booking"));

        return PaymentMapper.toResponse(payment);
    }

	@Override
	public List<PaymentResponse> getAllPayments() {
		// TODO Auto-generated method stub
		return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());
	}
}
