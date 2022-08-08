package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class ValidationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/validation")
    public String validation() {
        return "validation";
    }

    @PostMapping("/validation")
    public String addUser(User username, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(username.getUsername());
        if (userFromDb != null) {
            model.put("message", "User is already exists!");
            return "validation";
        } else {
            model.put("message", "You may use this name!");
            return "validation";
        }
    }
}
