package com.example.travel.planner.service.impl;

import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    void loadUserByUsername_ShouldReturnUser() {
        String email = "email";
        String password = "password";

        User user = new User();
        user.setId("123");
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername(email);

        assertEquals(email, result.getUsername());
        assertEquals(password, result.getPassword());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldThrowNotFoundException(){
        String email = "404";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userDetailService.loadUserByUsername(email));

        verify(userRepository).findByEmail(email);
    }

}
