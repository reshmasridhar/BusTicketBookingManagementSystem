package com.busbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
	
	@NotBlank
	private String email;

	@NotBlank
	private String oldPassword;
	
	@NotBlank
    private String newPassword;
	
	public ResetPasswordRequest() {
    }
	
	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    
    
}
