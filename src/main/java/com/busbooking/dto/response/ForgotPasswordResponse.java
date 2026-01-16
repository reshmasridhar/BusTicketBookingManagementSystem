package com.busbooking.dto.response;

import java.time.LocalDateTime;

public class ForgotPasswordResponse {
	
	    private String message;
	    private String temporaryPassword;
	    private LocalDateTime updatedAt;
	    
	    public String getMessage() {
	        return message;
	    }
	    public void setMessage(String message) {
	        this.message = message;
	    }
	    public String getTemporaryPassword() {
	        return temporaryPassword;
	    }
	    public void setTemporaryPassword(String temporaryPassword) {
	        this.temporaryPassword = temporaryPassword;
	    }
	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }
	    public void setUpdatedAt(LocalDateTime updatedAt) {
	        this.updatedAt = updatedAt;
	    }

}
