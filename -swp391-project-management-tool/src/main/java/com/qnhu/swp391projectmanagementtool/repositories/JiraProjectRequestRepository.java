package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.JiraProjectRequest;
import com.qnhu.swp391projectmanagementtool.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JiraProjectRequestRepository extends JpaRepository<JiraProjectRequest, Integer> {

    List<JiraProjectRequest> findByStatus(RequestStatus status);
}