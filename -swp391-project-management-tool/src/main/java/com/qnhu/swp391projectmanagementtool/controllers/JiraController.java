package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jira")
@RequiredArgsConstructor
public class JiraController {

    private final JiraService jiraService;

    //để test kết nối thôi
    @GetMapping("/test")
    public String testConnection() {

        boolean connected = jiraService.testConnection();

        if (connected) {
            return "Jira connection successful!";
        } else {
            return "Jira connection failed!";
        }
    }

    @GetMapping("/projects/{projectKey}/issues")
    public List<JiraIssue> getProjectIssues(@PathVariable String projectKey) {

        return jiraService.syncIssuesFromProject(projectKey);

    }
}