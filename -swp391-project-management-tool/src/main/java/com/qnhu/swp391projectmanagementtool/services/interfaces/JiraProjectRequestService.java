package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.CreateJiraProjectRequestDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraProjectRequest;

import java.util.List;

public interface JiraProjectRequestService {

    void createRequest(CreateJiraProjectRequestDto dto, String leaderEmail);

    List<JiraProjectRequest> getPendingRequests();

    void approveRequest(int requestId);

    void rejectRequest(int requestId);
}