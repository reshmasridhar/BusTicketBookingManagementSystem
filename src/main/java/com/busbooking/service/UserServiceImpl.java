package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.entity.User;
import com.busbooking.enums.UserRole;
import com.busbooking.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	

	@Override
	public User registerUser(User user) throws Exception {
		// TODO Auto-generated method stub
		if(userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        user.setRole(UserRole.PASSENGER);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getEmail()); 
        return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

}
