package com.qnhu.swp391projectmanagementtool.entities;

import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.enums.UserStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer yob;
    private String phoneNumber;
    @Column(name = "jira_account_id")
    private String jiraAccountId;
    @Column(name = "jira_synced")
    private Boolean jiraSynced = false;

    @Column(name = "github_account_id")
    private String githubAccountId;
    @Column(name = "github_synced")
    private Boolean githubSynced = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getYob() {
        return yob;
    }

    public void setYob(Integer yob) {
        this.yob = yob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJiraAccountId() {
        return jiraAccountId;
    }

    public void setJiraAccountId(String jiraAccountId) {
        this.jiraAccountId = jiraAccountId;
    }

    public Boolean getJiraSynced() {
        return jiraSynced;
    }

    public void setJiraSynced(Boolean jiraSynced) {
        this.jiraSynced = jiraSynced;
    }

    public String getGithubAccountId() {
        return githubAccountId;
    }

    public void setGithubAccountId(String githubAccountId) {
        this.githubAccountId = githubAccountId;
    }

    public Boolean getGithubSynced() {
        return githubSynced;
    }

    public void setGithubSynced(Boolean githubSynced) {
        this.githubSynced = githubSynced;
    }

    public void login() {
    }

    public void logout() {
    }

    public void manageStudentGroups() {
    }

    public void viewRequirements() {
    }

    public void viewTasks() {
    }

    public void assignTaskToMember() {
    }

    public void commitToGitHub() {
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
