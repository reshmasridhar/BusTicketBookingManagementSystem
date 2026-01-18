package com.busbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.busbooking.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
	boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByPhoneNumber(String phoneNumber);
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);

    Optional<Driver> findByPhoneNumber(String phoneNumber);
}
