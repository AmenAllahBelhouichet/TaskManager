import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-accept-invite',
  templateUrl: './accept-invite.component.html',
  standalone: true,
  imports: [CommonModule]
})
export class AcceptInviteComponent implements OnInit {
  message: string | null = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      this.http.post('http://localhost:8089/api/project-members/accept-invite', { token }).subscribe({
        next: () => {
          this.message = 'You accepted the Invitation';
          this.error = null;
        },
        error: err => {
          let msg = err.error;
          if (typeof msg === 'object') msg = 'Invalid or expired invitation link.';
          this.error = msg || 'Invalid or expired invitation link.';
          this.message = null;
        }
      });
    } else {
      this.error = 'No invitation token found in the URL.';
      this.message = null;
    }
  }
} 