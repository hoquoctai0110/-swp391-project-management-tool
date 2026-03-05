package com.qnhu.swp391projectmanagementtool.dtos;

public class UserSimpleResponse {

    private int userId;
    private String username;
    private String role;

    public UserSimpleResponse(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}