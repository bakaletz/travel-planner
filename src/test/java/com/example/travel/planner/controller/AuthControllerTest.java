package com.example.travel.planner.controller;

import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.service.AuthService;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginUser_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginRequestDTO validLogin = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("testest")
                .build();

        authController.loginUser(validLogin);

        verify(authService).authenticateUser(any(LoginRequestDTO.class));
    }

    @Test
    void registerUser_ShouldReturnCreatedUser_WhenDataIsValid() {
        UserRegisterDTO testUserRegisterDTO = TestUserProvider.createUserRegisterDTO();

        authController.registerUser(testUserRegisterDTO);

        verify(authService).registerUser(any(UserRegisterDTO.class));
    }

}


