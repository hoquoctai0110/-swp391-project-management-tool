package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // lấy tất cả user hoặc filter theo role
    @GetMapping
    public List<User> getUsersByRole(@RequestParam(required = false) Role role) {

        if (role != null) {
            return userRepository.findByRole(role);
        }

        return userRepository.findAll();
    }

    // lấy danh sách lecturers
    @GetMapping("/lecturers")
    public List<User> getLecturers() {
        return userRepository.findByRole(Role.ROLE_LECTURER);
    }

    // lấy danh sách members
    @GetMapping("/members")
    public List<User> getMembers() {
        return userRepository.findByRole(Role.ROLE_MEMBER);
    }

    // lấy user theo id (optional nhưng rất hữu ích)
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}