package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.request.BookingRequest;
import com.busbooking.dto.response.BookingResponse;

public interface BookingService {
	
	BookingResponse createBooking(BookingRequest request);

    BookingResponse confirmPayment(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);

    List<BookingResponse> getAllBookings();

}
