package com.qnhu.swp391projectmanagementtool.dtos;

public class CreateJiraProjectRequest {

    private String projectKey;
    private String projectName;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}