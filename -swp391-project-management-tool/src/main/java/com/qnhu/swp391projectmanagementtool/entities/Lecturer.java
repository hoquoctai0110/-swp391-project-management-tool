package com.qnhu.swp391projectmanagementtool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "lecturers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Lecturer extends User {

    private String lecturerName;

    public Lecturer() {
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }
}