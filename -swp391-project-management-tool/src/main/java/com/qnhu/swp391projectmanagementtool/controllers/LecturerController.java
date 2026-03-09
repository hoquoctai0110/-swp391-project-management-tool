package com.qnhu.swp391projectmanagementtool.controllers;

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

    @GetMapping("/groups")
    public List<LecturerGroupDto> getGroups() {
        return lecturerService.getAllGroups();
    }

    @GetMapping("/groups/{groupId}/issues")
    public List<JiraIssue> getGroupIssues(@PathVariable Integer groupId) {
        return lecturerService.getGroupIssues(groupId);
    }

    @GetMapping("/dashboard")
    public LecturerDashboardDto getDashboard() {
        return lecturerService.getDashboard();
    }
}