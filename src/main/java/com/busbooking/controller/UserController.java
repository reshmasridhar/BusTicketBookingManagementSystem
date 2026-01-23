package com.busbooking.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.ResetPasswordRequest;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.entity.User;
import com.busbooking.exception.UserNotFoundException;
import com.busbooking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));

        //Correct password check
        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse("Old password is incorrect"));
        }

        //Encrypt new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        user.setUpdatedBy(user.getEmail());
        user.setUpdatedAt(LocalDateTime.now());

        userService.updateUser(user);

        return ResponseEntity.ok(
                new GenericResponse(
                        "Password reset successfully",
                        user.getUpdatedBy(),
                        user.getUpdatedAt()
                )
        );
    }
}
