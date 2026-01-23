package com.busbooking.mapper;

import java.time.LocalDateTime;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Driver;
import com.busbooking.entity.Schedule;

public class ScheduleMapper {

    public static Schedule toEntity(
            ScheduleRequest request,
            Bus bus,
            Driver driver
    ) {
        Schedule schedule = new Schedule();
        schedule.setBus(bus);
        schedule.setDriver(driver);
        schedule.setSource(request.getSource());
        schedule.setDestination(request.getDestination());
        schedule.setJourneyDate(request.getJourneyDate());
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setTotalSeats(bus.getTotalSeats());
        schedule.setAvailableSeats(bus.getTotalSeats());
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setCreatedBy("ADMIN");
        return schedule;
    }

    public static ScheduleResponse toResponse(
            Schedule schedule,
            Double avgRating) {

        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(schedule.getScheduleId());
        response.setBusId(schedule.getBus().getBusId());
        response.setDriverId(schedule.getDriver().getDriverId());
        response.setSource(schedule.getSource());
        response.setDestination(schedule.getDestination());
        response.setJourneyDate(schedule.getJourneyDate());
        response.setDepartureTime(schedule.getDepartureTime());
        response.setArrivalTime(schedule.getArrivalTime());
        response.setStatus(schedule.getStatus());

        response.setAverageRating(
                avgRating == null ? 0.0 : avgRating
        );

        return response;
    }

}
