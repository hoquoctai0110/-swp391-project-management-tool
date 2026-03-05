package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRequestRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/group-requests")
@RequiredArgsConstructor
public class AdminGroupRequestController {

    private final GroupRequestService groupRequestService;
    private final GroupRequestRepository groupRequestRepository;

    @GetMapping
    public List<GroupRequest> getAllRequests() {
        return groupRequestRepository.findAll();
    }

    @PostMapping("/{id}/approve")
    public String approveRequest(@PathVariable int id) {

        groupRequestService.approveRequest(id);

        return "Request approved successfully!";
    }

    @PostMapping("/{id}/reject")
    public String rejectRequest(@PathVariable int id) {

        groupRequestService.rejectRequest(id);

        return "Request rejected successfully!";
    }
}