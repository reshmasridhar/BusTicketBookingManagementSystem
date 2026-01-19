package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public DriverResponse createDriver(DriverRequest request) {

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DriverAlreadyExistsException(
                    "Driver with this license number already exists");
        }

        if (driverRepository.existsByPhoneNumber(request.getPhoneNumber())) {
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

        return mapToResponse(saved);
    }

    @Override
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DriverResponse getDriverById(Long driverId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with id: " + driverId));

        return mapToResponse(driver);
    }

    //mapper
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

	@Override
	public DriverResponse getDriverByLicenseNumber(String licenseNumber) {
		// TODO Auto-generated method stub
		Driver driver = driverRepository
                .findByLicenseNumber(licenseNumber)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with license number: " + licenseNumber));

        return mapToResponse(driver);
	}

	@Override
	public DriverResponse getDriverByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		Driver driver = driverRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with phone number: " + phoneNumber));

        return mapToResponse(driver);
	}

	@Override
	public DriverResponse updateDriver(Long driverId, DriverRequest request) {
		// TODO Auto-generated method stub
		Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with id: " + driverId));

        driver.setName(request.getName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setPhoneNumber(request.getPhoneNumber());
        
        driver.setUpdatedAt(LocalDateTime.now());
        driver.setUpdatedBy("admin@bus.com");

        return mapToResponse(driverRepository.save(driver));
	}

	@Override
	public void deleteDriver(Long driverId) {
		// TODO Auto-generated method stub
		Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with id: " + driverId));

        driverRepository.delete(driver);
		
	}
}
