package com.busbooking.service;

import java.time.LocalDate;
import java.util.List;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;

public interface ScheduleService {

    ScheduleResponse createSchedule(ScheduleRequest request);

    List<ScheduleResponse> getAllSchedules();

    List<ScheduleResponse> searchSchedules(
            String source,
            String destination,
            LocalDate journeyDate);

    ScheduleResponse cancelSchedule(Long scheduleId);
}
