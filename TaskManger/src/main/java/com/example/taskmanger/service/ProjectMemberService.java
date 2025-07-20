package com.example.taskmanger.service;

import com.example.taskmanger.model.Project;
import com.example.taskmanger.model.ProjectMember;
import com.example.taskmanger.model.TeamMembers;
import com.example.taskmanger.model.User;
import com.example.taskmanger.repository.ProjectMemberRepository;
import com.example.taskmanger.repository.ProjectRepository;
import com.example.taskmanger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectMemberService {
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    public ProjectMember addMember(Integer projectId, String email, TeamMembers role) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User with email not found: " + email);
        }
        Project project = projectRepository.findById(projectId).orElseThrow();
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setTeamRole(role);
        if (role == TeamMembers.INVITED) {
            member.setInviteToken(java.util.UUID.randomUUID().toString());
            member.setAccepted(false);
            member.setInviteCreatedAt(new java.util.Date());
        } else {
            member.setInviteToken(null);
            member.setAccepted(true);
            member.setInviteCreatedAt(null);
        }
        return projectMemberRepository.save(member);
    }

    public boolean acceptInvite(String token) {
        ProjectMember member = projectMemberRepository.findByInviteToken(token).orElse(null);
        if (member == null || member.isAccepted()) return false;
        // Check if token is expired (older than 7 days)
        if (member.getInviteCreatedAt() != null) {
            long now = System.currentTimeMillis();
            long created = member.getInviteCreatedAt().getTime();
            long sevenDaysMillis = 7L * 24 * 60 * 60 * 1000;
            if (now - created > sevenDaysMillis) {
                return false; // Token expired
            }
        }
        member.setAccepted(true);
        member.setTeamRole(TeamMembers.MEMBER);
        projectMemberRepository.save(member);
        return true;
    }

    public java.util.List<ProjectMember> getMembersByProject(Integer projectId) {
        java.util.List<ProjectMember> members = projectMemberRepository.findAll().stream()
            .filter(pm -> pm.getProject().getId() == projectId)
            .toList();
        // Fetch project and owner
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null && project.getOwner() != null) {
            boolean ownerAlreadyIncluded = members.stream().anyMatch(pm ->
                pm.getUser() != null && pm.getUser().getId() == project.getOwner().getId() && pm.getTeamRole() == TeamMembers.OWNER
            );
            if (!ownerAlreadyIncluded) {
                ProjectMember ownerMember = new ProjectMember();
                ownerMember.setId(null); // Not persisted
                ownerMember.setProject(project);
                ownerMember.setUser(project.getOwner());
                ownerMember.setTeamRole(TeamMembers.OWNER);
                ownerMember.setAccepted(true);
                ownerMember.setInviteToken(null);
                ownerMember.setInviteCreatedAt(null);
                members = new java.util.ArrayList<>(members);
                members.add(0, ownerMember); // Add owner at the start
            }
        }
        return members;
    }
} 