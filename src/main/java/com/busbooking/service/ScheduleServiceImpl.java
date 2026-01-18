package com.busbooking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Driver;
import com.busbooking.entity.Schedule;
import com.busbooking.enums.ScheduleStatus;
import com.busbooking.exception.BusAlreadyScheduledException;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.exception.DriverAlreadyScheduledException;
import com.busbooking.exception.DriverNotFoundException;
import com.busbooking.exception.ScheduleNotFoundException;
import com.busbooking.mapper.ScheduleMapper;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.DriverRepository;
import com.busbooking.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DriverRepository driverRepository;

    // ================= CREATE =================
    @Override
    public ScheduleResponse createSchedule(ScheduleRequest request) {

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() ->
                        new BusNotFoundException("Bus not found"));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() ->
                        new DriverNotFoundException("Driver not found"));

        boolean busConflict =
                scheduleRepository.existsBusConflict(
                        bus.getBusId(),
                        request.getJourneyDate(),
                        request.getDepartureTime(),
                        request.getArrivalTime());

        if (busConflict) {
            throw new BusAlreadyScheduledException(
                    "Bus is already scheduled for this time slot");
        }

        boolean driverConflict =
                scheduleRepository.existsDriverConflict(
                        driver.getDriverId(),
                        request.getJourneyDate(),
                        request.getDepartureTime(),
                        request.getArrivalTime());

        if (driverConflict) {
            throw new DriverAlreadyScheduledException(
                    "Driver is already assigned to another schedule at this time");
        }

        Schedule schedule =
                ScheduleMapper.toEntity(request, bus, driver);

        Schedule saved = scheduleRepository.save(schedule);

        return ScheduleMapper.toResponse(saved);
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
