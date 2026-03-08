package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.GroupRequest;
import com.qnhu.swp391projectmanagementtool.entities.Lecturer;
import com.qnhu.swp391projectmanagementtool.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    boolean existsByGroupName(String groupName);

    List<Group> findByLecturer(User lecturer);

    List<Group> findByTeamLeader(User teamLeader);

    List<Group> findByMembersContaining(User member);

    boolean existsByProjectKey(String projectKey);

}