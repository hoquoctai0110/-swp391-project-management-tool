package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.services.interfaces.LeaderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leader")
@RequiredArgsConstructor
public class LeaderController {

    private final LeaderService leaderService;

    // 1. Dashboard group
    @GetMapping("/dashboard/{groupId}")
    public LecturerDashboardDto getDashboard(@PathVariable Integer groupId) {
        return leaderService.getDashboard(groupId);
    }

    // 2. Xem issues của group
    @GetMapping("/groups/{groupId}/issues")
    public List<JiraIssue> getGroupIssues(@PathVariable Integer groupId) {
        return leaderService.getGroupIssues(groupId);
    }

    // 3. Progress của group
    @GetMapping("/groups/{groupId}/progress")
    public GroupProgressDto getGroupProgress(@PathVariable Integer groupId) {
        return leaderService.getGroupProgress(groupId);
    }

    // 4. Task của member
    @GetMapping("/members/{assignee}/tasks")
    public List<JiraIssue> getMemberTasks(@PathVariable String assignee) {
        return leaderService.getMemberTasks(assignee);
    }
}