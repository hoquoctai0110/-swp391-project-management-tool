package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.services.interfaces.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. Dashboard cá nhân
    @GetMapping("/dashboard/{assignee}")
    public Object getDashboard(@PathVariable String assignee) {
        return memberService.getDashboard(assignee);
    }

    // 2. Xem tất cả task của mình
    @GetMapping("/my-tasks/{assignee}")
    public List<JiraIssue> getMyTasks(@PathVariable String assignee) {
        return memberService.getMyTasks(assignee);
    }

    // 3. Xem chi tiết task
    @GetMapping("/tasks/{issueKey}")
    public JiraIssue getTaskDetail(@PathVariable String issueKey) {
        return memberService.getTaskDetail(issueKey);
    }
}