package com.busbooking.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.ForgotPasswordRequest;
import com.busbooking.dto.request.LoginRequest;
import com.busbooking.dto.request.UserSignupRequest;
import com.busbooking.dto.response.ForgotPasswordResponse;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.dto.response.LoginResponse;
import com.busbooking.dto.response.UserSignupResponse;
import com.busbooking.entity.User;
import com.busbooking.enums.UserRole;
import com.busbooking.exception.UserNotFoundException;
import com.busbooking.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	 @Autowired
	    private UserService userService;

	    
	    @PostMapping("/signup")
	    public ResponseEntity<UserSignupResponse> signup(
	            @RequestBody UserSignupRequest request) throws Exception {

	        User user = new User();
	        user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(request.getPassword());
	        user.setPhoneNumber(request.getPhoneNumber());
	        user.setRole(UserRole.PASSENGER);
	        user.setCreatedBy("SYSTEM");
	        user.setCreatedAt(LocalDateTime.now());

	        User savedUser = userService.registerUser(user);

	        UserSignupResponse response = new UserSignupResponse();
	        response.setMessage("User registered successfully");
	        response.setUserId(savedUser.getUserId());
	        response.setCreatedBy(savedUser.getCreatedBy());
	        response.setCreatedAt(savedUser.getCreatedAt());

	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }

	    
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponse> login(
	            @RequestBody LoginRequest request) {

	        Optional<User> optionalUser = userService.findByEmail(request.getEmail());

	        if (optionalUser.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new LoginResponse("Invalid email or password"));
	        }

	        User user = optionalUser.get();

	        if (!user.getPassword().equals(request.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new LoginResponse("Invalid email or password"));
	        }

	        return ResponseEntity.ok(
	                new LoginResponse("Login successful", user.getRole().name())
	        );
	    }
	    
	    @PostMapping("/forgot-password")
	    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
	            @RequestBody ForgotPasswordRequest request) {

	        User user = userService.findByEmail(request.getEmail())
	                .orElseThrow(() ->
	                        new UserNotFoundException(
	                                "User not found with email: " + request.getEmail()));

	        String tempPassword = "TMP#" +
	                UUID.randomUUID().toString().substring(0, 5);

	        user.setPassword(tempPassword);
	        user.setUpdatedBy(user.getEmail());
	        user.setUpdatedAt(LocalDateTime.now());

	        userService.updateUser(user);

	        ForgotPasswordResponse response = new ForgotPasswordResponse();
	        response.setMessage(
	                "Temporary password generated successfully. Please reset your password after login");
	        response.setTemporaryPassword(tempPassword);
	        response.setUpdatedAt(user.getUpdatedAt());

	        return ResponseEntity.ok(response);
	    }


	    
}
