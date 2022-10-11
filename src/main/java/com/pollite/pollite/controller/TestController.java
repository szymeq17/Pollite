package com.pollite.pollite.controller;

import com.pollite.pollite.service.poll.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @GetMapping("greet")
    public String greeting(Principal principal) {
        UserDetails userDetails= userDetailsService.loadUserByUsername(principal.getName());
        return String.format("Hello %s, your password is %s.", userDetails.getUsername(), userDetails.getPassword());
    }

    @GetMapping("register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "Zarejestrowano użytkownika " + username;
    }
}
