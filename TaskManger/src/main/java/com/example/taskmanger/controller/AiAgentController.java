package com.example.taskmanger.controller;

import com.example.taskmanger.service.AiAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-agents")
public class AiAgentController {
    @Autowired
    private AiAgentService aiAgentService;

    @PostMapping("/execute-natural-prompt")
    public ResponseEntity<String> executeNaturalPrompt(@RequestBody Map<String, String> body) {
        String promptText = body.get("promptText");
        System.out.println("Received promptText: " + promptText);
        if (promptText == null || promptText.isEmpty()) {
            return ResponseEntity.badRequest().body("Prompt text is required");
        }
        String trimmedPrompt = promptText.trim();
        String result = "";
        try {
            if (trimmedPrompt.toLowerCase().startsWith("move task")) {
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("move task \"(.+?)\" to column \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher matcher = pattern.matcher(trimmedPrompt);
                if (matcher.find()) {
                    String taskTitle = matcher.group(1);
                    String columnName = matcher.group(2);
                    String intent = "move_task";
                    String targetEntity = "move_task:" + taskTitle + ":" + columnName;
                    result = aiAgentService.executePrompt(intent, targetEntity);
                } else {
                    result = "Could not parse task title and column name. Use: move task \"Task Name\" to column \"Column Name\"";
                }
            } else if (trimmedPrompt.toLowerCase().startsWith("create task")) {
                java.util.regex.Pattern titlePattern = java.util.regex.Pattern.compile("create task \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern columnPattern = java.util.regex.Pattern.compile("to column \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern deadlinePattern = java.util.regex.Pattern.compile("with deadline of ([0-9\\-]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern statusPattern = java.util.regex.Pattern.compile("status ([^ ]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern descriptionPattern = java.util.regex.Pattern.compile("description \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern assignToPattern = java.util.regex.Pattern.compile("assign to ([^ ]+@[^ ]+)", java.util.regex.Pattern.CASE_INSENSITIVE);

                String title = null, column = null, deadline = null, status = null, description = null, assignTo = null;
                java.util.regex.Matcher m;
                m = titlePattern.matcher(trimmedPrompt); if (m.find()) title = m.group(1);
                m = columnPattern.matcher(trimmedPrompt); if (m.find()) column = m.group(1);
                m = deadlinePattern.matcher(trimmedPrompt); if (m.find()) deadline = m.group(1);
                m = statusPattern.matcher(trimmedPrompt); if (m.find()) status = m.group(1);
                m = descriptionPattern.matcher(trimmedPrompt); if (m.find()) description = m.group(1);
                m = assignToPattern.matcher(trimmedPrompt); if (m.find()) assignTo = m.group(1);

                org.json.JSONObject json = new org.json.JSONObject();
                if (title != null) json.put("title", title);
                if (column != null) json.put("column", column);
                if (deadline != null) json.put("deadline", deadline);
                if (status != null) json.put("status", status);
                if (description != null) json.put("description", description);
                if (assignTo != null) json.put("assignTo", assignTo);

                String intent = "flexible_create_task";
                String targetEntity = json.toString();
                result = aiAgentService.executePrompt(intent, targetEntity);
            } else if (trimmedPrompt.toLowerCase().startsWith("assign task")) {
                java.util.regex.Pattern titlePattern = java.util.regex.Pattern.compile("assign task \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern idPattern = java.util.regex.Pattern.compile("assign task (\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern assignToPattern = java.util.regex.Pattern.compile("to ([^ ]+@[^ ]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                String id = null, title = null, assignTo = null;
                java.util.regex.Matcher m;
                m = idPattern.matcher(trimmedPrompt); if (m.find()) id = m.group(1);
                m = titlePattern.matcher(trimmedPrompt); if (m.find()) title = m.group(1);
                m = assignToPattern.matcher(trimmedPrompt); if (m.find()) assignTo = m.group(1);
                org.json.JSONObject json = new org.json.JSONObject();
                if (id != null) json.put("id", Integer.parseInt(id));
                if (title != null) json.put("title", title);
                if (assignTo != null) json.put("assignTo", assignTo);
                String intent = "assign_task_only";
                String targetEntity = json.toString();
                result = aiAgentService.executePrompt(intent, targetEntity);
            } else if (trimmedPrompt.toLowerCase().startsWith("edit task")) {
                java.util.regex.Pattern titlePattern = java.util.regex.Pattern.compile("edit task \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern idPattern = java.util.regex.Pattern.compile("edit task (\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern newTitlePattern = java.util.regex.Pattern.compile("set title to \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern statusPattern = java.util.regex.Pattern.compile("status to ([^ ]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern deadlinePattern = java.util.regex.Pattern.compile("deadline to ([0-9\\-]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern descriptionPattern = java.util.regex.Pattern.compile("description to \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern assignToPattern = java.util.regex.Pattern.compile("assign to ([^ ]+@[^ ]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern columnPattern = java.util.regex.Pattern.compile("column to \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);

                String id = null, title = null, newTitle = null, status = null, deadline = null, description = null, assignTo = null, column = null;
                java.util.regex.Matcher m;
                m = idPattern.matcher(trimmedPrompt); if (m.find()) id = m.group(1);
                m = titlePattern.matcher(trimmedPrompt); if (m.find()) title = m.group(1);
                m = newTitlePattern.matcher(trimmedPrompt); if (m.find()) newTitle = m.group(1);
                m = statusPattern.matcher(trimmedPrompt); if (m.find()) status = m.group(1);
                m = deadlinePattern.matcher(trimmedPrompt); if (m.find()) deadline = m.group(1);
                m = descriptionPattern.matcher(trimmedPrompt); if (m.find()) description = m.group(1);
                m = assignToPattern.matcher(trimmedPrompt); if (m.find()) assignTo = m.group(1);
                m = columnPattern.matcher(trimmedPrompt); if (m.find()) column = m.group(1);

                org.json.JSONObject json = new org.json.JSONObject();
                if (id != null) json.put("id", Integer.parseInt(id));
                if (title != null) json.put("title", title);
                if (newTitle != null) json.put("newTitle", newTitle);
                if (status != null) json.put("status", status);
                if (deadline != null) json.put("deadline", deadline);
                if (description != null) json.put("description", description);
                if (assignTo != null) json.put("assignTo", assignTo);
                if (column != null) json.put("column", column);

                String intent = "edit_task";
                String targetEntity = json.toString();
                result = aiAgentService.executePrompt(intent, targetEntity);
            } else if (trimmedPrompt.toLowerCase().startsWith("delete task")) {
                java.util.regex.Pattern titlePattern = java.util.regex.Pattern.compile("delete task \"(.+?)\"", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Pattern idPattern = java.util.regex.Pattern.compile("delete task (\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                String id = null, title = null;
                java.util.regex.Matcher m;
                m = idPattern.matcher(trimmedPrompt); if (m.find()) id = m.group(1);
                m = titlePattern.matcher(trimmedPrompt); if (m.find()) title = m.group(1);
                org.json.JSONObject json = new org.json.JSONObject();
                if (id != null) json.put("id", Integer.parseInt(id));
                if (title != null) json.put("title", title);
                String intent = "delete_task";
                String targetEntity = json.toString();
                result = aiAgentService.executePrompt(intent, targetEntity);
            } else {
                result = "Unknown or unsupported prompt.";
            }
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
        return ResponseEntity.ok(result);
    }
} 