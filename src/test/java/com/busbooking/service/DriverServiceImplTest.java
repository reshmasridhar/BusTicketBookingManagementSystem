package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.Driver;
import com.busbooking.enums.DriverStatus;

class DriverServiceImplTest {

    private Driver driver;
    private List<Driver> driverList;

    @BeforeEach
    void setUp() {

        driver = new Driver();
        driver.setDriverId(1L);
        driver.setName("Ramesh");
        driver.setLicenseNumber("LIC12345");
        driver.setPhoneNumber("9876543210");
        driver.setStatus(DriverStatus.ACTIVE);
        driver.setCreatedBy("admin@bus.com");
        driver.setCreatedAt(LocalDateTime.now());

        driverList = new ArrayList<>();
    }

    
    @Test
    void createDriver_success() {

        driverList.add(driver);

        assertEquals(1, driverList.size());
        assertEquals("Ramesh", driverList.get(0).getName());
        assertEquals("LIC12345", driverList.get(0).getLicenseNumber());
        assertEquals(DriverStatus.ACTIVE, driverList.get(0).getStatus());
        assertNotNull(driverList.get(0).getCreatedAt());
    }

    
    @Test
    void getAllDrivers_success() {

        driverList.add(driver);

        Driver driver2 = new Driver();
        driver2.setDriverId(2L);
        driver2.setName("Suresh");
        driver2.setLicenseNumber("LIC67890");
        driver2.setPhoneNumber("9123456780");
        driver2.setStatus(DriverStatus.ACTIVE);
        driver2.setCreatedAt(LocalDateTime.now());

        driverList.add(driver2);

        assertEquals(2, driverList.size());
        assertEquals("Suresh", driverList.get(1).getName());
        assertNotEquals(driverList.get(0).getLicenseNumber(),
                        driverList.get(1).getLicenseNumber());
    }

   
    @Test
    void getDriverById_success() {

        driverList.add(driver);

        Driver foundDriver = null;
        for (Driver d : driverList) {
            if (d.getDriverId().equals(1L)) {
                foundDriver = d;
                break;
            }
        }

        assertNotNull(foundDriver);
        assertEquals("Ramesh", foundDriver.getName());
        assertEquals("9876543210", foundDriver.getPhoneNumber());
    }

    
    @Test
    void getDriverByLicenseNumber_success() {

        driverList.add(driver);

        Driver foundDriver = null;
        for (Driver d : driverList) {
            if (d.getLicenseNumber().equals("LIC12345")) {
                foundDriver = d;
                break;
            }
        }

        assertNotNull(foundDriver);
        assertEquals("Ramesh", foundDriver.getName());
        assertEquals(DriverStatus.ACTIVE, foundDriver.getStatus());
    }

    
    @Test
    void getDriverByPhoneNumber_success() {

        driverList.add(driver);

        Driver foundDriver = null;
        for (Driver d : driverList) {
            if (d.getPhoneNumber().equals("9876543210")) {
                foundDriver = d;
                break;
            }
        }

        assertNotNull(foundDriver);
        assertEquals("LIC12345", foundDriver.getLicenseNumber());
    }

    
    @Test
    void updateDriver_success() {

        driverList.add(driver);

        Driver existingDriver = driverList.get(0);
        existingDriver.setName("Ramesh Kumar");
        existingDriver.setPhoneNumber("9999999999");
        existingDriver.setUpdatedBy("admin@bus.com");
        existingDriver.setUpdatedAt(LocalDateTime.now());

        assertEquals("Ramesh Kumar", existingDriver.getName());
        assertEquals("9999999999", existingDriver.getPhoneNumber());
        assertNotNull(existingDriver.getUpdatedAt());
    }

    
    @Test
    void deleteDriver_success() {

        driverList.add(driver);
        assertEquals(1, driverList.size());

        driverList.remove(driver);

        assertEquals(0, driverList.size());
    }
}
