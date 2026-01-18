package com.busbooking.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.busbooking.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	
	//search methods
    List<Schedule> findBySourceAndDestinationAndJourneyDate(
            String source,
            String destination,
            LocalDate journeyDate
    );
    
    List<Schedule> findBySourceAndDestination(
            String source, String destination);

    List<Schedule> findBySourceAndJourneyDate(
            String source, LocalDate journeyDate);

    List<Schedule> findByDestinationAndJourneyDate(
            String destination, LocalDate journeyDate);

    List<Schedule> findBySource(String source);

    List<Schedule> findByDestination(String destination);

    List<Schedule> findByJourneyDate(LocalDate journeyDate);
    
    //conflict validation 
    @Query("""
            SELECT COUNT(s) > 0 FROM Schedule s
            WHERE s.bus.busId = :busId
            AND s.journeyDate = :journeyDate
            AND s.status = 'SCHEDULED'
            AND (:startTime < s.arrivalTime AND :endTime > s.departureTime)
        """)
        boolean existsBusConflict(
                Long busId,
                LocalDate journeyDate,
                LocalTime startTime,
                LocalTime endTime
        );
    
    @Query("""
            SELECT COUNT(s) > 0 FROM Schedule s
            WHERE s.driver.driverId = :driverId
            AND s.journeyDate = :journeyDate
            AND s.status = 'SCHEDULED'
            AND (:startTime < s.arrivalTime AND :endTime > s.departureTime)
        """)
        boolean existsDriverConflict(
                Long driverId,
                LocalDate journeyDate,
                LocalTime startTime,
                LocalTime endTime
        );
    
}
