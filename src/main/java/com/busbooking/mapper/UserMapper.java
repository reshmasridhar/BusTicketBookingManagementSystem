package com.busbooking.mapper;

import java.time.LocalDateTime;

import com.busbooking.dto.request.UserSignupRequest;
import com.busbooking.entity.User;
import com.busbooking.enums.UserRole;

public class UserMapper {
	
	private UserMapper() {
        // prevent instantiation
    }

    public static User toEntity(UserSignupRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(UserRole.USER);
        user.setCreatedBy("SYSTEM");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

}
