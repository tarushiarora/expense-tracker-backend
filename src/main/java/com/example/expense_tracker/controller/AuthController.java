package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.User;
import com.example.expense_tracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user){
        String token = authService.verify(user);
        // Wrap the plain string in a Map (JSON object)
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }
}
