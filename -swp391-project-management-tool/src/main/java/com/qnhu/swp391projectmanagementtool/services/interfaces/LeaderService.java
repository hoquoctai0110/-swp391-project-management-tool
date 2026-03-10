package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.GroupProgressDto;
import com.qnhu.swp391projectmanagementtool.dtos.LecturerDashboardDto;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;

import java.util.List;

public interface LeaderService {

    LecturerDashboardDto getDashboard(Integer groupId);

    List<JiraIssue> getGroupIssues(Integer groupId);

    GroupProgressDto getGroupProgress(Integer groupId);

    List<JiraIssue> getMemberTasks(String assignee);
}