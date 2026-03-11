package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.dtos.GroupRequestResponse;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.RequestStatus;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.mappers.GroupRequestMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void createRequest(CreateGroupRequest dto, String requesterEmail) {

        // người gửi request
        User createdBy = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        // lecturer
        User lecturer = userRepository.findById(dto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // leader
        User leader = userRepository.findById(dto.getLeaderId())
                .orElseThrow(() -> new RuntimeException("Leader not found"));

        // check member list
        if (dto.getMemberIds() == null || dto.getMemberIds().isEmpty()) {
            throw new RuntimeException("Member list cannot be empty");
        }

        // check duplicate members
        Set<Integer> uniqueMembers = new HashSet<>(dto.getMemberIds());

        if (uniqueMembers.size() != dto.getMemberIds().size()) {
            throw new RuntimeException("Duplicate members detected");
        }

        // leader phải nằm trong member list
        if (!uniqueMembers.contains(dto.getLeaderId())) {
            throw new RuntimeException("Leader must be included in member list");
        }

        // check group name trùng
        if (groupRepository.existsByGroupName(dto.getGroupName())) {
            throw new RuntimeException("Group name already exists");
        }

        // convert memberIds -> List<User>
        List<User> members = new ArrayList<>();

        for (Integer id : uniqueMembers) {

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Member not found: " + id));

            members.add(user);
        }

        // tạo request
        GroupRequest request = new GroupRequest();

        request.setGroupName(dto.getGroupName());
        request.setCreatedBy(createdBy);
        request.setLecturer(lecturer);
        request.setLeader(leader);
        request.setMembers(members);
        request.setStatus(RequestStatus.PENDING);

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

        leader.setRole(Role.ROLE_LEADER);
        userRepository.save(leader);

        group = groupRepository.findById(group.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found after update"));

        String projectKey = generateProjectKey(request.getGroupName());

        while (groupRepository.existsByProjectKey(projectKey)) {
            projectKey = generateProjectKey(request.getGroupName());
        }

        // Tạo tên dự án không bị trùng bằng cách thêm đuôi ngẫu nhiên
        int randomSuffix = (int) (Math.random() * 9000 + 1000); // 4 chữ số
        String uniqueProjectName = request.getGroupName() + " P" + randomSuffix;

        jiraService.createProjectForGroup(
                group.getGroupId(),
                projectKey,
                uniqueProjectName);

        // reload group sau khi tạo Jira project
        group = groupRepository.findById(group.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found after Jira creation"));

        System.out.println("=== START JIRA USER SYNC ===");

        // sync leader
//        if (group.getTeamLeader() != null) {
//            try {
//                jiraService.syncUserToJiraProject(group, group.getTeamLeader());
//            } catch (Exception e) {
//                System.out.println("SYNC FAILED (LEADER): " + group.getTeamLeader().getEmail());
//                e.printStackTrace();
//            }
//        }

        // sync members
        for (User member : request.getMembers()) {
            try {
                jiraService.syncUserToJiraProject(group, member);
            } catch (Exception e) {
                System.out.println("SYNC FAILED (MEMBER): " + member.getEmail());
                e.printStackTrace();
            }
        }

        // sync lecturer
//        if (group.getLecturer() != null) {
//            try {
//                jiraService.syncUserToJiraProject(group, group.getLecturer());
//            } catch (Exception e) {
//                System.out.println("SYNC FAILED (LECTURER): " + group.getLecturer().getEmail());
//                e.printStackTrace();
//            }
//        }

        System.out.println("=== JIRA USER SYNC FINISHED ===");

        request.setStatus(RequestStatus.APPROVED);
        groupRequestRepository.save(request);
    }

    @Override
    public void rejectRequest(int requestId, String reason) {

        GroupRequest request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);

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

        String cleanedKey = key.toString().replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        if (cleanedKey.isEmpty() || Character.isDigit(cleanedKey.charAt(0))) {
            cleanedKey = "P" + cleanedKey;
        }

        if (cleanedKey.length() > 6) {
            cleanedKey = cleanedKey.substring(0, 6);
        }

        int random = (int) (Math.random() * 90 + 10);

        return cleanedKey + random;
    }

    @Override
    public List<GroupRequestResponse> getMyRequests(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return groupRequestRepository.findByUserInvolved(user)
                .stream()
                .map(GroupRequestMapper::toResponse)
                .toList();
    }

    @Override
    public void cancelRequest(int requestId, String email) {
        GroupRequest request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy yêu cầu đang chờ duyệt");
        }

        groupRequestRepository.delete(request);
    }
}