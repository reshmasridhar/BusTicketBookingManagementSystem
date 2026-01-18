package com.busbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.BookingRequest;
import com.busbooking.dto.response.BookingResponse;
import com.busbooking.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PutMapping("/{bookingId}/payment/success")
    public ResponseEntity<BookingResponse> confirmPayment(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.confirmPayment(bookingId));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}
