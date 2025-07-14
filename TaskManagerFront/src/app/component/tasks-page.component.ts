import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TaskColumnService } from '../service/task-column.service';
import { TaskService } from '../service/task.service';
import { TaskColumn } from '../service/task-column.model';
import { Task } from '../service/task.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-tasks-page',
  templateUrl: './tasks-page.component.html',
  styleUrls: ['./tasks-page.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule]
})
export class TasksPageComponent implements OnInit {
  boardId: number = 0;
  columns: TaskColumn[] = [];
  tasksByColumn: { [columnId: number]: Task[] } = {};
  newColumnName = '';
  newTaskTitle: { [columnId: number]: string } = {};
  newTaskDescription: { [columnId: number]: string } = {};
  newTaskDeadline: { [columnId: number]: string } = {};
  editingColumn: TaskColumn | null = null;
  editingColumnName = '';

  constructor(
    private route: ActivatedRoute,
    private columnService: TaskColumnService,
    private taskService: TaskService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.boardId = Number(params.get('boardId'));
      this.loadColumnsAndTasks();
    });
  }

  loadColumnsAndTasks() {
    this.columnService.getColumnsByBoard(this.boardId).subscribe(columns => {
      this.columns = columns;
      this.columns.forEach(col => {
        this.taskService.getTasksByColumn(col.id).subscribe(tasks => {
          this.tasksByColumn[col.id] = tasks;
        });
      });
    });
  }

  createColumn() {
    if (!this.newColumnName.trim()) return;
    this.columnService.createColumn({ id: 0, name: this.newColumnName, board: { id: this.boardId, name: '' } }).subscribe(col => {
      this.newColumnName = '';
      this.loadColumnsAndTasks();
    });
  }

  createTask(column: TaskColumn) {
    const title = this.newTaskTitle[column.id];
    const description = this.newTaskDescription[column.id];
    const deadline = this.newTaskDeadline[column.id];
    if (!title || !title.trim()) return;
    this.taskService.createTask({
      id: 0,
      title,
      description,
      status: column.name,
      deadline: deadline ? new Date(deadline) : undefined,
      taskColumn: { id: column.id }
    }).subscribe(task => {
      this.newTaskTitle[column.id] = '';
      this.newTaskDescription[column.id] = '';
      this.newTaskDeadline[column.id] = '';
      this.loadColumnsAndTasks();
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
        this.loadColumnsAndTasks();
      });
    }
  }

  startEditColumn(column: TaskColumn) {
    this.editingColumn = { ...column };
    this.editingColumnName = column.name;
  }

  confirmEditColumn() {
    if (!this.editingColumn || !this.editingColumnName.trim()) return;
    this.columnService.updateColumn(this.editingColumn.id, {
      id: this.editingColumn.id,
      name: this.editingColumnName,
      board: { id: this.boardId, name: '' }
    }).subscribe(() => {
      this.editingColumn = null;
      this.editingColumnName = '';
      this.loadColumnsAndTasks();
    });
  }

  cancelEditColumn() {
    this.editingColumn = null;
    this.editingColumnName = '';
  }

  deleteColumn(columnId: number) {
    if (!confirm('Are you sure you want to delete this column?')) return;
    this.columnService.deleteColumn(columnId).subscribe(() => {
      this.loadColumnsAndTasks();
    });
  }
} 