package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.entities.User;

import java.util.List;

public interface IGroupService {

    GroupResponse createGroup(String groupName);

    void addMember(int groupId, int userId);

    void assignLeader(int groupId, int leaderId);

    List<GroupResponse> getAllGroups();

    GroupResponse getGroupById(int groupId);

    void deleteGroup(int groupId);

    List<GroupResponse> getGroupsByCurrentUser(User currentUser);

    void removeMember(int groupId, int userId);

    void removeLecturer(int groupId);
}