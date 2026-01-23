package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.entity.User;
import com.busbooking.exception.EmailAlreadyExistsException;
import com.busbooking.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    
    @Override
    public User registerUser(User user) {
        logger.info("Registering user with email: {}", user.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Email already exists: {}", user.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getEmail());

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with userId: {}", savedUser.getUserId());

        return savedUser;
    }

    
    @Override
    public Optional<User> findByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            logger.info("User found with email: {}", email);
        } else {
            logger.warn("No user found with email: {}", email);
        }
        return userOpt;
    }

    
    @Override
    public User updateUser(User user) {
        logger.info("Updating user with userId: {}", user.getUserId());
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with userId: {}", updatedUser.getUserId());
        return updatedUser;
    }
}
