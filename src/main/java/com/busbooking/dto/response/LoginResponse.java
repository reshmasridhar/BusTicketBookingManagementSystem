package com.busbooking.dto.response;

public class LoginResponse {
	
	private String message;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(String message, String role) {
        this.message = message;
        this.role = role;
    }
    
    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
