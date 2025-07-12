import { Component, OnInit } from '@angular/core';
import { Board } from '../../service/board.model';
import { TaskColumn } from '../../service/task-column.model';
import { Task } from '../../service/task.model';
import { BoardService } from '../../service/board.service';
import { TaskColumnService } from '../../service/task-column.service';
import { TaskService } from '../../service/task.service';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { FormsModule } from '@angular/forms';
import { Project } from '../../service/project.model';
import { ProjectService } from '../../service/project.service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: true,
  imports: [CommonModule, DragDropModule, FormsModule, RouterModule]
})
export class DashboardComponent implements OnInit {
  projects: Project[] = [];
  selectedProject: Project | null = null;
  newProjectName = '';
  boards: Board[] = [];
  selectedBoard: Board | null = null;
  columns: TaskColumn[] = [];
  tasksByColumn: { [columnId: number]: Task[] } = {};

  // UI state for creation
  newBoardName = '';
  newColumnName = '';
  newTaskTitle: { [columnId: number]: string } = {};
  newTaskDescription: { [columnId: number]: string } = {};
  message = '';
  userIdWarning = '';
  showAddProject = false;
  editingProject: Project | null = null;
  editingProjectName = '';

  constructor(
    private projectService: ProjectService,
    private boardService: BoardService,
    private columnService: TaskColumnService,
    private taskService: TaskService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      this.projects = [];
      this.selectedProject = null;
      this.boards = [];
      this.selectedBoard = null;
      this.columns = [];
      this.tasksByColumn = {};
      this.userIdWarning = 'No userId found in localStorage. Please log in again.';
      return;
    }
    this.userIdWarning = '';
    this.projectService.getProjectsByOwnerId(Number(userId)).subscribe(projects => {
      this.projects = projects;
      if (!this.selectedProject && projects.length) {
        this.selectProject(projects[0]);
      }
      if (!projects.length) {
        this.selectedProject = null;
        this.boards = [];
        this.selectedBoard = null;
        this.columns = [];
        this.tasksByColumn = {};
      }
    });
  }

  selectProject(project: Project) {
    this.selectedProject = project;
    this.loadBoards();
  }

  createProject() {
    const userId = localStorage.getItem('userId');
    if (!this.newProjectName.trim() || !userId) {
      console.warn('Project name or userId missing');
      return;
    }
    this.projectService.createProject({ id: 0, name: this.newProjectName, owner: { id: Number(userId), username: '', email: '' } })
      .subscribe({
        next: (project) => {
          this.newProjectName = '';
          this.loadProjects();
        },
        error: (err) => {
          console.error('Failed to create project', err);
          alert('Failed to create project: ' + (err.error || err.message));
        }
      });
  }

  loadBoards() {
    if (!this.selectedProject) return;
    this.boards = this.selectedProject.boards || [];
    if (this.boards.length) {
      this.selectBoard(this.boards[0]);
    } else {
      this.selectedBoard = null;
      this.columns = [];
      this.tasksByColumn = {};
    }
  }

  selectBoard(board: Board) {
    this.selectedBoard = board;
    this.loadColumnsAndTasks(board.id);
  }

  loadColumnsAndTasks(boardId: number) {
    this.columnService.getColumnsByBoard(boardId).subscribe(columns => {
      this.columns = columns;
      this.columns.forEach(col => {
        this.taskService.getTasksByColumn(col.id).subscribe(tasks => {
          this.tasksByColumn[col.id] = tasks;
        });
      });
    });
  }

  createBoard() {
    if (!this.newBoardName.trim() || !this.selectedProject) return;
    this.boardService.createBoard({ id: 0, name: this.newBoardName, project: this.selectedProject }).subscribe(board => {
      this.newBoardName = '';
      this.loadProjects();
    });
  }

  createColumn() {
    if (!this.newColumnName.trim() || !this.selectedBoard) return;
    this.columnService.createColumn({ id: 0, name: this.newColumnName, board: this.selectedBoard }).subscribe(col => {
      this.newColumnName = '';
      this.loadColumnsAndTasks(this.selectedBoard!.id);
    });
  }

  createTask(column: TaskColumn) {
    const title = this.newTaskTitle[column.id];
    const description = this.newTaskDescription[column.id];
    if (!title || !title.trim()) return;
    this.taskService.createTask({ id: 0, title, description, status: column.name, columnId: column.id }).subscribe(task => {
      this.newTaskTitle[column.id] = '';
      this.newTaskDescription[column.id] = '';
      this.loadColumnsAndTasks(this.selectedBoard!.id);
    });
  }

  drop(event: CdkDragDrop<Task[]>, column: TaskColumn) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const task = event.previousContainer.data[event.previousIndex];
      this.taskService.moveTaskToColumn(task.id, column.id).subscribe(() => {
        transferArrayItem(
          event.previousContainer.data,
          event.container.data,
          event.previousIndex,
          event.currentIndex
        );
        this.loadColumnsAndTasks(this.selectedBoard!.id);
      });
    }
  }

  onProjectChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const index = select.selectedIndex - 1; // -1 for default option
    if (index >= 0 && index < this.projects.length) {
      this.selectProject(this.projects[index]);
    }
  }

  onBoardChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const index = select.selectedIndex - 1; // -1 for default option
    if (index >= 0 && index < this.boards.length) {
      this.selectBoard(this.boards[index]);
    }
  }

  goToBoards(projectId: number) {
    this.router.navigate(['/boards', projectId]);
  }

  startEditProject(project: Project) {
    this.editingProject = { ...project };
    this.editingProjectName = project.name;
  }

  confirmEditProject() {
    if (!this.editingProject || !this.editingProjectName.trim()) return;
    this.projectService.updateProject(this.editingProject.id, {
      id: this.editingProject.id,
      name: this.editingProjectName,
      owner: this.editingProject.owner
    }).subscribe(() => {
      this.editingProject = null;
      this.editingProjectName = '';
      this.loadProjects();
    });
  }

  cancelEditProject() {
    this.editingProject = null;
    this.editingProjectName = '';
  }

  deleteProject(projectId: number) {
    if (!confirm('Are you sure you want to delete this project?')) return;
    this.projectService.deleteProject(projectId).subscribe(() => {
      this.loadProjects();
    });
  }
}
