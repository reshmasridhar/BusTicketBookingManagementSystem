package com.busbooking.service;

import java.util.Optional;

import com.busbooking.entity.User;

public interface UserService {
	
	User registerUser(User user) throws Exception;
    Optional<User> findByEmail(String email);

}
