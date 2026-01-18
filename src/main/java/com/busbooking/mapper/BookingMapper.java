package com.busbooking.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.busbooking.dto.response.BookingResponse;
import com.busbooking.dto.response.PassengerResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.enums.BookingStatus;

public class BookingMapper {

    public static Booking toEntity() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.INITIATED);
        booking.setBookingTime(LocalDateTime.now());
        return booking;
    }

    public static BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setStatus(booking.getStatus());
        response.setBookingTime(booking.getBookingTime());

        List<PassengerResponse> passengers =
                booking.getPassengers().stream().map(p -> {
                    PassengerResponse pr = new PassengerResponse();
                    pr.setPassengerId(p.getPassengerId());
                    pr.setName(p.getName());
                    pr.setGender(p.getGender());
                    pr.setSeatNumber(p.getSeat().getSeatNumber());
                    pr.setSeatFare(p.getSeat().getSeatFare());
                    return pr;
                }).collect(Collectors.toList());

        response.setPassengers(passengers);
        return response;
    }
}
