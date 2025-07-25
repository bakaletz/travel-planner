package com.example.travel.planner.controller;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable String id){
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO){

        return userService.createUser(userRegisterDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateDTO userUpdateDTO){

        return userService.updateUser(id, userUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id){
        return userService.deleteUser(id);
    }

}
