package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/login")
    public String registration() {
        return "login";
    }

    @PostMapping("/login")
    public String addUser(User username, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(username.getUsername());

        if (userFromDb.getFailedAttempt() > 0) {
            model.put("message", "Invalid password or name!");
        }
        return "login";
    }
}
