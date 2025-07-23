import { Component } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { TaskService } from '../service/task.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule]
})
export class LoginComponent {
  email = '';
  password = '';
  error: string | null = null;

  constructor(private authService: AuthService, private taskService: TaskService, private router: Router) {}

  login() {
    if (!this.password || !this.password.trim()) {
      this.error = 'Password is required.';
      return;
    }
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        const token = response.token;
        localStorage.setItem('jwtToken', token); // Store as 'jwtToken' for interceptor
        let userIdMsg = '';
        if (response.user && response.user.id) {
          localStorage.setItem('userId', response.user.id.toString());
          localStorage.setItem('user', JSON.stringify(response.user)); // Store full user object
          userIdMsg = `Logged in as user ID: ${response.user.id}`;
          console.log(userIdMsg);
        } else {
          userIdMsg = 'Warning: No userId returned from backend!';
          console.warn(userIdMsg);
        }
        this.error = null;
        alert(userIdMsg); // Show userId or warning
        // Role-based navigation
        if (response.user && response.user.role && response.user.role.toUpperCase() === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (err) => {
        this.error = 'Login failed';
      }
    });
  }
} 