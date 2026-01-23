package com.busbooking.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.busbooking.repository.ReviewRepository;
import com.busbooking.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger =
            LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    
    @Override
    public ScheduleResponse createSchedule(ScheduleRequest request) {

        logger.info("Creating schedule for busId: {}, driverId: {}, date: {}",
                request.getBusId(), request.getDriverId(), request.getJourneyDate());

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> {
                    logger.warn("Bus not found with id: {}", request.getBusId());
                    return new BusNotFoundException("Bus not found");
                });

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> {
                    logger.warn("Driver not found with id: {}", request.getDriverId());
                    return new DriverNotFoundException("Driver not found");
                });

        boolean busConflict =
                scheduleRepository.existsBusConflict(
                        bus.getBusId(),
                        request.getJourneyDate(),
                        request.getDepartureTime(),
                        request.getArrivalTime());

        if (busConflict) {
            logger.warn("Bus already scheduled for this time slot. busId: {}", bus.getBusId());
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
            logger.warn("Driver already scheduled at this time. driverId: {}", driver.getDriverId());
            throw new DriverAlreadyScheduledException(
                    "Driver is already assigned to another schedule at this time");
        }

        Schedule schedule =
                ScheduleMapper.toEntity(request, bus, driver);

        schedule.setTotalSeats(bus.getTotalSeats());
        schedule.setAvailableSeats(bus.getTotalSeats());

        Schedule saved = scheduleRepository.save(schedule);

        logger.info("Schedule created successfully with scheduleId: {}", saved.getScheduleId());

        return ScheduleMapper.toResponse(saved, 0.0);
    }

    
    @Override
    public List<ScheduleResponse> getAllSchedules() {

        logger.info("Fetching all schedules");

        List<ScheduleResponse> schedules = scheduleRepository.findAll()
                .stream()
                .map(schedule -> {

                    Double avgRating =
                            reviewRepository.getAverageRatingByBusId(
                                    schedule.getBus().getBusId());

                    return ScheduleMapper.toResponse(schedule, avgRating);
                })
                .toList();

        logger.info("Total schedules found: {}", schedules.size());

        return schedules;
    }

    
    @Override
    public List<ScheduleResponse> searchSchedules(
            String source,
            String destination,
            LocalDate journeyDate) {

        logger.info("Searching schedules | source: {}, destination: {}, date: {}",
                source, destination, journeyDate);

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

        List<ScheduleResponse> responses = schedules.stream()
                .map(schedule -> {
                    Double avgRating =
                            reviewRepository.getAverageRatingByBusId(
                                    schedule.getBus().getBusId());

                    return ScheduleMapper.toResponse(schedule, avgRating);
                })
                .toList();

        logger.info("Schedules found after search: {}", responses.size());

        return responses;
    }

    
    @Override
    public ScheduleResponse cancelSchedule(Long scheduleId) {

        logger.info("Cancelling schedule with id: {}", scheduleId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> {
                    logger.warn("Schedule not found with id: {}", scheduleId);
                    return new ScheduleNotFoundException(
                            "Schedule not found with id: " + scheduleId);
                });

        schedule.setStatus(ScheduleStatus.CANCELLED);

        Double avgRating =
                reviewRepository.getAverageRatingByBusId(
                        schedule.getBus().getBusId());

        ScheduleResponse response = ScheduleMapper.toResponse(
                scheduleRepository.save(schedule),
                avgRating);

        logger.info("Schedule cancelled successfully with id: {}", scheduleId);

        return response;
    }
}
