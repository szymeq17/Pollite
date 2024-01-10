package com.pollite.controller;

import com.pollite.dto.UserDto;
import com.pollite.exception.UserAlreadyExistsException;
import com.pollite.service.poll.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.authenticateUser(user));
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.registerUser(user.getUsername(), user.getPassword()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity handleUserAlreadyExistsException() {
        return ResponseEntity.badRequest().build();
    }
}
