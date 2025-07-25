package com.example.travel.planner.service.impl;

import com.example.travel.planner.constant.ApplicationConstants;
import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.authentication.LoginResponseDTO;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Environment env;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDTO loginRequestDTO;
    private User user;
    private Authentication authentication;

    private UserRegisterDTO userRegisterDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setup() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password");

        user = new User();
        user.setId("user-1");
        user.setEmail("test@example.com");

        authentication = mock(Authentication.class);

        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("new@example.com");
        userRegisterDTO.setPassword("password");

        userDTO = new UserDTO();
        userDTO.setEmail("new@example.com");
    }

    @Test
    void authenticateAndGenerateToken_shouldReturnJwtToken_whenAuthenticationSuccessful() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(env.getProperty(eq(ApplicationConstants.JWT_SECRET_KEY), eq(ApplicationConstants.JWT_SECRET_DEFAULT_VALUE)))
                .thenReturn("12345678901234567890123456789012");


        LoginResponseDTO response = authService.authenticateAndGenerateToken(loginRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatus());
        assertTrue(response.getJwtToken().startsWith("Bearer "));

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail("test@example.com");
        verify(env).getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
    }

    @Test
    void authenticateAndGenerateToken_shouldThrow_whenUserNotFound() {

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authService.authenticateAndGenerateToken(loginRequestDTO));

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail("test@example.com");
    }


    @Test
    void registerUser_shouldSaveUser_whenEmailNotExists() {
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userRegisterDTO)).thenReturn(user);
        when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = authService.registerUser(userRegisterDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());

        verify(userRepository).existsByEmail(userRegisterDTO.getEmail());
        verify(userMapper).toEntity(userRegisterDTO);
        verify(passwordEncoder).encode(userRegisterDTO.getPassword());
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void registerUser_shouldThrow_whenEmailExists() {
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.registerUser(userRegisterDTO));

        verify(userRepository).existsByEmail(userRegisterDTO.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}
