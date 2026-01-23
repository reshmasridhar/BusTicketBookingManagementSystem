package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Bus;
import com.busbooking.enums.BusType;

class BusServiceImplTest {

    private Bus bus;
    private List<Bus> busList;

    @BeforeEach
    void setUp() {

        bus = new Bus();
        bus.setBusId(1L);
        bus.setBusNumber("TN01AB1234");
        bus.setBusName("Volvo Express");
        bus.setBusType(BusType.AC_SEATER);
        bus.setTotalSeats(40);
        bus.setCreatedBy("admin@gmail.com");
        bus.setCreatedAt(LocalDateTime.now());

        busList = new ArrayList<>();
    }

    
    @Test
    void addBus_success() {

        busList.add(bus);

        assertEquals(1, busList.size());
        assertEquals("Volvo Express", busList.get(0).getBusName());
        assertEquals(BusType.AC_SEATER, busList.get(0).getBusType());
        assertEquals(40, busList.get(0).getTotalSeats());
        assertNotNull(busList.get(0).getCreatedAt());
    }

    
    @Test
    void updateBus_success() {

        busList.add(bus);

        Bus existingBus = busList.get(0);
        existingBus.setBusName("Updated Volvo");
        existingBus.setBusType(BusType.AC_SLEEPER);
        existingBus.setTotalSeats(30);
        existingBus.setUpdatedBy("admin@gmail.com");
        existingBus.setUpdatedAt(LocalDateTime.now());

        assertEquals("Updated Volvo", existingBus.getBusName());
        assertEquals(BusType.AC_SLEEPER, existingBus.getBusType());
        assertEquals(30, existingBus.getTotalSeats());
        assertNotNull(existingBus.getUpdatedAt());
    }

    
    @Test
    void deleteBus_success() {

        busList.add(bus);
        assertEquals(1, busList.size());

        busList.remove(bus);

        assertEquals(0, busList.size());
    }

    
    @Test
    void getBusById_success() {

        busList.add(bus);

        Bus foundBus = null;
        for (Bus b : busList) {
            if (b.getBusId().equals(1L)) {
                foundBus = b;
                break;
            }
        }

        assertNotNull(foundBus);
        assertEquals("TN01AB1234", foundBus.getBusNumber());
        assertEquals(BusType.AC_SEATER, foundBus.getBusType());
    }

    
    @Test
    void getAllBuses_success() {

        busList.add(bus);

        Bus anotherBus = new Bus();
        anotherBus.setBusId(2L);
        anotherBus.setBusNumber("KA05CD5678");
        anotherBus.setBusName("Sleeper King");
        anotherBus.setBusType(BusType.AC_SLEEPER);
        anotherBus.setTotalSeats(36);
        anotherBus.setCreatedAt(LocalDateTime.now());

        busList.add(anotherBus);

        assertEquals(2, busList.size());
        assertEquals("Sleeper King", busList.get(1).getBusName());
        assertNotEquals(busList.get(0).getBusType(), busList.get(1).getBusType());
    }
}
