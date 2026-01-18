package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.request.DriverRequest;
import com.busbooking.dto.response.DriverResponse;

public interface DriverService {
	
	DriverResponse createDriver(DriverRequest request);

    List<DriverResponse> getAllDrivers();

    DriverResponse getDriverById(Long driverId);

    DriverResponse getDriverByLicenseNumber(String licenseNumber);

    DriverResponse getDriverByPhoneNumber(String phoneNumber);

    DriverResponse updateDriver(Long driverId, DriverRequest request);

    void deleteDriver(Long driverId);
	

}
