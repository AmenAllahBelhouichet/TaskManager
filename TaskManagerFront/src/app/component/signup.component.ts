import { Component } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule]
})
export class SignupComponent {
  name = '';
  email = '';
  password = '';
  error: string | null = null;
  success: string | null = null;

  constructor(private authService: AuthService) {}

  signup() {
    this.authService.signup({ name: this.name, email: this.email, password: this.password }).subscribe({
      next: () => {
        this.success = 'Signup successful! You can now log in.';
        this.error = null;
      },
      error: (err) => {
        this.error = 'Signup failed';
        this.success = null;
      }
    });
  }
} 