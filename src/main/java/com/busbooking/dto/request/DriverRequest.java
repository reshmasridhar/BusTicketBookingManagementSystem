package com.busbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class DriverRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String phoneNumber;

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
