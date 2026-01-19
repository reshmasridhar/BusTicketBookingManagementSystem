package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.busbooking.exception.SeatAlreadyBookedException;
import com.busbooking.exception.SeatNotFoundException;
import com.busbooking.mapper.BookingMapper;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.ScheduleRepository;
import com.busbooking.repository.SeatRepository;

@Service
public class BookingServiceImpl implements BookingService{
	
	 @Autowired private BookingRepository bookingRepository;
	    @Autowired private ScheduleRepository scheduleRepository;
	    @Autowired private SeatRepository seatRepository;
	   
	    

	@Override
	public BookingResponse createBooking(BookingRequest request) {
		// TODO Auto-generated method stub
		 Schedule schedule = scheduleRepository.findById(request.getScheduleId())
	                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

	        Booking booking = BookingMapper.toEntity();
	        booking.setSchedule(schedule);
	        booking.setBookingTime(LocalDateTime.now());
	        booking.setStatus(BookingStatus.INITIATED);

	        List<Passenger> passengerList = new ArrayList<>();

	        for (PassengerRequest pr : request.getPassengers()) {
	        	
	        	//find seat

	            Seat seat = seatRepository.findById(pr.getSeatId())
	                    .orElseThrow(() -> new SeatNotFoundException("Seat not found"));
	            
	   

	            // Check if this seat is already booked in this schedule
	            boolean seatAlreadyBooked = bookingRepository
	                    .findBySchedule_ScheduleId(schedule.getScheduleId())
	                    .stream()
	                    .anyMatch(b -> b.getStatus() == BookingStatus.BOOKED &&
	                                   b.getPassengers().stream()
	                                    .anyMatch(p -> p.getSeat().getSeatId().equals(seat.getSeatId())));
	            
	            // consider only BOOKED seats

	            if (seatAlreadyBooked) {
	                throw new SeatAlreadyBookedException(
	                        "Seat " + seat.getSeatNumber() + " is already booked for this schedule"
	                );
	            }

	            Passenger passenger = new Passenger();
	            passenger.setName(pr.getName());
	            passenger.setAge(pr.getAge());
	            passenger.setGender(pr.getGender());
	            passenger.setSeat(seat);
	            passenger.setBooking(booking);

	            passengerList.add(passenger);
	        }

	        booking.setPassengers(passengerList);
	        Booking saved = bookingRepository.save(booking);

	        return BookingMapper.toResponse(saved);
	}

	@Override
	public BookingResponse confirmPayment(Long bookingId) {
		// TODO Auto-generated method stub
		Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.BOOKED);
        return BookingMapper.toResponse(bookingRepository.save(booking));
	}

	@Override
	public BookingResponse cancelBooking(Long bookingId) {
		// TODO Auto-generated method stub
		Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);
        return BookingMapper.toResponse(bookingRepository.save(booking));
	}

	@Override
	public List<BookingResponse> getAllBookings() {
		// TODO Auto-generated method stub
		return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
	}

}
