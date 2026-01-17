package com.busbooking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

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
}
