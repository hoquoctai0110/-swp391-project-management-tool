package com.qnhu.swp391projectmanagementtool.dtos;

public class UpdateIntegrationRequest {

    private String jiraAccountId;
    private String githubAccountId;

    public UpdateIntegrationRequest() {
    }

    public String getJiraAccountId() {
        return jiraAccountId;
    }

    public void setJiraAccountId(String jiraAccountId) {
        this.jiraAccountId = jiraAccountId;
    }

    public String getGithubAccountId() {
        return githubAccountId;
    }

    public void setGithubAccountId(String githubAccountId) {
        this.githubAccountId = githubAccountId;
    }
}
