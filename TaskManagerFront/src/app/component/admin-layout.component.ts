import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../service/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SharedModalComponent } from './shared-modal.component';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, FormsModule, SharedModalComponent]
})
export class AdminLayoutComponent {
  showDropdown = false;
  showEditProfileModal = false;
  editName = '';
  editEmail = '';
  editPassword = '';
  editError = '';
  editSuccess = '';
  user: any = {};

  constructor(private router: Router, private userService: UserService) {
    this.user = JSON.parse(localStorage.getItem('user') || '{}');
    this.editName = this.user.name || '';
    this.editEmail = this.user.email || '';
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getUserEmail(): string {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        return user.email || 'Admin';
      } catch {
        return 'Admin';
      }
    }
    return 'Admin';
  }

  openEditProfile() {
    this.showEditProfileModal = true;
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
        setTimeout(() => {
          this.showEditProfileModal = false;
          // Update displayed info immediately
          this.editName = this.user.name;
          this.editEmail = this.user.email;
        }, 1200);
      },
      error: () => {
        this.editError = 'Failed to update profile.';
      }
    });
  }

  toggleDropdown(event: Event) {
    event.preventDefault();
    this.showDropdown = !this.showDropdown;
  }
} 