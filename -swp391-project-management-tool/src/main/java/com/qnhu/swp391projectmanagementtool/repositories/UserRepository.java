package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByJiraSyncedFalse();
    List<User> findByRole(Role role);
    List<User> findByRoleIn(List<Role> roles);

    List<User> findByStatus(UserStatus status);
    List<User> findByStatusAndRole(UserStatus status, Role role);
    long countByRole(Role role);

    Optional<User> findByGithubAccountId(String githubAccountId);
}