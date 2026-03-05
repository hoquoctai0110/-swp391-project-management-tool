package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.RequestStatus;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRequestRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GroupRequestService;
import com.qnhu.swp391projectmanagementtool.services.interfaces.IGroupService;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupRequestServiceImpl implements GroupRequestService {

    private final GroupRequestRepository groupRequestRepository;
    private final UserRepository userRepository;
    private final IGroupService groupService;
    private final JiraService jiraService;
    private final GroupRepository groupRepository;

    @Override
    public void createRequest(CreateGroupRequest dto, String lecturerEmail) {

        User lecturer = userRepository.findByEmail(lecturerEmail)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        GroupRequest request = new GroupRequest();
        request.setGroupName(dto.getGroupName());
        request.setLecturer(lecturer);
        request.setStatus(RequestStatus.PENDING);

        User leader = userRepository.findById(dto.getLeaderId())
                .orElseThrow(() -> new RuntimeException("Leader not found"));
        request.setLeader(leader);

        List<User> members = new ArrayList<>();
        for (Integer id : dto.getMemberIds()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            members.add(user);
        }

        request.setMembers(members);

        groupRequestRepository.save(request);
    }

    @Override
    public void approveRequest(int requestId) {

        GroupRequest request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        var groupResponse = groupService.createGroup(request.getGroupName());

        Group group = groupRepository.findById(groupResponse.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found after creation"));

        group.setLecturer(request.getLecturer());
        groupRepository.save(group);

        for (User member : request.getMembers()) {
            groupService.addMember(group.getGroupId(), member.getUserId());
        }

        User leader = request.getLeader();

        if (!request.getMembers().contains(leader)) {
            groupService.addMember(group.getGroupId(), leader.getUserId());
        }

        groupService.assignLeader(group.getGroupId(), leader.getUserId());

        String projectKey = generateProjectKey(request.getGroupName());

        jiraService.createProjectForGroup(
                group.getGroupId(),
                projectKey,
                request.getGroupName()
        );

        request.setStatus(RequestStatus.APPROVED);
        groupRequestRepository.save(request);
    }

    private String generateProjectKey(String groupName) {

        String[] words = groupName.split(" ");
        StringBuilder key = new StringBuilder();

        for (String word : words) {
            if (!word.isBlank()) {
                key.append(word.charAt(0));
            }
        }

        int random = (int)(Math.random() * 90 + 10);

        return (key.toString() + random)
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();
    }

    @Override
    public void rejectRequest(int requestId) {

        GroupRequest request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        groupRequestRepository.save(request);
    }
}