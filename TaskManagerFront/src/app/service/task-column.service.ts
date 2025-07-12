import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskColumn } from './task-column.model';

@Injectable({ providedIn: 'root' })
export class TaskColumnService {
  private apiUrl = 'http://localhost:8089/api/task-columns';

  constructor(private http: HttpClient) {}

  getColumnsByBoard(boardId: number): Observable<TaskColumn[]> {
    // You may need to implement this endpoint in your backend if not present
    return this.http.get<TaskColumn[]>(`${this.apiUrl}/by-board/${boardId}`);
  }

  getAllColumns(): Observable<TaskColumn[]> {
    return this.http.get<TaskColumn[]>(`${this.apiUrl}/all`);
  }

  createColumn(column: TaskColumn): Observable<TaskColumn> {
    return this.http.post<TaskColumn>(`${this.apiUrl}/add`, column);
  }

  updateColumn(id: number, column: TaskColumn): Observable<TaskColumn> {
    return this.http.put<TaskColumn>(`${this.apiUrl}/update/${id}`, column);
  }

  deleteColumn(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
} 