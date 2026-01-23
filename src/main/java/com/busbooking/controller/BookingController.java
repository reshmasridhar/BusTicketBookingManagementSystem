package com.busbooking.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.BookingRequest;
import com.busbooking.dto.response.BookingResponse;
import com.busbooking.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody @Valid BookingRequest request,Principal principal) {

        return ResponseEntity.ok(
                bookingService.createBooking(request, principal.getName()) 
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{bookingId}/payment/success")
    public ResponseEntity<BookingResponse> confirmPayment(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.confirmPayment(bookingId)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(bookingId)
        );
    }

    

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }
    
    
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<List<BookingResponse>> getUserBookings(Principal principal) {

        String userEmail = principal.getName();
        System.out.println("Logged-in user email: " + userEmail);

        List<BookingResponse> bookings = bookingService.getBookingsByUser(userEmail);

        return ResponseEntity.ok(bookings);
    }
    
    
    
    
    
    
    
}
