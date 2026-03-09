package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.AdminDashboardDto;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.JiraIssueRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final GroupRepository groupRepository;
    private final JiraIssueRepository jiraIssueRepository;

    public AdminDashboardDto getDashboard() {

        AdminDashboardDto dto = new AdminDashboardDto();

        dto.setTotalProjects(groupRepository.count());
        dto.setTotalIssues(jiraIssueRepository.count());
        dto.setTodo(jiraIssueRepository.countByStatus("To Do"));
        dto.setInProgress(jiraIssueRepository.countByStatus("In Progress"));
        dto.setDone(jiraIssueRepository.countByStatus("Done"));

        return dto;
    }
}