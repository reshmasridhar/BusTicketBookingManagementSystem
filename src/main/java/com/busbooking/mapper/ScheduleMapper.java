package com.busbooking.mapper;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Schedule;

public class ScheduleMapper {

	private ScheduleMapper() {
        // Prevent object creation
    }

    public static Schedule toEntity(
            ScheduleRequest request,
            Bus bus) {

        Schedule schedule = new Schedule();
        schedule.setBus(bus);
        schedule.setSource(request.getSource());
        schedule.setDestination(request.getDestination());
        schedule.setJourneyDate(request.getJourneyDate());
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());

        return schedule;
    }

    public static ScheduleResponse toResponse(Schedule schedule) {

        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(schedule.getScheduleId());
        response.setBusId(schedule.getBus().getBusId());
        response.setSource(schedule.getSource());
        response.setDestination(schedule.getDestination());
        response.setJourneyDate(schedule.getJourneyDate());
        response.setDepartureTime(schedule.getDepartureTime());
        response.setArrivalTime(schedule.getArrivalTime());
        response.setStatus(schedule.getStatus());

        return response;
    }
}
