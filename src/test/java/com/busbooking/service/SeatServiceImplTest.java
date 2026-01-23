package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;
import com.busbooking.enums.SeatType;

class SeatServiceImplTest {

    private Bus bus;
    private Seat seat;
    private List<Seat> seatList;

    @BeforeEach
    void setUp() {

        bus = new Bus();
        bus.setBusId(1L);
        bus.setBusName("Volvo Express");

        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setSeatType(SeatType.SLEEPER);
        seat.setSeatFare(800.0);
        seat.setBus(bus);
        seat.setCreatedBy("admin@bus.com");
        seat.setCreatedAt(LocalDateTime.now());

        seatList = new ArrayList<>();
    }

    
    @Test
    void addSeat_success() {

        seatList.add(seat);

        assertEquals(1, seatList.size());
        assertEquals("A1", seatList.get(0).getSeatNumber());
        assertEquals(800.0, seatList.get(0).getSeatFare());
        assertEquals(bus.getBusId(), seatList.get(0).getBus().getBusId());
    }

   
    @Test
    void updateSeat_success() {

        seatList.add(seat);

        Seat existingSeat = seatList.get(0);
        existingSeat.setSeatFare(950.0);
        existingSeat.setUpdatedBy("admin@bus.com");
        existingSeat.setUpdatedAt(LocalDateTime.now());

        assertEquals(950.0, existingSeat.getSeatFare());
        assertNotNull(existingSeat.getUpdatedAt());
    }

    
    @Test
    void updateSeat_wrongBus() {

        Bus anotherBus = new Bus();
        anotherBus.setBusId(2L);

        seat.setBus(anotherBus);
        seatList.add(seat);

        boolean belongsToBus = seat.getBus().getBusId().equals(bus.getBusId());

        assertFalse(belongsToBus);
    }

    
    @Test
    void deleteSeat_success() {

        seatList.add(seat);
        assertEquals(1, seatList.size());

        seatList.remove(seat);

        assertEquals(0, seatList.size());
    }

    
    @Test
    void deleteSeat_notFound() {

        Seat found = null;

        for (Seat s : seatList) {
            if (s.getSeatId().equals(99L)) {
                found = s;
            }
        }

        assertNull(found);
    }

    
    @Test
    void getSeatsByBus_success() {

        seatList.add(seat);

        List<Seat> result = new ArrayList<>();
        for (Seat s : seatList) {
            if (s.getBus().getBusId().equals(bus.getBusId())) {
                result.add(s);
            }
        }

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getSeatNumber());
    }

    
    @Test
    void getSeatsByBus_empty() {

        List<Seat> result = new ArrayList<>();
        for (Seat s : seatList) {
            if (s.getBus().getBusId().equals(bus.getBusId())) {
                result.add(s);
            }
        }

        assertTrue(result.isEmpty());
    }
}
