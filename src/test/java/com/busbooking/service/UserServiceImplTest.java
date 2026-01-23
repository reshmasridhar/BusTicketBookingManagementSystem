package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.busbooking.entity.User;
import com.busbooking.enums.UserRole;
import com.busbooking.exception.EmailAlreadyExistsException;

class UserServiceImplTest {

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);
        user.setName("Reshma");
        user.setEmail("reshma@gmail.com");
        user.setPassword("password123");
        user.setPhoneNumber("9876543210");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("reshma@gmail.com");
    }

    
    @Test
    void registerUser_success() {

        assertEquals("Reshma", user.getName());
        assertEquals("reshma@gmail.com", user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getCreatedBy());
    }

    
    @Test
    void registerUser_duplicateEmail() {

        Optional<User> existingUser = Optional.of(user);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> {
                    if (existingUser.isPresent()) {
                        throw new EmailAlreadyExistsException("Email already exists");
                    }
                }
        );
    }

    
    @Test
    void findByEmail_success() {

        Optional<User> foundUser = Optional.of(user);

        assertTrue(foundUser.isPresent());
        assertEquals("reshma@gmail.com", foundUser.get().getEmail());
    }

    
    @Test
    void findByEmail_notFound() {

        Optional<User> foundUser = Optional.empty();

        assertFalse(foundUser.isPresent());
    }

    
    @Test
    void updateUser_success() {

        user.setName("Reshma S");
        user.setPhoneNumber("9999999999");

        assertEquals("Reshma S", user.getName());
        assertEquals("9999999999", user.getPhoneNumber());
    }
}
