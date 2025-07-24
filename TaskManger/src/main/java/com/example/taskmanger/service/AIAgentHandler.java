package com.example.taskmanger.service;


public interface AIAgentHandler {
    String getSupportedAction();
    String handle(String TargetEntity);
}