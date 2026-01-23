package com.busbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.DriverRequest;
import com.busbooking.dto.response.DriverResponse;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.service.DriverService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/drivers")
@PreAuthorize("hasRole('ADMIN')")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(
            @RequestBody @Valid DriverRequest request) {

        return new ResponseEntity<>(
                driverService.createDriver(request),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

   
    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponse> getDriverById(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                driverService.getDriverById(driverId));
    }

   
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DriverResponse> getByLicense(
            @PathVariable String licenseNumber) {

        return ResponseEntity.ok(
                driverService.getDriverByLicenseNumber(licenseNumber));
    }

  
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<DriverResponse> getByPhone(
            @PathVariable String phoneNumber) {

        return ResponseEntity.ok(
                driverService.getDriverByPhoneNumber(phoneNumber));
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long driverId,
            @RequestBody @Valid DriverRequest request) {

        return ResponseEntity.ok(
                driverService.updateDriver(driverId, request));
    }

   
    @DeleteMapping("/{driverId}")
    public ResponseEntity<GenericResponse> deleteDriver(
            @PathVariable Long driverId) {

        driverService.deleteDriver(driverId);

        return ResponseEntity.ok(
                new GenericResponse("Driver deleted successfully"));
    }
}
