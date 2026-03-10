package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;

import java.util.List;

public interface MemberService {

    Object getDashboard(String assignee);

    List<JiraIssue> getMyTasks(String assignee);

    JiraIssue getTaskDetail(String issueKey);
}