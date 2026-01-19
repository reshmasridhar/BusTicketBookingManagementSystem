package com.busbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.ScheduleRequest;
import com.busbooking.dto.response.ScheduleResponse;
import com.busbooking.service.ScheduleService;

@RestController
@RequestMapping("/api/admin/schedules")
public class ScheduleController {

    @Autowired ScheduleService scheduleService;

    // ================= CREATE =================
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleRequest request) {

        return new ResponseEntity<>(
                scheduleService.createSchedule(request),
                HttpStatus.CREATED);
    }

    // ================= GET ALL =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules());
    }

    // ================= SEARCH =================
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{scheduleId}/cancel")
    public ResponseEntity<ScheduleResponse> cancelSchedule(
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                scheduleService.cancelSchedule(scheduleId));
    }
}
