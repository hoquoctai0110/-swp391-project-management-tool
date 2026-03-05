package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Integer> {

    List<GroupRequest> findByStatus(Enum status);

}