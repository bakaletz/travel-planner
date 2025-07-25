package com.example.travel.planner.controller;

import com.example.travel.planner.NoSecurityFilterConfig;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(NoSecurityFilterConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO testUserDTO;
    private UserRegisterDTO testUserRegisterDTO;
    private UserUpdateDTO testUserUpdateDTO;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setId("1");
        testUserDTO.setUsername("johndoe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setFirstName("John");

        testUserRegisterDTO = new UserRegisterDTO();
        testUserRegisterDTO.setUsername("johndoe");
        testUserRegisterDTO.setPassword("password123");
        testUserRegisterDTO.setEmail("john.doe@example.com");
        testUserRegisterDTO.setFirstName("John");

        testUserUpdateDTO = new UserUpdateDTO();
        testUserUpdateDTO.setUsername("janedoe");
        testUserUpdateDTO.setPassword("newpassword123");
        testUserUpdateDTO.setEmail("jane.doe@example.com");
        testUserUpdateDTO.setFirstName("Jane");
    }

    @Test
    void getUsers_ShouldReturnListOfUsers() throws Exception {

        List<UserDTO> users = Arrays.asList(testUserDTO);
        when(userService.findAll()).thenReturn(users);


        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void getUsers_ShouldReturnEmptyList_WhenNoUsersExist() throws Exception {

        when(userService.findAll()).thenReturn(Arrays.asList());


        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {

        String userId = "1";
        when(userService.findById(userId)).thenReturn(testUserDTO);


        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userService, times(1)).findById(userId);
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidInput() throws Exception {

        when(userService.createUser(any(UserRegisterDTO.class))).thenReturn(testUserDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userService, times(1)).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {

        UserRegisterDTO invalidUser = new UserRegisterDTO();
        invalidUser.setUsername("");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenUsernameTooShort() throws Exception {

        UserRegisterDTO invalidUser = new UserRegisterDTO();
        invalidUser.setUsername("ab");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenPasswordTooShort() throws Exception {

        UserRegisterDTO invalidUser = new UserRegisterDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("12345");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenEmailInvalid() throws Exception {

        UserRegisterDTO invalidUser = new UserRegisterDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("invalid-email");
        invalidUser.setFirstName("John");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenFirstNameIsBlank() throws Exception {

        UserRegisterDTO invalidUser = new UserRegisterDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("");


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenEmptyRequestBody() throws Exception {

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenValidInput() throws Exception {

        String userId = "1";
        UserDTO updatedUser = new UserDTO();
        updatedUser.setId("1");
        updatedUser.setUsername("janedoe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setFirstName("Jane");

        when(userService.updateUser(eq(userId), any(UserUpdateDTO.class))).thenReturn(updatedUser);


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("janedoe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));

        verify(userService, times(1)).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {

        String userId = "1";
        UserUpdateDTO invalidUser = new UserUpdateDTO();
        invalidUser.setUsername("");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenUsernameTooShort() throws Exception {

        String userId = "1";
        UserUpdateDTO invalidUser = new UserUpdateDTO();
        invalidUser.setUsername("ab"); // менше 3 символів
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenPasswordTooShort() throws Exception {

        String userId = "1";
        UserUpdateDTO invalidUser = new UserUpdateDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("12345");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("John");


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenEmailInvalid() throws Exception {

        String userId = "1";
        UserUpdateDTO invalidUser = new UserUpdateDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("invalid-email");
        invalidUser.setFirstName("John");


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenFirstNameIsBlank() throws Exception {

        String userId = "1";
        UserUpdateDTO invalidUser = new UserUpdateDTO();
        invalidUser.setUsername("johndoe");
        invalidUser.setPassword("password123");
        invalidUser.setEmail("test@example.com");
        invalidUser.setFirstName("");


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenEmptyRequestBody() throws Exception {

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyString(), any(UserUpdateDTO.class));
    }

    @Test
    void deleteUser_ShouldReturnSuccessMessage_WhenUserExists() throws Exception {

        String userId = "1";
        String expectedMessage = "User deleted successfully";
        when(userService.deleteUser(userId)).thenReturn(expectedMessage);


        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));

        verify(userService, times(1)).deleteUser(userId);
    }


    @Test
    void getUserById_WithSpecificId_ShouldCallServiceWithCorrectParameter() throws Exception {

        String userId = "test-user-123";
        when(userService.findById(userId)).thenReturn(testUserDTO);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).findById(userId);
    }

    @Test
    void createUser_WithNullBody_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void wrongHttpMethod_ShouldReturnMethodNotAllowed() throws Exception {

        mockMvc.perform(patch("/api/v1/users"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void createUser_ShouldAcceptValidUser_WithMinimumRequiredFields() throws Exception {

        UserRegisterDTO validUser = new UserRegisterDTO();
        validUser.setUsername("abc");
        validUser.setPassword("123456");
        validUser.setEmail("a@b.c");
        validUser.setFirstName("A");

        when(userService.createUser(any(UserRegisterDTO.class))).thenReturn(testUserDTO);


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(UserRegisterDTO.class));
    }

    @Test
    void updateUser_ShouldAcceptValidUser_WithMinimumRequiredFields() throws Exception {

        String userId = "1";
        UserUpdateDTO validUser = new UserUpdateDTO();
        validUser.setUsername("abc");
        validUser.setPassword("123456");
        validUser.setEmail("a@b.c");
        validUser.setFirstName("A");

        when(userService.updateUser(eq(userId), any(UserUpdateDTO.class))).thenReturn(testUserDTO);


        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(eq(userId), any(UserUpdateDTO.class));
    }
}