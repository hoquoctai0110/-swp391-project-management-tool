package com.qnhu.swp391projectmanagementtool.mappers;

import com.qnhu.swp391projectmanagementtool.dtos.GroupRequestResponse;
import com.qnhu.swp391projectmanagementtool.dtos.UserSimpleResponse;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class GroupRequestMapper {

    public static GroupRequestResponse toResponse(GroupRequest request) {

        UserSimpleResponse lecturer = null;
        if (request.getLecturer() != null) {
            lecturer = new UserSimpleResponse(
                    request.getLecturer().getUserId(),
                    request.getLecturer().getUsername(),
                    request.getLecturer().getRole().name()
            );
        }

        UserSimpleResponse leader = null;
        if (request.getLeader() != null) {
            leader = new UserSimpleResponse(
                    request.getLeader().getUserId(),
                    request.getLeader().getUsername(),
                    request.getLeader().getRole().name()
            );
        }

        List<UserSimpleResponse> members =
                request.getMembers()
                        .stream()
                        .map(GroupRequestMapper::toUserSimple)
                        .collect(Collectors.toList());

        return new GroupRequestResponse(
                request.getRequestId(),
                request.getGroupName(),
                lecturer,
                leader,
                members,
                request.getStatus().name()
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