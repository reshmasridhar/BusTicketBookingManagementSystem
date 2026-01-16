package com.busbooking.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.ResetPasswordRequest;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.entity.User;
import com.busbooking.exception.UserNotFoundException;
import com.busbooking.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse("Old password is incorrect"));
        }

        user.setPassword(request.getNewPassword());
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
