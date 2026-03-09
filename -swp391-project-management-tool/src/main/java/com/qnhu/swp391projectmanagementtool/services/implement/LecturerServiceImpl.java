package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerGroupDto;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.JiraIssueRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.LecturerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {

    private final GroupRepository groupRepository;
    private final JiraIssueRepository jiraIssueRepository;

    // 1. Lấy tất cả group của lecturer
    @Override
    public List<LecturerGroupDto> getAllGroups() {

        List<Group> groups = groupRepository.findAll();

        return groups.stream().map(group -> {
            LecturerGroupDto dto = new LecturerGroupDto();
            dto.setGroupId(group.getGroupId());
            dto.setGroupName(group.getGroupName());
            dto.setProjectKey(group.getProjectKey());
            return dto;
        }).collect(Collectors.toList());
    }

    // 2. Lấy danh sách Jira issues của group
    @Override
    public List<JiraIssue> getGroupIssues(Integer groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        return jiraIssueRepository.findByProjectKey(group.getProjectKey());
    }

    // 3. Dashboard tổng quan cho lecturer
    @Override
    public LecturerDashboardDto getDashboard() {

        LecturerDashboardDto dto = new LecturerDashboardDto();

        dto.setTotalGroups(groupRepository.count());
        dto.setTotalIssues(jiraIssueRepository.count());
        dto.setTodo(jiraIssueRepository.countByStatus("To Do"));
        dto.setInProgress(jiraIssueRepository.countByStatus("In Progress"));
        dto.setDone(jiraIssueRepository.countByStatus("Done"));

        return dto;
    }

    // 4. Progress của một group
    @Override
    public GroupProgressDto getGroupProgress(Integer groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        String projectKey = group.getProjectKey();

        GroupProgressDto dto = new GroupProgressDto();

        dto.setTodo(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "To Do"));
        dto.setInProgress(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "In Progress"));
        dto.setDone(jiraIssueRepository.countByProjectKeyAndStatus(projectKey, "Done"));

        return dto;
    }
}