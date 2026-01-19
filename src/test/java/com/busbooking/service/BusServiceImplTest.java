package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.entity.Bus;
import com.busbooking.enums.BusType;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.repository.BusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusServiceImpl busService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBus_success() {
        Bus bus = new Bus();
        bus.setBusName("Test Bus");
        bus.setBusNumber("BUS123");
        bus.setBusType(BusType.AC_SEATER);
        bus.setTotalSeats(40);
        bus.setSeats(List.of()); // ⭐ IMPORTANT

        Bus savedBus = new Bus();
        savedBus.setBusId(1L);
        savedBus.setBusName(bus.getBusName());
        savedBus.setBusNumber(bus.getBusNumber());
        savedBus.setBusType(bus.getBusType());
        savedBus.setTotalSeats(bus.getTotalSeats());
        savedBus.setAvailableSeats(bus.getTotalSeats());
        savedBus.setSeats(bus.getSeats());

        when(busRepository.save(bus)).thenReturn(savedBus);

        BusResponse response = busService.addBus(bus);

        assertNotNull(response);
        assertEquals("Test Bus", response.getBusName());
        assertEquals(BusType.AC_SEATER, response.getBusType());
        verify(busRepository, times(1)).save(bus);
    }

    @Test
    void updateBus_success() {
        Bus existingBus = new Bus();
        existingBus.setBusId(1L);
        existingBus.setBusName("Old Bus");
        existingBus.setBusType(BusType.NAC_SEATER);
        existingBus.setTotalSeats(30);
        existingBus.setAvailableSeats(30);
        existingBus.setSeats(List.of()); // ⭐ FIX

        Bus updatedBus = new Bus();
        updatedBus.setBusName("Updated Bus");
        updatedBus.setBusType(BusType.AC_SEATER);
        updatedBus.setTotalSeats(45);
        updatedBus.setAvailableSeats(45);
        updatedBus.setUpdatedBy("ADMIN");
        updatedBus.setSeats(List.of()); // ⭐ FIX

        when(busRepository.findById(1L)).thenReturn(Optional.of(existingBus));
        when(busRepository.save(existingBus)).thenReturn(existingBus);

        BusResponse response = busService.updateBus(1L, updatedBus);

        assertNotNull(response);
        assertEquals("Updated Bus", response.getBusName());
        assertEquals(BusType.AC_SEATER, response.getBusType());
        verify(busRepository, times(1)).findById(1L);
        verify(busRepository, times(1)).save(existingBus);
    }

    @Test
    void updateBus_notFound() {
        Bus updatedBus = new Bus();
        updatedBus.setBusType(BusType.AC_SEATER);

        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> busService.updateBus(1L, updatedBus));
        verify(busRepository, times(1)).findById(1L);
        verify(busRepository, never()).save(any());
    }

    @Test
    void deleteBus_success() {
        Bus bus = new Bus();
        bus.setBusId(1L);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));

        busService.deleteBus(1L);

        verify(busRepository, times(1)).findById(1L);
        verify(busRepository, times(1)).delete(bus);
    }

    @Test
    void deleteBus_notFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> busService.deleteBus(1L));
        verify(busRepository, times(1)).findById(1L);
        verify(busRepository, never()).delete(any());
    }

    @Test
    void getBusById_success() {
        Bus bus = new Bus();
        bus.setBusId(1L);
        bus.setBusName("Test Bus");
        bus.setBusType(BusType.AC_SEATER);
        bus.setTotalSeats(40);
        bus.setAvailableSeats(40);
        bus.setSeats(List.of()); // ⭐ FIX

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));

        BusResponse response = busService.getBusById(1L);

        assertNotNull(response);
        assertEquals("Test Bus", response.getBusName());
        assertEquals(BusType.AC_SEATER, response.getBusType());
        verify(busRepository, times(1)).findById(1L);
    }

    @Test
    void getBusById_notFound() {
        when(busRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusNotFoundException.class, () -> busService.getBusById(1L));
        verify(busRepository, times(1)).findById(1L);
    }

    @Test
    void getAllBuses_success() {
        Bus bus1 = new Bus();
        bus1.setBusId(1L);
        bus1.setBusName("Bus One");
        bus1.setBusType(BusType.AC_SEATER);
        bus1.setTotalSeats(40);
        bus1.setAvailableSeats(40);
        bus1.setSeats(List.of()); // ⭐ FIX

        Bus bus2 = new Bus();
        bus2.setBusId(2L);
        bus2.setBusName("Bus Two");
        bus2.setBusType(BusType.NAC_SEATER);
        bus2.setTotalSeats(30);
        bus2.setAvailableSeats(30);
        bus2.setSeats(List.of()); // ⭐ FIX

        when(busRepository.findAll()).thenReturn(List.of(bus1, bus2));

        List<BusResponse> responseList = busService.getAllBuses();

        assertEquals(2, responseList.size());
        verify(busRepository, times(1)).findAll();
    }
}
