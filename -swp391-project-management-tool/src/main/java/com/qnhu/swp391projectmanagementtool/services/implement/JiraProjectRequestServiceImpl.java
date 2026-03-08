package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.CreateJiraProjectRequestDto;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.JiraProjectRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.RequestStatus;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.JiraProjectRequestRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraProjectRequestService;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectRequestServiceImpl implements JiraProjectRequestService {

    private final JiraProjectRequestRepository jiraProjectRequestRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final JiraService jiraService;

    @Override
    public void createRequest(CreateJiraProjectRequestDto dto, String leaderEmail) {
        User leader = userRepository.findByEmail(leaderEmail)
                .orElseThrow(() -> new RuntimeException("Leader not found"));

        if (leader.getRole() != Role.ROLE_LEADER) {
            throw new RuntimeException("Only leader can create Jira project request");
        }

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getTeamLeader() == null || group.getTeamLeader().getUserId() != leader.getUserId()) {
            throw new RuntimeException("You are not the leader of this group");
        }

        if (group.getProjectKey() != null && !group.getProjectKey().isBlank()) {
            throw new RuntimeException("This group already has a Jira project");
        }

        String generatedProjectKey = "GR" + group.getGroupId();
        String generatedProjectName = group.getGroupName();

        JiraProjectRequest request = new JiraProjectRequest();
        request.setGroup(group);
        request.setLeader(leader);
        request.setProjectKey(generatedProjectKey);
        request.setProjectName(generatedProjectName);
        request.setStatus(RequestStatus.PENDING);

        jiraProjectRequestRepository.save(request);
    }

    @Override
    public List<JiraProjectRequest> getPendingRequests() {
        return jiraProjectRequestRepository.findByStatus(RequestStatus.PENDING);
    }

    @Override
    public void approveRequest(int requestId) {
        JiraProjectRequest request = jiraProjectRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        jiraService.createProjectForGroup(
                request.getGroup().getGroupId(),
                request.getProjectKey(),
                request.getProjectName()
        );

        request.setStatus(RequestStatus.APPROVED);
        jiraProjectRequestRepository.save(request);
    }

    @Override
    public void rejectRequest(int requestId) {
        JiraProjectRequest request = jiraProjectRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        request.setStatus(RequestStatus.REJECTED);
        jiraProjectRequestRepository.save(request);
    }
}