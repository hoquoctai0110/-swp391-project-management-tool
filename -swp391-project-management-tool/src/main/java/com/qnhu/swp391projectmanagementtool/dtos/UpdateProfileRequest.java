package com.qnhu.swp391projectmanagementtool.dtos;

public class UpdateProfileRequest {
    private String username;
    private Integer yob;
    private String phoneNumber;

    public UpdateProfileRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getYob() {
        return yob;
    }

    public void setYob(Integer yob) {
        this.yob = yob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}