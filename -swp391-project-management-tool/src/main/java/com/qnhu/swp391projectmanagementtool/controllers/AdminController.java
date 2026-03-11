package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.services.implement.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;

    @PutMapping("/users/{id}/disable")
    public String disableUser(@PathVariable Integer id){

        adminService.deactivateUser(id);

        return "User disabled successfully";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserDetail(@PathVariable Integer id){

        return adminService.getUserById(id);
    }
}
