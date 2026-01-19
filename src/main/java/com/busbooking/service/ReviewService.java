package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.request.ReviewRequest;
import com.busbooking.dto.response.ReviewResponse;

public interface ReviewService {

    ReviewResponse addReview(ReviewRequest request, String userEmail);

    List<ReviewResponse> getReviewsByBusId(Long busId);
}
