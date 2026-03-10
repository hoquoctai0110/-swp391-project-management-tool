package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.dtos.GroupRequestResponse;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-requests")
@RequiredArgsConstructor
public class GroupRequestController {

    private final GroupRequestService groupRequestService;

    // MEMBER tạo request
    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody CreateGroupRequest dto,
            Authentication authentication) {

        String email = authentication.getName();

        groupRequestService.createRequest(dto, email);

        return ResponseEntity.ok("Group request created successfully");
    }

    // ADMIN approve request
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable int requestId) {

        groupRequestService.approveRequest(requestId);

        return ResponseEntity.ok("Request approved successfully");
    }

    // ADMIN reject request
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable int requestId) {

        groupRequestService.rejectRequest(requestId);

        return ResponseEntity.ok("Request rejected successfully");
    }

    @GetMapping("/my-requests")
    public List<GroupRequestResponse> getMyRequests(Authentication authentication) {

        String email = authentication.getName();

        return groupRequestService.getMyRequests(email);
    }

    // MEMBER cancel request
    @DeleteMapping("/{requestId}/cancel")
    public ResponseEntity<String> cancelRequest(@PathVariable int requestId, Authentication authentication) {
        String email = authentication.getName();
        groupRequestService.cancelRequest(requestId, email);
        return ResponseEntity.ok("Request cancelled successfully");
    }
}