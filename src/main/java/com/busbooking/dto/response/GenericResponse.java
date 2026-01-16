package com.busbooking.dto.response;

import java.time.LocalDateTime;

public class GenericResponse {
	
	private String message;
    private String updatedBy;
    private LocalDateTime updatedAt;
    
    public GenericResponse() {
    }

    public GenericResponse(String message) {
        this.message = message;
    }
    
    public GenericResponse(String message, String updatedBy, LocalDateTime updatedAt) {
        this.message = message;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public String getMessage() {
        return message;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    

}
