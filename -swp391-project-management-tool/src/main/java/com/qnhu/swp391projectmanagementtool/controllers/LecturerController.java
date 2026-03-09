package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerGroupDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.services.interfaces.LecturerService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    /**
     * 1. Dashboard tổng quan của Lecturer
     */
    @GetMapping("/dashboard")
    public LecturerDashboardDto getDashboard() {
        return lecturerService.getDashboard();
    }

    /**
     * 2. Lấy danh sách nhóm (group) mà lecturer quản lý
     */
    @GetMapping("/groups")
    public List<LecturerGroupDto> getGroups() {
        return lecturerService.getAllGroups();
    }

    /**
     * 3. Xem danh sách Jira issues của một group
     */
    @GetMapping("/groups/{groupId}/issues")
    public List<JiraIssue> getGroupIssues(@PathVariable Integer groupId) {
        return lecturerService.getGroupIssues(groupId);
    }

    /**
     * 4. Xem tiến độ Jira của group
     */
    @GetMapping("/groups/{groupId}/progress")
    public GroupProgressDto getGroupProgress(@PathVariable Integer groupId) {
        return lecturerService.getGroupProgress(groupId);
    }
}