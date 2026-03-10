package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.repositories.JiraIssueRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JiraIssueRepository jiraIssueRepository;

    @Override
    public Object getDashboard(String assignee) {

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalTasks", jiraIssueRepository.countByAssignee(assignee));
        dashboard.put("todo", jiraIssueRepository.countByAssigneeAndStatus(assignee, "To Do"));
        dashboard.put("inProgress", jiraIssueRepository.countByAssigneeAndStatus(assignee, "In Progress"));
        dashboard.put("done", jiraIssueRepository.countByAssigneeAndStatus(assignee, "Done"));

        return dashboard;
    }

    @Override
    public List<JiraIssue> getMyTasks(String assignee) {
        return jiraIssueRepository.findByAssignee(assignee);
    }

    @Override
    public JiraIssue getTaskDetail(String issueKey) {
        return jiraIssueRepository.findByIssueKey(issueKey);
    }
}