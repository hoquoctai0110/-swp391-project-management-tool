package com.qnhu.swp391projectmanagementtool.mappers;

import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.dtos.UserSimpleResponse;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class GroupMapper {

    public static GroupResponse toResponse(Group group) {

        UserSimpleResponse lecturer = null;
        if (group.getLecturer() != null) {
            lecturer = new UserSimpleResponse(
                    group.getLecturer().getUserId(),
                    group.getLecturer().getUsername(),
                    group.getLecturer().getRole().name()
            );
        }

        UserSimpleResponse leader = null;
        if (group.getTeamLeader() != null) {
            leader = new UserSimpleResponse(
                    group.getTeamLeader().getUserId(),
                    group.getTeamLeader().getUsername(),
                    group.getTeamLeader().getRole().name()
            );
        }

        List<UserSimpleResponse> members =
                group.getMembers()
                        .stream()
                        .map(GroupMapper::toUserSimple)
                        .collect(Collectors.toList());

        return new GroupResponse(
                group.getGroupId(),
                group.getGroupName(),
                lecturer,
                leader,
                members,
                group.getProjectKey(),
                group.getProjectId(),
                group.getGithubLink()
        );
    }

    private static UserSimpleResponse toUserSimple(User user) {
        return new UserSimpleResponse(
                user.getUserId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}