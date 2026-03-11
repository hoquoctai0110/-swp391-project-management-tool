package com.qnhu.swp391projectmanagementtool.dtos;

import java.util.List;

public class GroupRequestResponse {

    private int requestId;
    private String groupName;
    private UserSimpleResponse lecturer;
    private UserSimpleResponse leader;
    private List<UserSimpleResponse> members;
    private String status;
    private String rejectionReason;

    public GroupRequestResponse(int requestId,
            String groupName,
            UserSimpleResponse lecturer,
            UserSimpleResponse leader,
            List<UserSimpleResponse> members,
            String status,
            String rejectionReason) {

        this.requestId = requestId;
        this.groupName = groupName;
        this.lecturer = lecturer;
        this.leader = leader;
        this.members = members;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getGroupName() {
        return groupName;
    }

    public UserSimpleResponse getLecturer() {
        return lecturer;
    }

    public UserSimpleResponse getLeader() {
        return leader;
    }

    public List<UserSimpleResponse> getMembers() {
        return members;
    }

    public String getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}