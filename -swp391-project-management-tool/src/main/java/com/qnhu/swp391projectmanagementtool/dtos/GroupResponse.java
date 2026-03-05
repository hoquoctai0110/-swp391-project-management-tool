package com.qnhu.swp391projectmanagementtool.dtos;

import java.util.List;

public class GroupResponse {

    private int groupId;
    private String groupName;
    private UserSimpleResponse lecturer;
    private UserSimpleResponse teamLeader;
    private List<UserSimpleResponse> members;

    public GroupResponse(int groupId,
                         String groupName,
                         UserSimpleResponse lecturer,
                         UserSimpleResponse teamLeader,
                         List<UserSimpleResponse> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lecturer = lecturer;
        this.teamLeader = teamLeader;
        this.members = members;
    }

    public int getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public UserSimpleResponse getLecturer() { return lecturer; }
    public UserSimpleResponse getTeamLeader() { return teamLeader; }
    public List<UserSimpleResponse> getMembers() { return members; }
}