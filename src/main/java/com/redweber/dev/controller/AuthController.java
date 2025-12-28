package com.redweber.dev.controller;

import com.redweber.dev.dto.LoginRequest;
import com.redweber.dev.dto.SignupRequest;
import com.redweber.dev.dto.UserResponse;
import com.redweber.dev.entity.User;
import com.redweber.dev.service.UserService;
import com.redweber.dev.utilities.RegularExpression;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RegularExpression regex;

    public AuthController(UserService userService, RegularExpression regex) {
        this.userService = userService;
        this.regex = regex;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @RequestBody SignupRequest req,
            HttpSession session
    ) {

        if (!regex.validUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(null);
        }

        if (!regex.validPassword(req.getPassword())) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = userService.signup(
                req.getUsername(),
                req.getPassword()
        );

        session.setAttribute("USER", user);

        return ResponseEntity.ok(
                new UserResponse(user.getId(), user.getUsername())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest req,
            HttpSession session
    ) {

        User user = userService.login(
                req.getUsername(),
                req.getPassword()
        );

        session.setAttribute("USER", user);

        return ResponseEntity.ok(
                new UserResponse(user.getId(), user.getUsername())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
