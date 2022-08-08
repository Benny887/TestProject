package com.example.sweater.login_handling;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = (User) userService.loadUserByUsername(username);


        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 10 failed attempts."
                            + " It will be unlocked after 1 hour.");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    showMessage(exception, response);
                }
            } else if (!user.isAccountNonLocked()) {
                if (userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                    showMessage(exception, response);
                } else {
                    long time = (user.getLockTime().getTime() + 60 * 60 * 1000) - new Date().getTime();
                    exception = new LockedException("Your account is locked. Please try again in " +
                            String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))));
                    showMessage(exception, response);
                }
            }
        }
        super.setDefaultFailureUrl("/login?error");
    }

    public void showMessage(AuthenticationException exception, HttpServletResponse response) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("Service message:", exception.getMessage());
        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
