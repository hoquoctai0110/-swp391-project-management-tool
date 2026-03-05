package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;

public interface GroupRequestService {

    void createRequest(CreateGroupRequest dto, String lecturerEmail);
    void approveRequest(int requestId);

    void rejectRequest(int id);
}