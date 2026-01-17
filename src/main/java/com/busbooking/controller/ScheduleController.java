package com.busbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.service.ScheduleService;

@RestController
@RequestMapping("/api/admin/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleRequest request) {

        return new ResponseEntity<>(
                scheduleService.createSchedule(request),
                HttpStatus.CREATED);
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponse>> searchSchedules(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate journeyDate) {

        return ResponseEntity.ok(
                scheduleService.searchSchedules(
                        source, destination, journeyDate));
    }

    // ================= CANCEL =================
    @PutMapping("/{scheduleId}/cancel")
    public ResponseEntity<ScheduleResponse> cancelSchedule(
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                scheduleService.cancelSchedule(scheduleId));
    }
}
