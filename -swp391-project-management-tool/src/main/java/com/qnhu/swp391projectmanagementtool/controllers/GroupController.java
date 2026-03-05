package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;
    private final UserRepository userRepository;

    @PostMapping
    public GroupResponse createGroup(@RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request.getGroupName());
    }

    @PutMapping("/{groupId}/member/{userId}")
    public ResponseEntity<String> addMember(
            @PathVariable int groupId,
            @PathVariable int userId) {

        groupService.addMember(groupId, userId);
        return ResponseEntity.ok("User processed successfully");
    }

    @PutMapping("/{groupId}/leader/{leaderId}")
    public void assignLeader(@PathVariable int groupId,
                             @PathVariable int leaderId) {
        groupService.assignLeader(groupId, leaderId);
    }

    @GetMapping
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroup(@PathVariable int groupId) {
        return groupService.getGroupById(groupId);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable int groupId) {
        groupService.deleteGroup(groupId);
    }

    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponse>> getMyGroups(Authentication authentication) {

        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                groupService.getGroupsByCurrentUser(currentUser)
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{groupId}/member/{userId}")
    public void removeMember(@PathVariable int groupId,
                             @PathVariable int userId) {
        groupService.removeMember(groupId, userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{groupId}/lecturer")
    public void removeLecturer(@PathVariable int groupId) {
        groupService.removeLecturer(groupId);
    }
}