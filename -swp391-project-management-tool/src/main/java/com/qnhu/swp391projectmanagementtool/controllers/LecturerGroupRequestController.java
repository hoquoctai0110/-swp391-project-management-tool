package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecturer/group-requests")
@RequiredArgsConstructor
public class LecturerGroupRequestController {

    private final GroupRequestService groupRequestService;

    @PostMapping
    public String createRequest(@RequestBody CreateGroupRequest dto,
                                Authentication authentication) {

        String lecturerEmail = authentication.getName();

        groupRequestService.createRequest(dto, lecturerEmail);

        return "Group request submitted successfully!";
    }


}