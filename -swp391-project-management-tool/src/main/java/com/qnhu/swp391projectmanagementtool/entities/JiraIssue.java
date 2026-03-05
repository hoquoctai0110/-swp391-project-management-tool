package com.qnhu.swp391projectmanagementtool.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jira_issues")
public class JiraIssue {

    @Id
    private String jiraIssueId;

    private String title;
    private String priority;
    private String status;

    public JiraIssue() {
    }

    public String getJiraIssueId() {
        return jiraIssueId;
    }

    public void setJiraIssueId(String jiraIssueId) {
        this.jiraIssueId = jiraIssueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void syncWithJira() {
    }
}
