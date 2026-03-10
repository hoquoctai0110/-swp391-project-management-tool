package com.qnhu.swp391projectmanagementtool.services.interfaces;
import com.qnhu.swp391projectmanagementtool.dtos.JiraProjectDto;

import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import com.qnhu.swp391projectmanagementtool.entities.User;

import java.util.List;

public interface JiraService {

    boolean testConnection();

    void createProjectForGroup(int groupId, String projectKey, String projectName);

    void syncUserToJiraProject(Group group, User user);
    List<JiraProjectDto> getAllProjects();

    List<JiraIssue> syncIssuesFromProject(String projectKey);

    String getAccountIdByEmail(String email);

}