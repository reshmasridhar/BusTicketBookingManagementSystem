package com.busbooking.service;

import java.util.Optional;

import com.busbooking.entity.User;

public interface UserService {
	
	User registerUser(User user);

    Optional<User> findByEmail(String email);

    void updateUser(User user);

}
