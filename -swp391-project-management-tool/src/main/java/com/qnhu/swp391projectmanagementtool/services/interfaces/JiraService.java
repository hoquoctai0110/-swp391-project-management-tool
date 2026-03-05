package com.qnhu.swp391projectmanagementtool.services.interfaces;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.User;

public interface JiraService {

    boolean testConnection();

    void createProjectForGroup(int groupId, String projectKey, String projectName);
    void syncUserToJiraProject(Group group, User user);

}