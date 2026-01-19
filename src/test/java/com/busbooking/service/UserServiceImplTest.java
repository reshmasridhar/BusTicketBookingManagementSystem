package com.busbooking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.busbooking.entity.User;
import com.busbooking.exception.EmailAlreadyExistsException;
import com.busbooking.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    
    @Mock
    private UserRepository userRepository;

    // Real service but with mocked repository injected
    @InjectMocks
    private UserServiceImpl userService;

    
    // Test: registerUser - SUCCESS

    @Test
    void registerUser_success() {

        User user = new User();
        user.setEmail("test@gmail.com");

        // Email does NOT exist
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        // Save user
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);
        verify(userRepository).save(user);
    }

    
    // Test: registerUser - Email Already Exists
  
    @Test
    void registerUser_emailAlreadyExists() {

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.registerUser(user)
        );

        verify(userRepository, never()).save(any());
    }

   
    // Test: findByEmail
   
    @Test
    void findByEmail_success() {

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("test@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("test@gmail.com", result.get().getEmail());
    }

    
    // Test: updateUser
    
    @Test
    void updateUser_success() {

        User user = new User();
        user.setEmail("update@gmail.com");

        when(userRepository.save(user))
                .thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("update@gmail.com", updatedUser.getEmail());
    }
}
