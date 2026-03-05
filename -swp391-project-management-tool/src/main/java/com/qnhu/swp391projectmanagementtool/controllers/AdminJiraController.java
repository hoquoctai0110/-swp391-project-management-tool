package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateJiraProjectRequest;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/groups")
@RequiredArgsConstructor
public class AdminJiraController {

    public JiraService jiraService;

    @PostMapping("/{groupId}/jira-project")
    public String createJiraProject(@PathVariable int groupId,
                                    @RequestBody CreateJiraProjectRequest request) {

        jiraService.createProjectForGroup(
                groupId,
                request.getProjectKey(),
                request.getProjectName()
        );

        return "Jira project created successfully!";
    }
}