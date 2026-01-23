package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.request.DriverRequest;
import com.busbooking.dto.response.DriverResponse;
import com.busbooking.entity.Driver;
import com.busbooking.enums.DriverStatus;
import com.busbooking.exception.DriverAlreadyExistsException;
import com.busbooking.exception.DriverNotFoundException;
import com.busbooking.repository.DriverRepository;

@Service
public class DriverServiceImpl implements DriverService {

    private static final Logger logger =
            LoggerFactory.getLogger(DriverServiceImpl.class);

    @Autowired
    private DriverRepository driverRepository;

    
    @Override
    public DriverResponse createDriver(DriverRequest request) {

        logger.info("Creating driver with licenseNumber: {}", request.getLicenseNumber());

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            logger.warn("Driver already exists with licenseNumber: {}", request.getLicenseNumber());
            throw new DriverAlreadyExistsException(
                    "Driver with this license number already exists");
        }

        if (driverRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            logger.warn("Driver already exists with phoneNumber: {}", request.getPhoneNumber());
            throw new DriverAlreadyExistsException(
                    "Driver with this phone number already exists");
        }

        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setStatus(DriverStatus.ACTIVE);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setCreatedBy("admin@bus.com");

        Driver saved = driverRepository.save(driver);

        logger.info("Driver created successfully with driverId: {}", saved.getDriverId());

        return mapToResponse(saved);
    }

    
    @Override
    public List<DriverResponse> getAllDrivers() {

        logger.info("Fetching all drivers");

        List<DriverResponse> drivers = driverRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Total drivers found: {}", drivers.size());

        return drivers;
    }

    
    @Override
    public DriverResponse getDriverById(Long driverId) {

        logger.info("Fetching driver with id: {}", driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> {
                    logger.warn("Driver not found with id: {}", driverId);
                    return new DriverNotFoundException(
                            "Driver not found with id: " + driverId);
                });

        return mapToResponse(driver);
    }

    
    @Override
    public DriverResponse getDriverByLicenseNumber(String licenseNumber) {

        logger.info("Fetching driver with licenseNumber: {}", licenseNumber);

        Driver driver = driverRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> {
                    logger.warn("Driver not found with licenseNumber: {}", licenseNumber);
                    return new DriverNotFoundException(
                            "Driver not found with license number: " + licenseNumber);
                });

        return mapToResponse(driver);
    }

    
    @Override
    public DriverResponse getDriverByPhoneNumber(String phoneNumber) {

        logger.info("Fetching driver with phoneNumber: {}", phoneNumber);

        Driver driver = driverRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    logger.warn("Driver not found with phoneNumber: {}", phoneNumber);
                    return new DriverNotFoundException(
                            "Driver not found with phone number: " + phoneNumber);
                });

        return mapToResponse(driver);
    }

    
    @Override
    public DriverResponse updateDriver(Long driverId, DriverRequest request) {

        logger.info("Updating driver with id: {}", driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> {
                    logger.warn("Driver not found with id: {}", driverId);
                    return new DriverNotFoundException(
                            "Driver not found with id: " + driverId);
                });

        driver.setName(request.getName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setUpdatedAt(LocalDateTime.now());
        driver.setUpdatedBy("admin@bus.com");

        Driver updated = driverRepository.save(driver);

        logger.info("Driver updated successfully with id: {}", driverId);

        return mapToResponse(updated);
    }

   
    @Override
    public void deleteDriver(Long driverId) {

        logger.info("Deleting driver with id: {}", driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> {
                    logger.warn("Driver not found with id: {}", driverId);
                    return new DriverNotFoundException(
                            "Driver not found with id: " + driverId);
                });

        driverRepository.delete(driver);

        logger.info("Driver deleted successfully with id: {}", driverId);
    }

    
    private DriverResponse mapToResponse(Driver driver) {

        DriverResponse response = new DriverResponse();
        response.setDriverId(driver.getDriverId());
        response.setName(driver.getName());
        response.setLicenseNumber(driver.getLicenseNumber());
        response.setPhoneNumber(driver.getPhoneNumber());
        response.setStatus(driver.getStatus());
        response.setUpdatedBy(driver.getUpdatedBy());
        response.setUpdatedAt(driver.getUpdatedAt());

        return response;
    }
}
