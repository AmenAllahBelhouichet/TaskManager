import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BoardService } from '../service/board.service';
import { Board } from '../service/board.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-boards-page',
  templateUrl: './boards-page.component.html',
  styleUrls: ['./boards-page.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class BoardsPageComponent implements OnInit {
  boards: Board[] = [];
  projectId: number = 0;
  newBoardName = '';
  showAddBoard = false;
  editingBoard: Board | null = null;
  editingBoardName = '';

  constructor(private route: ActivatedRoute, private boardService: BoardService) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.projectId = Number(params.get('projectId'));
      console.log('Loaded BoardsPageComponent for projectId:', this.projectId);
      this.loadBoards();
    });
  }

  loadBoards() {
    this.boardService.getBoardsByProjectId(this.projectId).subscribe(boards => {
      this.boards = boards;
    });
  }

  createBoard() {
    if (!this.newBoardName.trim()) return;
    console.log('Creating board with payload:', {
      id: 0,
      name: this.newBoardName,
      project: { id: this.projectId, name: '' }
    });
    this.boardService.createBoard({ id: 0, name: this.newBoardName, project: { id: this.projectId, name: '' } }).subscribe(board => {
      this.newBoardName = '';
      this.showAddBoard = false;
      this.loadBoards();
    });
  }

  startEditBoard(board: Board) {
    this.editingBoard = { ...board };
    this.editingBoardName = board.name;
  }

  confirmEditBoard() {
    if (!this.editingBoard || !this.editingBoardName.trim()) return;
    this.boardService.updateBoard(this.editingBoard.id, {
      id: this.editingBoard.id,
      name: this.editingBoardName,
      project: { id: this.projectId, name: '' }
    }).subscribe(() => {
      this.editingBoard = null;
      this.editingBoardName = '';
      this.loadBoards();
    });
  }

  cancelEditBoard() {
    this.editingBoard = null;
    this.editingBoardName = '';
  }

  deleteBoard(boardId: number) {
    if (!confirm('Are you sure you want to delete this board?')) return;
    this.boardService.deleteBoard(boardId).subscribe(() => {
      this.loadBoards();
    });
  }
} 