import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from './task.model';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private apiUrl = 'http://localhost:8089/api/tasks';

  constructor(private http: HttpClient) {}

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/all`);
  }

  getTasksByColumn(columnId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/by-column/${columnId}`);
  }

  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiUrl}/add`, task);
  }

  updateTask(id: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/update/${id}`, task);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  moveTaskToColumn(taskId: number, columnId: number): Observable<Task> {
    // You may need to implement this endpoint in your backend if not present
    return this.http.put<Task>(`${this.apiUrl}/move/${taskId}/to-column/${columnId}`, {});
  }
}
