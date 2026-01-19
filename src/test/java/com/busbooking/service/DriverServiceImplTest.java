package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.busbooking.dto.request.DriverRequest;
import com.busbooking.dto.response.DriverResponse;
import com.busbooking.entity.Driver;
import com.busbooking.enums.DriverStatus;
import com.busbooking.exception.DriverAlreadyExistsException;
import com.busbooking.exception.DriverNotFoundException;
import com.busbooking.repository.DriverRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private DriverRequest request;
    private Driver driver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new DriverRequest();
        request.setName("John Doe");
        request.setLicenseNumber("LIC12345");
        request.setPhoneNumber("9876543210");

        driver = new Driver();
        driver.setDriverId(1L);
        driver.setName("John Doe");
        driver.setLicenseNumber("LIC12345");
        driver.setPhoneNumber("9876543210");
        driver.setStatus(DriverStatus.ACTIVE);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setCreatedBy("admin@bus.com");
    }

    // ================= CREATE =================
    @Test
    void createDriver_success() {
        when(driverRepository.existsByLicenseNumber("LIC12345")).thenReturn(false);
        when(driverRepository.existsByPhoneNumber("9876543210")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        DriverResponse response = driverService.createDriver(request);

        assertNotNull(response);
        assertEquals(driver.getDriverId(), response.getDriverId());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriver_licenseAlreadyExists() {
        when(driverRepository.existsByLicenseNumber("LIC12345")).thenReturn(true);

        assertThrows(DriverAlreadyExistsException.class, () -> driverService.createDriver(request));
    }

    @Test
    void createDriver_phoneAlreadyExists() {
        when(driverRepository.existsByLicenseNumber("LIC12345")).thenReturn(false);
        when(driverRepository.existsByPhoneNumber("9876543210")).thenReturn(true);

        assertThrows(DriverAlreadyExistsException.class, () -> driverService.createDriver(request));
    }

    // ================= GET ALL =================
    @Test
    void getAllDrivers_success() {
        when(driverRepository.findAll()).thenReturn(List.of(driver));

        List<DriverResponse> drivers = driverService.getAllDrivers();

        assertEquals(1, drivers.size());
        assertEquals(driver.getDriverId(), drivers.get(0).getDriverId());
    }

    // ================= GET BY ID =================
    @Test
    void getDriverById_success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        DriverResponse response = driverService.getDriverById(1L);

        assertEquals(driver.getDriverId(), response.getDriverId());
    }

    @Test
    void getDriverById_notFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverById(1L));
    }

    // ================= GET BY LICENSE =================
    @Test
    void getDriverByLicenseNumber_success() {
        when(driverRepository.findByLicenseNumber("LIC12345")).thenReturn(Optional.of(driver));

        DriverResponse response = driverService.getDriverByLicenseNumber("LIC12345");

        assertEquals(driver.getLicenseNumber(), response.getLicenseNumber());
    }

    @Test
    void getDriverByLicenseNumber_notFound() {
        when(driverRepository.findByLicenseNumber("LIC12345")).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverByLicenseNumber("LIC12345"));
    }

    // ================= GET BY PHONE =================
    @Test
    void getDriverByPhoneNumber_success() {
        when(driverRepository.findByPhoneNumber("9876543210")).thenReturn(Optional.of(driver));

        DriverResponse response = driverService.getDriverByPhoneNumber("9876543210");

        assertEquals(driver.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    void getDriverByPhoneNumber_notFound() {
        when(driverRepository.findByPhoneNumber("9876543210")).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverByPhoneNumber("9876543210"));
    }

    // ================= UPDATE =================
    @Test
    void updateDriver_success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        request.setName("Jane Doe"); // updating name
        DriverResponse response = driverService.updateDriver(1L, request);

        assertEquals(driver.getDriverId(), response.getDriverId());
        assertEquals("Jane Doe", response.getName());
    }

    @Test
    void updateDriver_notFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver(1L, request));
    }

    // ================= DELETE =================
    @Test
    void deleteDriver_success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        driverService.deleteDriver(1L);

        verify(driverRepository, times(1)).delete(driver);
    }

    @Test
    void deleteDriver_notFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(1L));
    }
}
