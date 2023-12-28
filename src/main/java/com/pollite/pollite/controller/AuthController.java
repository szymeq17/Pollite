package com.pollite.pollite.controller;

import com.pollite.pollite.dto.UserDto;
import com.pollite.pollite.exception.UserAlreadyExistsException;
import com.pollite.pollite.service.poll.UserService;
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

    @GetMapping("register")
    public ResponseEntity<UserDto> register(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.registerUser(username, password));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity handleUserAlreadyExistsException() {
        return ResponseEntity.badRequest().build();
    }
}
