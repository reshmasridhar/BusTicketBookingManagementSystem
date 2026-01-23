package com.busbooking.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.request.ReviewRequest;
import com.busbooking.dto.response.ReviewResponse;
import com.busbooking.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody @Valid ReviewRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                reviewService.addReview(request, userEmail));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByBus(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByBusId(busId)
        );
    }
}
