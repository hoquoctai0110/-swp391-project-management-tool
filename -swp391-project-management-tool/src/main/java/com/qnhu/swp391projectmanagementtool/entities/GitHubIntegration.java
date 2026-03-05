package com.qnhu.swp391projectmanagementtool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "github_integrations")
public class GitHubIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int integrationId;

    private String accessToken;
    private String repositoryUrl;

    public GitHubIntegration() {
    }

    public int getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(int integrationId) {
        this.integrationId = integrationId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public void getCommitStatistics() {
    }

    public void generateCommitReport() {
    }
}
