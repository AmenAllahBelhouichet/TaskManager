import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

interface User {
  id: number;
  name: string;
  email: string;
  password?: string;
  role: string;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, HttpClientModule]
})
export class AdminDashboardComponent implements OnInit {
  users: User[] = [];
  userForm: Partial<User> = { name: '', email: '', password: '', role: 'USER' };
  editingUserId: number | null = null;
  error: string | null = null;
  success: string | null = null;

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.fetchUsers();
  }

  fetchUsers() {
    this.http.get<User[]>('http://localhost:8089/api/users/all').subscribe({
      next: users => this.users = users,
      error: () => this.error = 'Failed to fetch users'
    });
  }

  createUser() {
    if (!this.userForm.name || !this.userForm.email || !this.userForm.password || !this.userForm.role) {
      this.error = 'All fields are required';
      return;
    }
    this.http.post<User>('http://localhost:8089/api/users/signup', this.userForm).subscribe({
      next: () => {
        this.success = 'User created successfully';
        this.error = null;
        this.userForm = { name: '', email: '', password: '', role: 'USER' };
        this.fetchUsers();
      },
      error: () => this.error = 'Failed to create user'
    });
  }

  editUser(user: User) {
    this.editingUserId = user.id;
    this.userForm = { ...user, password: '' };
  }

  updateUser() {
    if (!this.editingUserId) return;
    const updateData = { ...this.userForm };
    if (!updateData.password) delete updateData.password;
    this.http.put<User>(`http://localhost:8089/api/users/update/${this.editingUserId}`, updateData).subscribe({
      next: () => {
        this.success = 'User updated successfully';
        this.error = null;
        this.editingUserId = null;
        this.userForm = { name: '', email: '', password: '', role: 'USER' };
        this.fetchUsers();
      },
      error: () => this.error = 'Failed to update user'
    });
  }

  deleteUser(id: number) {
    if (!confirm('Are you sure you want to delete this user?')) return;
    this.http.delete(`http://localhost:8089/api/users/delete/${id}`).subscribe({
      next: () => {
        this.success = 'User deleted successfully';
        this.error = null;
        this.fetchUsers();
      },
      error: () => this.error = 'Failed to delete user'
    });
  }

  cancelEdit() {
    this.editingUserId = null;
    this.userForm = { name: '', email: '', password: '', role: 'USER' };
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
} 