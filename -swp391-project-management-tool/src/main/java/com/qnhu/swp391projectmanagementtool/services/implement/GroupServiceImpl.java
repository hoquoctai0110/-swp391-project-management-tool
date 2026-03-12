package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.mappers.GroupMapper;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.IGroupService;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final JiraService jiraService;

    @Override
    public GroupResponse createGroup(String groupName) {

        if (groupRepository.existsByGroupName(groupName)) {
            throw new RuntimeException("Group name already exists");
        }

        Group group = new Group();
        group.setGroupName(groupName);

        Group savedGroup = groupRepository.save(group);

        return GroupMapper.toResponse(savedGroup);
    }

    @Override
    public void addMember(int groupId, int userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new RuntimeException("Admin cannot join group");
        }

        if (user.getRole() == Role.ROLE_LECTURER) {
            group.setLecturer(user);
        } else {

            if (group.getMembers().contains(user)) {
                throw new RuntimeException("User already in group");
            }

            group.addMember(user);
        }

        groupRepository.save(group);

        if (group.getProjectKey() != null && !group.getProjectKey().isBlank()) {
            try {
                jiraService.syncUserToJiraProject(group, user);
                user.setJiraSynced(true);
            } catch (Exception e) {
                user.setJiraSynced(false);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void assignLeader(int groupId, int leaderId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(user)) {
            throw new RuntimeException("User must be a member of this group");
        }

        group.setTeamLeader(user);

        groupRepository.save(group);

        if (group.getProjectKey() != null && !group.getProjectKey().isBlank()) {
            jiraService.syncUserToJiraProject(group, user);
        }
    }

    @Override
    public List<GroupResponse> getAllGroups() {

        return groupRepository.findAll()
                .stream()
                .map(GroupMapper::toResponse)
                .toList();
    }

    @Override
    public GroupResponse getGroupById(int groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return GroupMapper.toResponse(group);
    }

    @Override
    public void deleteGroup(int groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public List<GroupResponse> getGroupsByCurrentUser(User currentUser) {

        List<Group> groups;

        switch (currentUser.getRole()) {

            case ROLE_ADMIN:
                groups = groupRepository.findAll();
                break;

            case ROLE_LECTURER:
                groups = groupRepository.findByLecturer(currentUser);
                break;

            case ROLE_MEMBER:
                groups = groupRepository.findByMembersContaining(currentUser);
                break;

            default:
                groups = groupRepository.findByMembersContaining(currentUser);
        }

        return groups.stream()
                .map(GroupMapper::toResponse)
                .toList();
    }

    @Override
    public void removeMember(int groupId, int userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(user)) {
            throw new RuntimeException("User is not in this group");
        }

        if (group.getTeamLeader() != null &&
                group.getTeamLeader().getUserId() == userId) {

            group.setTeamLeader(null);
        }

        group.removeMember(user);

        groupRepository.save(group);
    }

    @Override
    public void removeLecturer(int groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setLecturer(null);

        groupRepository.save(group);
    }

    @Override
    public void updateGithubLink(int groupId, String githubLink, String currentUserEmail) {
        
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getTeamLeader() == null || !group.getTeamLeader().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Only the team leader can update the GitHub link");
        }

        group.setGithubLink(githubLink);
        groupRepository.save(group);
    }
}