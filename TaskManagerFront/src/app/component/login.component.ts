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
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (res) => {
        const token = res.token;
        if (token) {
          this.taskService.setToken(token);
          this.error = null;
          const payload = JSON.parse(atob(token.split('.')[1]));
          const role = payload.role || payload.authorities || payload.auth || payload['authority'] || '';
          if (role === 'ADMIN' || (Array.isArray(role) && role.includes('ADMIN')) || (typeof role === 'string' && role.includes('ADMIN'))) {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        }
      },
      error: (err) => {
        this.error = 'Login failed';
      }
    });
  }
} 