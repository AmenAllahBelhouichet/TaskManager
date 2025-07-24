import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule]
})
export class NavbarComponent {
  @Input() showTeamMembers = false;
  @Input() teamMembers: { name: string; email: string; role: string }[] = [];
  @Input() teamMembersLoading = false;

  showProfileMenu = false;
  showEditProfileModal = false;
  showTeamModal = false;
  showAIPromptModal = false;
  aiPromptText = '';
  aiPromptResult = '';
  aiPromptError = '';

  user = JSON.parse(localStorage.getItem('user') || '{}');
  editName = this.user.name || '';
  editEmail = this.user.email || '';
  editPassword = '';
  editError = '';
  editSuccess = '';

  @Output() aiActionCompleted = new EventEmitter<void>();

  constructor(private router: Router, private userService: UserService, private http: HttpClient) {}

  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  openEditProfile() {
    this.showEditProfileModal = true;
    this.showProfileMenu = false;
    this.editName = this.user.name || '';
    this.editEmail = this.user.email || '';
    this.editPassword = '';
    this.editError = '';
    this.editSuccess = '';
  }

  saveProfile() {
    if (!this.editName.trim() || !this.editEmail.trim()) {
      this.editError = 'Name and email are required.';
      return;
    }
    if (this.editPassword && this.editPassword.length < 6) {
      this.editError = 'Password must be at least 6 characters.';
      return;
    }
    const updateData: any = {
      name: this.editName,
      email: this.editEmail,
      role: this.user.role // Always include the current role
    };
    if (this.editPassword) updateData.password = this.editPassword;
    this.userService.updateUser(this.user.id, updateData).subscribe({
      next: (updatedUser: any) => {
        this.user = { ...this.user, ...updateData };
        localStorage.setItem('user', JSON.stringify(this.user));
        this.editSuccess = 'Profile updated!';
        setTimeout(() => this.showEditProfileModal = false, 1200);
      },
      error: () => {
        this.editError = 'Failed to update profile.';
      }
    });
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  openTeamModal() {
    this.showTeamModal = true;
  }
  closeTeamModal() {
    this.showTeamModal = false;
  }

  openAIPromptModal() {
    this.showAIPromptModal = true;
    this.aiPromptText = '';
    this.aiPromptResult = '';
    this.aiPromptError = '';
  }

  submitAIPrompt() {
    this.aiPromptError = '';
    this.aiPromptResult = '';
    if (!this.aiPromptText.trim()) {
      this.aiPromptError = 'Prompt cannot be empty.';
      return;
    }
    // Send the prompt text to the backend natural prompt endpoint
    this.http.post('http://localhost:8089/api/ai-agents/execute-natural-prompt', { promptText: this.aiPromptText }, { responseType: 'text' })
      .subscribe({
        next: (result) => {
          this.aiPromptResult = result;
          this.showAIPromptModal = false;
          this.aiActionCompleted.emit(); // Notify parent to refresh data
        },
        error: (err) => {
          this.aiPromptError = 'Failed to execute prompt: ' + (err.error || err.message);
        }
      });
  }

  get isOwner(): boolean {
    if (!this.user || !this.teamMembers) return false;
    return this.teamMembers.some(m => m.role === 'OWNER' && (m.email === this.user.email || m.name === this.user.name));
  }
} 