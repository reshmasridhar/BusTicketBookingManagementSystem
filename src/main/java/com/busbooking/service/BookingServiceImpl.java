package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.request.BookingRequest;
import com.busbooking.dto.request.PassengerRequest;
import com.busbooking.dto.response.BookingResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.entity.Schedule;
import com.busbooking.entity.Seat;
import com.busbooking.entity.User;
import com.busbooking.enums.BookingStatus;
import com.busbooking.exception.BookingNotFoundException;
import com.busbooking.exception.ScheduleNotFoundException;
import com.busbooking.exception.SeatAlreadyBookedException;
import com.busbooking.exception.SeatNotFoundException;
import com.busbooking.exception.UserNotFoundException;
import com.busbooking.mapper.BookingMapper;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.ScheduleRepository;
import com.busbooking.repository.SeatRepository;
import com.busbooking.repository.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    
    @Override
    public BookingResponse createBooking(BookingRequest request, String userEmail) {

        logger.info("Creating booking for user: {}", userEmail);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", userEmail);
                    return new UserNotFoundException("User not found");
                });

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> {
                    logger.warn("Schedule not found with id: {}", request.getScheduleId());
                    return new ScheduleNotFoundException("Schedule not found");
                });

        Booking booking = BookingMapper.toEntity();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.INITIATED);

        List<Passenger> passengerList = new ArrayList<>();

        for (PassengerRequest pr : request.getPassengers()) {

            logger.debug("Processing passenger: {}", pr.getName());

            Seat seat = seatRepository.findById(pr.getSeatId())
                    .orElseThrow(() -> {
                        logger.warn("Seat not found with id: {}", pr.getSeatId());
                        return new SeatNotFoundException("Seat not found");
                    });

            boolean seatAlreadyBooked = bookingRepository
                    .findBySchedule_ScheduleId(schedule.getScheduleId())
                    .stream()
                    .anyMatch(b -> b.getStatus() == BookingStatus.BOOKED &&
                            b.getPassengers().stream()
                                    .anyMatch(p -> p.getSeat().getSeatId().equals(seat.getSeatId())));

            if (seatAlreadyBooked) {
                logger.warn("Seat {} already booked for schedule {}",
                        seat.getSeatNumber(), schedule.getScheduleId());

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

        Booking savedBooking = bookingRepository.save(booking);

        logger.info("Booking created successfully with bookingId: {}",
                savedBooking.getBookingId());

        return BookingMapper.toResponse(savedBooking);
    }

    
    @Override
    public BookingResponse confirmPayment(Long bookingId) {

        logger.info("Confirming payment for bookingId: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.warn("Booking not found with id: {}", bookingId);
                    return new BookingNotFoundException("Booking not found");
                });

        booking.setStatus(BookingStatus.BOOKED);

        Schedule schedule = booking.getSchedule();
        int seatsBooked = booking.getPassengers().size();

        schedule.setAvailableSeats(
                schedule.getAvailableSeats() - seatsBooked
        );

        scheduleRepository.save(schedule);
        Booking savedBooking = bookingRepository.save(booking);

        logger.info("Payment confirmed. Booking marked as BOOKED for bookingId: {}", bookingId);

        return BookingMapper.toResponse(savedBooking);
    }

    
    @Override
    public BookingResponse cancelBooking(Long bookingId) {

        logger.info("Cancelling booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.warn("Booking not found with id: {}", bookingId);
                    return new BookingNotFoundException("Booking not found");
                });

        booking.setStatus(BookingStatus.CANCELLED);

        Booking cancelledBooking = bookingRepository.save(booking);

        logger.info("Booking cancelled successfully for bookingId: {}", bookingId);

        return BookingMapper.toResponse(cancelledBooking);
    }

    
    @Override
    public List<BookingResponse> getAllBookings() {

        logger.info("Fetching all bookings");

        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<BookingResponse> getBookingsByUser(String userEmail) {

        logger.info("Fetching bookings for user: {}", userEmail);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", userEmail);
                    return new UserNotFoundException("User not found");
                });

        List<Booking> bookings =
                bookingRepository.findByUser_UserId(user.getUserId());

        logger.info("Found {} bookings for user {}", bookings.size(), userEmail);

        return bookings.stream()
                .map(BookingMapper::toResponse)
                .toList();
    }
}