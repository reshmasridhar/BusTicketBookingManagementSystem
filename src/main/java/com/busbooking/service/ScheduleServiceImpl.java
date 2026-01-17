package com.busbooking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Schedule;
import com.busbooking.enums.ScheduleStatus;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.exception.ScheduleNotFoundException;
import com.busbooking.mapper.ScheduleMapper;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.ScheduleRepository;
import com.busbooking.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BusRepository busRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               BusRepository busRepository) {
        this.scheduleRepository = scheduleRepository;
        this.busRepository = busRepository;
    }

    // ================= CREATE =================
    @Override
    public ScheduleResponse createSchedule(ScheduleRequest request) {

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() ->
                        new BusNotFoundException(
                                "Bus not found with id: " + request.getBusId()));

        Schedule schedule = ScheduleMapper.toEntity(request, bus);
        schedule.setStatus(ScheduleStatus.ACTIVE);

        return ScheduleMapper.toResponse(
                scheduleRepository.save(schedule));
    }

    // ================= GET ALL =================
    @Override
    public List<ScheduleResponse> getAllSchedules() {

        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleMapper::toResponse)
                .toList();
    }

    // ================= SEARCH =================
    @Override
    public List<ScheduleResponse> searchSchedules(
            String source,
            String destination,
            LocalDate journeyDate) {

        List<Schedule> schedules;

        if (source != null && destination != null && journeyDate != null) {
            schedules = scheduleRepository
                    .findBySourceAndDestinationAndJourneyDate(
                            source, destination, journeyDate);

        } else if (source != null && destination != null) {
            schedules = scheduleRepository
                    .findBySourceAndDestination(source, destination);

        } else if (source != null && journeyDate != null) {
            schedules = scheduleRepository
                    .findBySourceAndJourneyDate(source, journeyDate);

        } else if (destination != null && journeyDate != null) {
            schedules = scheduleRepository
                    .findByDestinationAndJourneyDate(destination, journeyDate);

        } else if (source != null) {
            schedules = scheduleRepository.findBySource(source);

        } else if (destination != null) {
            schedules = scheduleRepository.findByDestination(destination);

        } else if (journeyDate != null) {
            schedules = scheduleRepository.findByJourneyDate(journeyDate);

        } else {
            schedules = scheduleRepository.findAll();
        }

        return schedules.stream()
                .map(ScheduleMapper::toResponse)
                .toList();
    }

    // ================= CANCEL =================
    @Override
    public ScheduleResponse cancelSchedule(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new ScheduleNotFoundException(
                                "Schedule not found with id: " + scheduleId));

        schedule.setStatus(ScheduleStatus.CANCELLED);

        return ScheduleMapper.toResponse(
                scheduleRepository.save(schedule));
    }
}
