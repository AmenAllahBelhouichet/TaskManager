import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8089/api/tasks';
  private jwtToken: string | null = null;

  constructor(private http: HttpClient) {}

  setToken(token: string) {
    this.jwtToken = token;
  }

  getTasks(): Observable<any> {
    const headers = this.jwtToken ? new HttpHeaders({ 'Authorization': `Bearer ${this.jwtToken}` }) : undefined;
    return this.http.get(this.apiUrl, { headers });
  }
}
