package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.response.SeatListResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;
import com.busbooking.enums.SeatType;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.exception.SeatNotFoundException;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.SeatRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    private Bus bus;
    private Seat seat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus = new Bus();
        bus.setBusId(1L);
        bus.setSeats(List.of());

        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setSeatType(SeatType.SEATER);
        seat.setSeatFare(100.0);
    }

    @Test
    void addSeat_success() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.save(seat)).thenReturn(seat);

        Seat result = seatService.addSeat(1L, seat);

        assertNotNull(result);
        assertEquals("A1", result.getSeatNumber());
        assertEquals(bus, result.getBus());
        verify(busRepository, times(1)).findById(1L);
        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    void addSeat_busNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> seatService.addSeat(1L, seat));
        verify(seatRepository, never()).save(any());
    }

    @Test
    void updateSeat_success() {
        seat.setBus(bus);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(seat);

        Seat updatedSeat = seatService.updateSeat(1L, 1L, 150.0);

        assertNotNull(updatedSeat);
        assertEquals(150.0, updatedSeat.getSeatFare());
        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    void updateSeat_busNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> seatService.updateSeat(1L, 1L, 150.0));
        verify(seatRepository, never()).save(any());
    }

    @Test
    void updateSeat_seatNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class, () -> seatService.updateSeat(1L, 1L, 150.0));
    }

    @Test
    void updateSeat_seatDoesNotBelongToBus() {
        Bus anotherBus = new Bus();
        anotherBus.setBusId(2L);

        seat.setBus(anotherBus);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        assertThrows(SeatNotFoundException.class, () -> seatService.updateSeat(1L, 1L, 150.0));
    }

    @Test
    void deleteSeat_success() {
        seat.setBus(bus);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        seatService.deleteSeat(1L, 1L);

        verify(seatRepository, times(1)).delete(seat);
    }

    @Test
    void deleteSeat_busNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> seatService.deleteSeat(1L, 1L));
        verify(seatRepository, never()).delete(any());
    }

    @Test
    void deleteSeat_seatNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class, () -> seatService.deleteSeat(1L, 1L));
        verify(seatRepository, never()).delete(any());
    }

    @Test
    void getSeatsByBus_success() {
        when(busRepository.existsById(1L)).thenReturn(true);
        when(seatRepository.findByBus_BusId(1L)).thenReturn(List.of(seat));

        List<Seat> result = seatService.getSeatsByBus(1L);

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getSeatNumber());
    }

    @Test
    void getSeatsByBus_busNotFound() {
        when(busRepository.existsById(1L)).thenReturn(false);

        assertThrows(BusNotFoundException.class, () -> seatService.getSeatsByBus(1L));
    }

    @Test
    void getSeatsByBusId_success() {
        bus.setSeats(List.of(seat));

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));

        List<SeatListResponse> responses = seatService.getSeatsByBusId(1L);

        assertEquals(1, responses.size());
        assertEquals("A1", responses.get(0).getSeatNumber());
    }

    @Test
    void getSeatsByBusId_busNotFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> seatService.getSeatsByBusId(1L));
    }
}
