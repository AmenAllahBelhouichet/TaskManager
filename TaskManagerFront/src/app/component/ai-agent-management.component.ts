import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface AiAgent {
  id: number;
  name: string;
  task: string;
}

@Component({
  selector: 'app-ai-agent-management',
  templateUrl: './ai-agent-management.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AiAgentManagementComponent implements OnInit {
  agents: AiAgent[] = [];
  agentForm: Partial<AiAgent> = { name: '', task: '' };
  editingAgentId: number | null = null;
  error: string | null = null;
  success: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchAgents();
  }

  fetchAgents() {
    this.http.get<AiAgent[]>('http://localhost:8089/api/ai-agents').subscribe({
      next: agents => this.agents = agents,
      error: err => {
        this.error = 'Failed to fetch AI Agents';
        this.success = null;
      }
    });
  }

  createAgent() {
    if (!this.agentForm.name || !this.agentForm.task) {
      this.error = 'All fields are required';
      return;
    }
    this.http.post<AiAgent>('http://localhost:8089/api/ai-agents', this.agentForm).subscribe({
      next: () => {
        this.success = 'AI Agent created successfully';
        this.error = null;
        this.agentForm = { name: '', task: '' };
        this.fetchAgents();
      },
      error: () => this.error = 'Failed to create AI Agent'
    });
  }

  editAgent(agent: AiAgent) {
    this.editingAgentId = agent.id;
    this.agentForm = { name: agent.name, task: agent.task };
  }

  updateAgent() {
    if (!this.editingAgentId) return;
    this.http.put<AiAgent>(`http://localhost:8089/api/ai-agents/${this.editingAgentId}`, this.agentForm).subscribe({
      next: () => {
        this.success = 'AI Agent updated successfully';
        this.error = null;
        this.editingAgentId = null;
        this.agentForm = { name: '', task: '' };
        this.fetchAgents();
      },
      error: () => this.error = 'Failed to update AI Agent'
    });
  }

  deleteAgent(id: number) {
    if (!confirm('Are you sure you want to delete this AI Agent?')) return;
    this.http.delete(`http://localhost:8089/api/ai-agents/${id}`).subscribe({
      next: () => {
        this.success = 'AI Agent deleted successfully';
        this.error = null;
        this.fetchAgents();
      },
      error: () => this.error = 'Failed to delete AI Agent'
    });
  }

  cancelEdit() {
    this.editingAgentId = null;
    this.agentForm = { name: '', task: '' };
  }
} 