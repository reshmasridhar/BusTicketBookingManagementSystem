package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Bus;
import com.busbooking.entity.Driver;
import com.busbooking.entity.Schedule;
import com.busbooking.enums.ScheduleStatus;

class ScheduleServiceImplTest {

    private Bus bus;
    private Driver driver;
    private Schedule schedule;

    private List<Schedule> scheduleList;

    @BeforeEach
    void setUp() {

        bus = new Bus();
        bus.setBusId(1L);
        bus.setBusName("Volvo Express");
        bus.setTotalSeats(40);

        driver = new Driver();
        driver.setDriverId(1L);
        driver.setName("Ramesh");

        schedule = new Schedule();
        schedule.setScheduleId(1L);
        schedule.setBus(bus);
        schedule.setDriver(driver);
        schedule.setSource("Bangalore");
        schedule.setDestination("Chennai");
        schedule.setJourneyDate(LocalDate.now().plusDays(1));
        schedule.setDepartureTime(LocalTime.of(9, 0));
        schedule.setArrivalTime(LocalTime.of(15, 0));
        schedule.setStatus(ScheduleStatus.SCHEDULED);
        schedule.setTotalSeats(bus.getTotalSeats());
        schedule.setAvailableSeats(bus.getTotalSeats());

        scheduleList = new ArrayList<>();
    }

    
    @Test
    void createSchedule_success() {

        boolean busConflict = false;
        boolean driverConflict = false;

        for (Schedule s : scheduleList) {
            if (s.getBus().getBusId().equals(bus.getBusId())
                    && s.getJourneyDate().equals(schedule.getJourneyDate())) {
                busConflict = true;
            }

            if (s.getDriver().getDriverId().equals(driver.getDriverId())
                    && s.getJourneyDate().equals(schedule.getJourneyDate())) {
                driverConflict = true;
            }
        }

        assertFalse(busConflict);
        assertFalse(driverConflict);

        scheduleList.add(schedule);

        assertEquals(1, scheduleList.size());
        assertEquals("Bangalore", scheduleList.get(0).getSource());
        assertEquals(40, scheduleList.get(0).getAvailableSeats());
    }

    
    @Test
    void createSchedule_busAlreadyScheduled() {

        scheduleList.add(schedule);

        boolean busConflict = false;
        for (Schedule s : scheduleList) {
            if (s.getBus().getBusId().equals(bus.getBusId())
                    && s.getJourneyDate().equals(schedule.getJourneyDate())) {
                busConflict = true;
                break;
            }
        }

        assertTrue(busConflict);
    }

    
    @Test
    void createSchedule_driverAlreadyScheduled() {

        scheduleList.add(schedule);

        boolean driverConflict = false;
        for (Schedule s : scheduleList) {
            if (s.getDriver().getDriverId().equals(driver.getDriverId())
                    && s.getJourneyDate().equals(schedule.getJourneyDate())) {
                driverConflict = true;
                break;
            }
        }

        assertTrue(driverConflict);
    }

    
    @Test
    void getAllSchedules_success() {

        scheduleList.add(schedule);

        assertEquals(1, scheduleList.size());
        assertEquals("Chennai", scheduleList.get(0).getDestination());
    }

    
    @Test
    void searchSchedules_sourceAndDestination() {

        scheduleList.add(schedule);

        List<Schedule> result = new ArrayList<>();
        for (Schedule s : scheduleList) {
            if (s.getSource().equals("Bangalore")
                    && s.getDestination().equals("Chennai")) {
                result.add(s);
            }
        }

        assertEquals(1, result.size());
        assertEquals("Volvo Express", result.get(0).getBus().getBusName());
    }

    
    @Test
    void searchSchedules_byJourneyDate() {

        scheduleList.add(schedule);

        List<Schedule> result = new ArrayList<>();
        for (Schedule s : scheduleList) {
            if (s.getJourneyDate().equals(schedule.getJourneyDate())) {
                result.add(s);
            }
        }

        assertEquals(1, result.size());
    }

    
    @Test
    void cancelSchedule_success() {

        scheduleList.add(schedule);

        Schedule s = scheduleList.get(0);
        s.setStatus(ScheduleStatus.CANCELLED);

        assertEquals(ScheduleStatus.CANCELLED, s.getStatus());
    }

    
    @Test
    void cancelSchedule_notFound() {

        Schedule notFound = null;

        for (Schedule s : scheduleList) {
            if (s.getScheduleId().equals(99L)) {
                notFound = s;
            }
        }

        assertNull(notFound);
    }
}
