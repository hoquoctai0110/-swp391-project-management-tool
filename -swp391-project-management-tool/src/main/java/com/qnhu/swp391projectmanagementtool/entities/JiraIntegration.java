package com.qnhu.swp391projectmanagementtool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jira_integrations")
public class JiraIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int integrationId;

    private String apiKey;
    private String projectKey;

    public JiraIntegration() {
    }

    public int getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(int integrationId) {
        this.integrationId = integrationId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public void syncRequirements() {
    }

    public void syncTasks() {
    }
}
