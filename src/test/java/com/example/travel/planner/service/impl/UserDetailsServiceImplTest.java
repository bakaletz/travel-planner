package com.example.travel.planner.service.impl;

import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    void loadUserByUsername_ShouldReturnUser() {
        User testUser = TestUserProvider.createDefaultUser();

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userDetailService.loadUserByUsername(testUser.getEmail()));
    }

    @Test
    void loadUserByUsername_ShouldThrowNotFoundException() {
        String email = "404";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userDetailService.loadUserByUsername(email));
    }

}
