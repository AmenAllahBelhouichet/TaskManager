package com.example.taskmanger.controller;

import com.example.taskmanger.model.ProjectMember;
import com.example.taskmanger.model.TeamMembers;
import com.example.taskmanger.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.client.RestTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {
    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUserToProject(@RequestBody Map<String, String> body) {
        Integer projectId = Integer.valueOf(body.get("projectId"));
        String email = body.get("email");
        TeamMembers role = TeamMembers.valueOf(body.getOrDefault("role", "INVITED"));
        try {
            ProjectMember member = projectMemberService.addMember(projectId, email, role);
            // Send email if invited
            if (role == TeamMembers.INVITED && member.getInviteToken() != null) {
                sendInviteEmail(email, member.getInviteToken());
            }
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<?> acceptInvite(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        boolean accepted = projectMemberService.acceptInvite(token);
        if (accepted) {
            return ResponseEntity.ok("Invite accepted!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or already accepted token");
        }
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<?> getMembersByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectMemberService.getMembersByProject(projectId));
    }

    // Use Mailgun's free API for sending emails
    private void sendInviteEmail(String toEmail, String token) {
        String inviteLink = " http://localhost:4200/accept-invite?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("You're invited to join a project!");
        message.setText("Click the link to accept the invitation: " + inviteLink);
        message.setFrom("amenallah.belouichet@gmail.com"); // Use your Gmail address
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Failed to send invite email: " + e.getMessage());
        }
    }
} 