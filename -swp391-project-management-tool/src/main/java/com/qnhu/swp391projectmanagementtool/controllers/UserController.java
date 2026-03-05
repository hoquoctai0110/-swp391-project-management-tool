package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getUsersByRole(@RequestParam(required = false) Role role) {

        if (role != null) {
            return userRepository.findByRole(role);
        }

        return userRepository.findAll();
    }
}