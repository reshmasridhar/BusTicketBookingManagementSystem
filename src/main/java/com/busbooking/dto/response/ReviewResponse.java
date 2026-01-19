package com.busbooking.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {
	
	 private Long reviewId;
	    private int rating;
	    private String review;
	    private String userName;
	    private LocalDateTime createdAt;
		public Long getReviewId() {
			return reviewId;
		}
		public void setReviewId(Long reviewId) {
			this.reviewId = reviewId;
		}
		public int getRating() {
			return rating;
		}
		public void setRating(int rating) {
			this.rating = rating;
		}
		public String getReview() {
			return review;
		}
		public void setReview(String review) {
			this.review = review;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

}
