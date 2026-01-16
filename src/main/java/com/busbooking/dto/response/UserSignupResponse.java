package com.busbooking.dto.response;

import java.time.LocalDateTime;

public class UserSignupResponse {
	
	private String message;
    private Long userId;
    private String createdBy;
    private LocalDateTime createdAt;
    
    public UserSignupResponse()
    {
    	
    }
	public UserSignupResponse(String message, Long userId, String createdBy, LocalDateTime createdAt) {
		
		this.message = message;
		this.userId = userId;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
	}
	
	 public String getMessage() {
	        return message;
	    }
	    public void setMessage(String message) {
	        this.message = message;
	    }
	    public Long getUserId() {
	        return userId;
	    }
	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }
	    public String getCreatedBy() {
	        return createdBy;
	    }
	    public void setCreatedBy(String createdBy) {
	        this.createdBy = createdBy;
	    }
	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }
	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
    
    

}
