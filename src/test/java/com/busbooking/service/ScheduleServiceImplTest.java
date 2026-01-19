package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Driver;
import com.busbooking.entity.Schedule;
import com.busbooking.enums.ScheduleStatus;
import com.busbooking.exception.*;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.DriverRepository;
import com.busbooking.repository.ScheduleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Bus bus;
    private Driver driver;
    private Schedule schedule;
    private ScheduleRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus = new Bus();
        bus.setBusId(1L);

        driver = new Driver();
        driver.setDriverId(1L);

        schedule = new Schedule();
        schedule.setScheduleId(1L);
        schedule.setBus(bus);
        schedule.setDriver(driver);
        schedule.setStatus(ScheduleStatus.SCHEDULED);

        request = new ScheduleRequest();
        request.setBusId(1L);
        request.setDriverId(1L);
        request.setSource("CityA");
        request.setDestination("CityB");
        request.setJourneyDate(LocalDate.now());
        request.setDepartureTime(LocalTime.of(10, 0));
        request.setArrivalTime(LocalTime.of(12, 0));
    }

    // ================= CREATE =================
    @Test
    void createSchedule_success() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(scheduleRepository.existsBusConflict(anyLong(), any(), any(), any())).thenReturn(false);
        when(scheduleRepository.existsDriverConflict(anyLong(), any(), any(), any())).thenReturn(false);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        ScheduleResponse response = scheduleService.createSchedule(request);

        assertNotNull(response);
        assertEquals(1L, response.getScheduleId());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void createSchedule_busNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> scheduleService.createSchedule(request));
    }

    @Test
    void createSchedule_driverNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> scheduleService.createSchedule(request));
    }

    @Test
    void createSchedule_busConflict() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(scheduleRepository.existsBusConflict(anyLong(), any(), any(), any())).thenReturn(true);

        assertThrows(BusAlreadyScheduledException.class, () -> scheduleService.createSchedule(request));
    }

    @Test
    void createSchedule_driverConflict() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(scheduleRepository.existsBusConflict(anyLong(), any(), any(), any())).thenReturn(false);
        when(scheduleRepository.existsDriverConflict(anyLong(), any(), any(), any())).thenReturn(true);

        assertThrows(DriverAlreadyScheduledException.class, () -> scheduleService.createSchedule(request));
    }

    // ================= GET ALL =================
    @Test
    void getAllSchedules_success() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<ScheduleResponse> schedules = scheduleService.getAllSchedules();

        assertEquals(1, schedules.size());
        assertEquals(schedule.getScheduleId(), schedules.get(0).getScheduleId());
    }

    // ================= SEARCH =================
    @Test
    void searchSchedules_allParams() {
        when(scheduleRepository.findBySourceAndDestinationAndJourneyDate(anyString(), anyString(), any()))
                .thenReturn(List.of(schedule));

        List<ScheduleResponse> result = scheduleService.searchSchedules("CityA", "CityB", LocalDate.now());

        assertEquals(1, result.size());
    }

    @Test
    void searchSchedules_noParams() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<ScheduleResponse> result = scheduleService.searchSchedules(null, null, null);

        assertEquals(1, result.size());
    }

    // ================= CANCEL =================
    @Test
    void cancelSchedule_success() {
        schedule.setStatus(ScheduleStatus.SCHEDULED);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        ScheduleResponse response = scheduleService.cancelSchedule(1L);

        assertEquals(ScheduleStatus.CANCELLED, schedule.getStatus());
        assertEquals(schedule.getScheduleId(), response.getScheduleId());
    }

    @Test
    void cancelSchedule_notFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.cancelSchedule(1L));
    }
}
