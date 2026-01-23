package com.busbooking.controller;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.BusRequest;
import com.busbooking.dto.response.BusResponse;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.entity.Bus;
import com.busbooking.service.BusService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusService busService;

    // ================= ADMIN =================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BusResponse> addBus(@RequestBody @Valid BusRequest request) {

        Bus bus = new Bus();
        bus.setBusName(request.getBusName());
        bus.setBusNumber(request.getBusNumber());
        bus.setBusType(Enum.valueOf(com.busbooking.enums.BusType.class, request.getBusType()));
        bus.setTotalSeats(request.getTotalSeats());
        bus.setCreatedBy("ADMIN");
        bus.setCreatedAt(LocalDateTime.now());

        return new ResponseEntity<>(
                busService.addBus(bus),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{busId}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Long busId,
            @RequestBody @Valid BusRequest request) {

        Bus bus = new Bus();
        bus.setBusName(request.getBusName());
        bus.setBusNumber(request.getBusNumber());
        bus.setBusType(Enum.valueOf(com.busbooking.enums.BusType.class, request.getBusType()));
        bus.setTotalSeats(request.getTotalSeats());
        bus.setUpdatedBy("ADMIN");

        return ResponseEntity.ok(
                busService.updateBus(busId, bus)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{busId}")
    public ResponseEntity<GenericResponse> deleteBus(@PathVariable Long busId) {

        busService.deleteBus(busId);

        return ResponseEntity.ok(
                new GenericResponse("Bus deleted successfully")
        );
    }

    // ================= USER + ADMIN =================

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{busId}")
    public ResponseEntity<BusResponse> getBusById(@PathVariable Long busId) {
        return ResponseEntity.ok(busService.getBusById(busId));
    }
}
