package com.example.taskmanger.repository;

import com.example.taskmanger.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    // Add custom queries if needed
    Optional<ProjectMember> findByInviteToken(String inviteToken);
    List<ProjectMember> findByUserIdAndAcceptedTrue(int userId);
} 