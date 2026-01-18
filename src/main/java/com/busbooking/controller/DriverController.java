package com.busbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.DriverRequest;
import com.busbooking.dto.response.DriverResponse;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.service.DriverService;

@RestController
@RequestMapping("/api/admin/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(
            @RequestBody DriverRequest request) {

        return new ResponseEntity<>(
                driverService.createDriver(request),
                HttpStatus.CREATED);
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // ================= GET BY ID =================
    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponse> getDriverById(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                driverService.getDriverById(driverId));
    }

    // ================= GET BY LICENSE =================
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DriverResponse> getByLicense(
            @PathVariable String licenseNumber) {

        return ResponseEntity.ok(
                driverService.getDriverByLicenseNumber(licenseNumber));
    }

    // ================= GET BY PHONE =================
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<DriverResponse> getByPhone(
            @PathVariable String phoneNumber) {

        return ResponseEntity.ok(
                driverService.getDriverByPhoneNumber(phoneNumber));
    }

    // ================= UPDATE =================
    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long driverId,
            @RequestBody DriverRequest request) {

        return ResponseEntity.ok(
                driverService.updateDriver(driverId, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{driverId}")
    public ResponseEntity<GenericResponse> deleteDriver(
            @PathVariable Long driverId) {

        driverService.deleteDriver(driverId);

        return ResponseEntity.ok(
                new GenericResponse("Driver deleted successfully"));
    }
}
