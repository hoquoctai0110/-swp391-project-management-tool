package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.dtos.GroupRequestResponse;

import java.util.List;

public interface GroupRequestService {

    void createRequest(CreateGroupRequest dto, String requesterEmail);

    void approveRequest(int requestId);

    void rejectRequest(int requestId, String reason);

    List<GroupRequestResponse> getMyRequests(String email);

    void cancelRequest(int requestId, String email);
}