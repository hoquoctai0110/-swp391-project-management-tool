package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerGroupDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;

import java.util.List;

public interface LecturerService {

    List<LecturerGroupDto> getAllGroups();

    List<JiraIssue> getGroupIssues(Integer groupId);

    LecturerDashboardDto getDashboard();

    GroupProgressDto getGroupProgress(Integer groupId);
}