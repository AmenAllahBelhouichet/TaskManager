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
            member.setInviteToken(UUID.randomUUID().toString());
            member.setAccepted(false);
        } else {
            member.setInviteToken(null);
            member.setAccepted(true);
        }
        return projectMemberRepository.save(member);
    }

    public boolean acceptInvite(String token) {
        ProjectMember member = projectMemberRepository.findByInviteToken(token).orElse(null);
        if (member == null || member.isAccepted()) return false;
        member.setAccepted(true);
        member.setTeamRole(TeamMembers.MEMBER);
        projectMemberRepository.save(member);
        return true;
    }
} 