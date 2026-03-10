package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.JiraIssueRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.LeaderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderServiceImpl implements LeaderService {

    private final GroupRepository groupRepository;
    private final JiraIssueRepository jiraIssueRepository;

    @Override
    public LecturerDashboardDto getDashboard(Integer groupId) {

        Group group = groupRepository.findById(groupId).orElseThrow();
        String projectKey = group.getProjectKey();

        LecturerDashboardDto dto = new LecturerDashboardDto();

        dto.setTotalIssues(jiraIssueRepository.countByProjectKey(projectKey));
        dto.setTodo(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "To Do"));
        dto.setInProgress(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "In Progress"));
        dto.setDone(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "Done"));

        return dto;
    }

    @Override
    public List<JiraIssue> getGroupIssues(Integer groupId) {

        Group group = groupRepository.findById(groupId).orElseThrow();

        return jiraIssueRepository.findByProjectKey(group.getProjectKey());
    }

    @Override
    public GroupProgressDto getGroupProgress(Integer groupId) {

        Group group = groupRepository.findById(groupId).orElseThrow();
        String projectKey = group.getProjectKey();

        GroupProgressDto dto = new GroupProgressDto();

        dto.setTodo(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "To Do"));
        dto.setInProgress(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "In Progress"));
        dto.setDone(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "Done"));

        return dto;
    }

    @Override
    public List<JiraIssue> getMemberTasks(String assignee) {

        return jiraIssueRepository.findByAssignee(assignee);
    }
}