package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.JiraProjectRequest;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraProjectRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/jira-requests")
@RequiredArgsConstructor
public class AdminJiraController {

    private final JiraProjectRequestService jiraProjectRequestService;

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
}