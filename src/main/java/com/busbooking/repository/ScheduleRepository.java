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
}
