package com.example.travel.planner.controller;

import com.example.travel.planner.NoSecurityFilterConfig;
import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.authentication.LoginResponseDTO;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(NoSecurityFilterConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void loginUser_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password123");

        LoginResponseDTO responseDTO = new LoginResponseDTO("OK", "Bearer some.jwt.token");

        when(authService.authenticateAndGenerateToken(any(LoginRequestDTO.class))).thenReturn(responseDTO);


        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.jwtToken").value("Bearer some.jwt.token"));

        verify(authService, times(1)).authenticateAndGenerateToken(any(LoginRequestDTO.class));
    }

    @Test
    void loginUser_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("wrongpassword");

        when(authService.authenticateAndGenerateToken(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());

        verify(authService, times(1)).authenticateAndGenerateToken(any(LoginRequestDTO.class));
    }

    @Test
    void loginUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("nonexistent@example.com");
        requestDTO.setPassword("password123");

        when(authService.authenticateAndGenerateToken(any(LoginRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("User","email","nonexistent@example.com"));


        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());

        verify(authService, times(1)).authenticateAndGenerateToken(any(LoginRequestDTO.class));
    }



    @Test
    void registerUser_ShouldReturnCreatedUser_WhenDataIsValid() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setEmail("newuser@example.com");
        registerDTO.setUsername("Test");
        registerDTO.setPassword("password123");
        registerDTO.setFirstName("John");


        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Test");
        userDTO.setId("123");
        userDTO.setEmail("newuser@example.com");
        userDTO.setFirstName("John");

        when(authService.registerUser(any(UserRegisterDTO.class))).thenReturn(userDTO);


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath(".username").value("Test"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(authService, times(1)).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_ShouldReturnConflict_WhenUserAlreadyExists() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setEmail("existing@example.com");
        registerDTO.setUsername("Test");
        registerDTO.setPassword("password123");
        registerDTO.setFirstName("John");

        when(authService.registerUser(any(UserRegisterDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("User", "email", "existing@example.com"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict());

        verify(authService, times(1)).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setEmail("invalid-email");
        registerDTO.setPassword("password123");
        registerDTO.setFirstName("John");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenRequiredFieldsAreMissing() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenPasswordIsTooShort() throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("123");
        registerDTO.setFirstName("John");


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenRequestBodyIsEmpty() throws Exception {

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(UserRegisterDTO.class));
    }

}