package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.UpdateProfileRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String email = authentication.getName();
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = opt.get();
        Map<String, Object> resp = buildSafeUserResponse(user);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody UpdateProfileRequest req) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String email = authentication.getName();
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = opt.get();

        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            String newUsername = req.getUsername().trim();
            if (newUsername.length() > 100) {
                return ResponseEntity.badRequest().body("username too long (max 100 chars)");
            }
            user.setUsername(newUsername);
        }

        if (req.getYob() != null) {
            int yob = req.getYob();
            int currentYear = java.time.Year.now().getValue();
            if (yob < 1900 || yob > currentYear) {
                return ResponseEntity.badRequest().body("yob invalid (must be between 1900 and " + currentYear + ")");
            }
            user.setYob(yob);
        }

        if (req.getPhoneNumber() != null && !req.getPhoneNumber().isBlank()) {
            String phone = req.getPhoneNumber().trim();
            String digitsOnly = phone.replaceAll("\\s+", "");
            if (!digitsOnly.matches("^\\+?\\d{7,15}$")) {
                return ResponseEntity.badRequest().body("phoneNumber invalid (should be digits, 7-15 chars, optional leading +)");
            }
            user.setPhoneNumber(digitsOnly);
        }

        userRepository.save(user);

        Map<String, Object> resp = buildSafeUserResponse(user);
        return ResponseEntity.ok(resp);
    }

    private Map<String, Object> buildSafeUserResponse(User user) {
        Map<String, Object> m = new HashMap<>();
        m.put("userId", user.getUserId());
        m.put("email", user.getEmail());
        m.put("username", user.getUsername());
        m.put("role", user.getRole());
        m.put("yob", user.getYob());
        m.put("phoneNumber", user.getPhoneNumber());
        return m;
    }
}