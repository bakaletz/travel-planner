package com.example.travel.planner.service.impl;

import com.example.travel.planner.constant.JWTConstants;
import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
    @Mock
    private Authentication authenticationResponse;
    @Mock
    private JWTConstants jwtConstants;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDTO loginRequestDTO;
    private User user;
    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    void setup() {
        loginRequestDTO = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("password")
                .build();
        user = TestUserProvider.createDefaultUser();
        userRegisterDTO = TestUserProvider.createUserRegisterDTO();
    }

    @Test
    void authenticateUser_whenAuthenticationSuccessful() {

        String secret = "my-secret-key-my-secret-key-my-secret-key12";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(env.getProperty(eq("jwt.secret.key"), anyString())).thenReturn(secret);
        when(jwtConstants.getSecretKey()).thenReturn("jwt.secret.key");
        when(jwtConstants.getSecretDefaultValue()).thenReturn("Default");

        assertDoesNotThrow(() -> authService.authenticateUser(loginRequestDTO));

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void authenticateUser_shouldThrowBadCredentialsException_whenDataIsInvalid(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,() -> authService.authenticateUser(loginRequestDTO));

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void registerUser_shouldSaveUser_whenEmailNotExists() {
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(false);

        assertDoesNotThrow(() -> authService.registerUser(userRegisterDTO));

        verify(userMapper).toEntity(userRegisterDTO, passwordEncoder);
        verify(userRepository).save(any());
        verify(userMapper).toDto(any());
    }

    @Test
    void registerUser_shouldThrow_whenEmailExists() {
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.registerUser(userRegisterDTO));

        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}
