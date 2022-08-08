package com.example.sweater.service;

import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 10;

    private static final long LOCK_TIME_DURATION = 60 * 60 * 1000;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MyEntityService myEntityService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByUsername(s);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        myEntityService.markSuccess(newFailAttempts, user.getUsername());
    }

    public void resetFailedAttempts(String username) {
        myEntityService.markSuccess(0, username);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepo.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepo.save(user);
            return true;
        }
        return false;
    }

}
