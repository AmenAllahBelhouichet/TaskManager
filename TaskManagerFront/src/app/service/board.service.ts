import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Board } from './board.model';

@Injectable({ providedIn: 'root' })
export class BoardService {
  private apiUrl = 'http://localhost:8089/api/boards';

  constructor(private http: HttpClient) {}

  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/all`);
  }

  getBoardById(id: number): Observable<Board> {
    return this.http.get<Board>(`${this.apiUrl}/get/${id}`);
  }

  createBoard(board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.apiUrl}/add`, board);
  }

  updateBoard(id: number, board: Board): Observable<Board> {
    return this.http.put<Board>(`${this.apiUrl}/update/${id}`, board);
  }

  deleteBoard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getBoardsByProjectId(projectId: number): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/by-project/${projectId}`);
  }
} 