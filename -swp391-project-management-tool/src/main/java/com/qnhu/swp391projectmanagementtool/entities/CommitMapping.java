package com.qnhu.swp391projectmanagementtool.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "commit_mappings")
public class CommitMapping {

    @Id
    private String commitHash;
    private String message;
    private String repository;
    private String branch;
    private String jiraIssueKey;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime commitDate;
    private int linesAdded;
    private int linesDeleted;

    public String getMessage() {
        return message;
    }

    public String getRepository() {
        return repository;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setJiraIssueKey(String jiraIssueKey) {
        this.jiraIssueKey = jiraIssueKey;
    }

    public String getBranch() {
        return branch;
    }

    public String getJiraIssueKey() {
        return jiraIssueKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommitMapping() {
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public LocalDateTime getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(LocalDateTime commitDate) {
        this.commitDate = commitDate;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }
}
