package com.redweber.dev.service;

import com.redweber.dev.entity.User;
import com.redweber.dev.repository.UserRepository;
import com.redweber.dev.utilities.ResourcesNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User signup(String username, String rawPassword) {

        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));

        return userRepo.save(user);
    }

    public User login(String username, String rawPassword) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Invalid credentials"));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new ResourcesNotFoundException("Invalid credentials");
        }

        return user;
    }
}
