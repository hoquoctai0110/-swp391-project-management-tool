package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.AdminDashboardDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraProjectRequest;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraProjectRequestService;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import com.qnhu.swp391projectmanagementtool.services.implement.AdminServiceImpl;
import com.qnhu.swp391projectmanagementtool.dtos.JiraProjectDto;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/jira-requests")
public class AdminJiraController {

    private final JiraProjectRequestService jiraProjectRequestService;
    private final JiraService jiraService;
    private final AdminServiceImpl adminService;

    public AdminJiraController(
            JiraProjectRequestService jiraProjectRequestService,
            JiraService jiraService,
            AdminServiceImpl adminService
    ) {
        this.jiraProjectRequestService = jiraProjectRequestService;
        this.jiraService = jiraService;
        this.adminService = adminService;
    }

    @GetMapping("/pending")
    public List<JiraProjectRequest> getPendingRequests() {
        return jiraProjectRequestService.getPendingRequests();
    }

    @PutMapping("/{requestId}/approve")
    public String approveRequest(@PathVariable int requestId) {
        jiraProjectRequestService.approveRequest(requestId);
        return "Jira project request approved successfully!";
    }

    @PutMapping("/{requestId}/reject")
    public String rejectRequest(@PathVariable int requestId) {
        jiraProjectRequestService.rejectRequest(requestId);
        return "Jira project request rejected successfully!";
    }

    @GetMapping("/projects")
    public List<JiraProjectDto> getAllProjects() {
        return jiraService.getAllProjects();
    }

    @GetMapping("/dashboard")
    public AdminDashboardDto getDashboard() {
        return adminService.getDashboard();
    }
}