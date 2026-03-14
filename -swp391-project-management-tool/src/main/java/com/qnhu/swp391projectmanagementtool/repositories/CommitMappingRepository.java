package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.CommitMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitMappingRepository extends JpaRepository<CommitMapping, String> {
    List<CommitMapping> findByUserUserId(int userId);
    List<CommitMapping> findByJiraIssueKey(String jiraIssueKey);
}
