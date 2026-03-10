package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.RequestStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Integer> {

    @Query("""
            SELECT DISTINCT gr
            FROM GroupRequest gr
            LEFT JOIN FETCH gr.members
            LEFT JOIN FETCH gr.leader
            LEFT JOIN FETCH gr.lecturer
            WHERE gr.status = :status
            """)
    List<GroupRequest> findByStatusWithMembers(@Param("status") RequestStatus status);

    List<GroupRequest> findByCreatedBy(User user);

    @Query("""
            SELECT DISTINCT gr
            FROM GroupRequest gr
            LEFT JOIN gr.members m
            WHERE gr.createdBy = :user OR m = :user
            """)
    List<GroupRequest> findByUserInvolved(@Param("user") User user);
}