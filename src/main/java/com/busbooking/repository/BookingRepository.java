package com.busbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entity.Booking;
import com.busbooking.entity.Schedule;
import com.busbooking.entity.Seat;
import com.busbooking.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findBySchedule_ScheduleId(Long scheduleId);

	
    
}
