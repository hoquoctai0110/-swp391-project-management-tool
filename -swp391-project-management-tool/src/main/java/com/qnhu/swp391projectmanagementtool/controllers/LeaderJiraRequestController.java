package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateJiraProjectRequestDto;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraProjectRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leader/jira-requests")
@RequiredArgsConstructor
public class LeaderJiraRequestController {

    private final JiraProjectRequestService jiraProjectRequestService;

    @PostMapping
    public String createRequest(@RequestBody CreateJiraProjectRequestDto dto,
                                Authentication authentication) {

        String leaderEmail = authentication.getName();
        jiraProjectRequestService.createRequest(dto, leaderEmail);

        return "Jira project request submitted successfully!";
    }
}