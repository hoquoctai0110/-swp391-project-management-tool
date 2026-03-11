package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.GroupRequestResponse;
import com.qnhu.swp391projectmanagementtool.dtos.UserSimpleResponse;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRequestRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GroupRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/group-requests")
@RequiredArgsConstructor
public class AdminGroupRequestController {

        private final GroupRequestService groupRequestService;
        private final GroupRequestRepository groupRequestRepository;

        @GetMapping
        @Transactional
        public List<GroupRequestResponse> getAllRequests() {

                return groupRequestRepository.findAll()
                                .stream()
                                .map(r -> new GroupRequestResponse(

                                                r.getRequestId(),
                                                r.getGroupName(),

                                                new UserSimpleResponse(
                                                                r.getLecturer().getUserId(),
                                                                r.getLecturer().getUsername()),

                                                new UserSimpleResponse(
                                                                r.getLeader().getUserId(),
                                                                r.getLeader().getUsername()),

                                                r.getMembers().stream()
                                                                .map(m -> new UserSimpleResponse(
                                                                                m.getUserId(),
                                                                                m.getUsername()))
                                                                .toList(),

                                                r.getStatus().name(),
                                                r.getRejectionReason()))
                                .toList();
        }

        @PutMapping("/{id}/approve")
        public String approveRequest(@PathVariable int id) {

                groupRequestService.approveRequest(id);

                return "Request approved successfully!";
        }

        @PutMapping("/{id}/reject")
        public String rejectRequest(@PathVariable int id,
                        @RequestBody(required = false) java.util.Map<String, String> body) {
                String reason = (body != null && body.containsKey("reason")) ? body.get("reason") : null;
                groupRequestService.rejectRequest(id, reason);

                return "Request rejected successfully!";
        }
}