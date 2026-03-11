package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.AdminDashboardDto;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.enums.UserStatus;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.JiraIssueRepository;

import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final GroupRepository groupRepository;
    private final JiraIssueRepository jiraIssueRepository;
    private final UserRepository userRepository;

    public AdminDashboardDto getDashboard() {

        AdminDashboardDto dto = new AdminDashboardDto();

        dto.setTotalProjects(groupRepository.count());
        dto.setTotalIssues(jiraIssueRepository.count());
        dto.setTodo(jiraIssueRepository.countByStatus("To Do"));
        dto.setInProgress(jiraIssueRepository.countByStatus("In Progress"));
        dto.setDone(jiraIssueRepository.countByStatus("Done"));

        dto.setTotalMembers(
                userRepository.countByRole(Role.ROLE_MEMBER)
        );

        dto.setTotalLeaders(
                userRepository.countByRole(Role.ROLE_LEADER)
        );

        dto.setTotalLecturers(
                userRepository.countByRole(Role.ROLE_LECTURER)
        );

        return dto;
    }

    public void deactivateUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}