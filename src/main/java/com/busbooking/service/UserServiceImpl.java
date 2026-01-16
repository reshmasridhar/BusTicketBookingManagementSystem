package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.entity.User;
import com.busbooking.enums.UserRole;
import com.busbooking.exception.EmailAlreadyExistsException;
import com.busbooking.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getEmail());

        return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
        userRepository.save(user);
	}
	

}
