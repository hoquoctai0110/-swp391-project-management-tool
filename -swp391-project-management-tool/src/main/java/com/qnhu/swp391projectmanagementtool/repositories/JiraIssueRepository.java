package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.JiraIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, String> {

    List<JiraIssue> findByProjectKey(String projectKey);

    long countByStatus(String status);

    long countByProjectKeyAndStatus(String projectKey, String status);

    List<JiraIssue> findByAssignee(String assignee);

    long countByProjectKey(String projectKey);

    long countByAssignee(String assignee);

    long countByAssigneeAndStatus(String assignee, String status);

    JiraIssue findByIssueKey(String issueKey);

}