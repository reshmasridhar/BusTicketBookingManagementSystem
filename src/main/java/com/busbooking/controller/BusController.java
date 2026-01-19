package com.busbooking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.entity.Bus;
import com.busbooking.service.BusService;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusService busService;

    // ADMIN
    @PostMapping
    public ResponseEntity<BusResponse> addBus(@RequestBody Bus bus) {

        bus.setCreatedBy("ADMIN");
        bus.setCreatedAt(LocalDateTime.now());

        BusResponse response = busService.addBus(bus);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ADMIN
    @PutMapping("/{busId}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Long busId,
            @RequestBody Bus bus) {

        bus.setUpdatedBy("ADMIN");

        BusResponse response = busService.updateBus(busId, bus);

        return ResponseEntity.ok(response);
    }

    // ADMIN
    @DeleteMapping("/{busId}")
    public ResponseEntity<GenericResponse> deleteBus(@PathVariable Long busId) {

        busService.deleteBus(busId);

        return ResponseEntity.ok(
                new GenericResponse("Bus deleted successfully")
        );
    }


    // USER
    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {

        return ResponseEntity.ok(busService.getAllBuses());
    }

    // USER
    @GetMapping("/{busId}")
    public ResponseEntity<BusResponse> getBusById(
            @PathVariable Long busId) {

        return ResponseEntity.ok(busService.getBusById(busId));
    }
}