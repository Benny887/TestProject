package com.example.sweater.service;

import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
public class MyEntityService {
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public void markSuccess(Integer failAttempts, String username) {
        userRepo.updateFailedAttempts(failAttempts, username);
    }

}
