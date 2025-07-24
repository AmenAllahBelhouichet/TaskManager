import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TaskColumnService } from '../service/task-column.service';
import { TaskService } from '../service/task.service';
import { TaskColumn } from '../service/task-column.model';
import { Task } from '../service/task.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { User } from '../service/user.model';
import { UserService } from '../service/user.service';
import { Comment } from '../service/comments.model';
import { CommentsService } from '../service/comments.service';
import { NavbarComponent } from './navbar.component';
import { ProjectService } from '../service/project.service';
import { BoardService } from '../service/board.service';
@Component({
  selector: 'app-tasks-page',
  templateUrl: './tasks-page.component.html',
  styleUrls: ['./tasks-page.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule, NavbarComponent]
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
  users: User[] = [];
  editingTask: Task | null = null;
  editingTaskTitle = '';
  editingTaskDescription = '';
  editingTaskDeadline = '';
  editingTaskAssignToId: number | null = null;
  showAddTaskModal: boolean = false;
  addTaskColumn: TaskColumn | null = null;
  consultingTask: Task | null = null;
  showConsultTaskModal: boolean = false;
  comments: Comment[] = [];
  newCommentContent: string = '';
  editingCommentId: number | null = null;
  editingCommentContent: string = '';
  currentUserId: number | null = null;
  newTaskAssignToId: { [columnId: number]: number | null } = {};

  constructor(
    private route: ActivatedRoute,
    private columnService: TaskColumnService,
    private taskService: TaskService,
    private userService: UserService,
    private commentsService: CommentsService,
    private boardService: BoardService,
    private projectService: ProjectService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.boardId = Number(params.get('boardId'));
      this.loadColumnsAndTasks();
      // Fetch board to get projectId, then fetch team members
      this.teamMembersLoading = true;
      this.boardService.getBoardById(this.boardId).subscribe(board => {
        const projectId = board.project?.id;
        if (projectId) {
          this.projectService.getTeamMembersByProjectId(projectId).subscribe({
            next: members => {
              this.teamMembers = members.map((m: any) => ({
                id: m.user?.id,
                name: m.user?.name,
                email: m.user?.email,
                role: m.teamRole
              }));
              this.teamMembersLoading = false;
            },
            error: () => {
              this.teamMembers = [];
              this.teamMembersLoading = false;
            }
          });
        } else {
          this.teamMembers = [];
          this.teamMembersLoading = false;
        }
      }, () => {
        this.teamMembers = [];
        this.teamMembersLoading = false;
      });
    });
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
    });
    // Set currentUserId from localStorage
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        this.currentUserId = user.id;
      } catch (e) {
        this.currentUserId = null;
      }
    }
  }
  teamMembers: { id: number; name: string; email: string; role: string }[] = [];
  teamMembersLoading = false;
  showTeamMembersBtn = false;
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

  openAddTaskModal(column: TaskColumn) {
    this.addTaskColumn = column;
    this.showAddTaskModal = true;
  }

  closeAddTaskModal() {
    this.showAddTaskModal = false;
    this.addTaskColumn = null;
  }

  createTaskFromModal() {
    if (!this.addTaskColumn) return;
    const column = this.addTaskColumn;
    const title = this.newTaskTitle[column.id];
    const description = this.newTaskDescription[column.id];
    const deadline = this.newTaskDeadline[column.id];
    const assignToId = this.newTaskAssignToId[column.id];
    if (!title || !title.trim()) return;
    this.taskService.createTask({
      id: 0,
      title,
      description,
      status: column.name,
      deadline: deadline ? new Date(deadline) : undefined,
      taskColumn: { id: column.id },
      assignTo: assignToId ? ({ id: assignToId } as User) : undefined
    }).subscribe(task => {
      this.showAddTaskModal = false;
      this.addTaskColumn = null;
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

  startEditTask(task: Task) {
    this.editingTask = { ...task };
    this.editingTaskTitle = task.title;
    this.editingTaskDescription = task.description;
    this.editingTaskDeadline = task.deadline ? (typeof task.deadline === 'string' ? (task.deadline as string).substring(0, 10) : new Date(task.deadline as any).toISOString().substring(0, 10)) : '';
    this.editingTaskAssignToId = task.assignTo?.id || null;
  }

  confirmEditTask() {
    if (!this.editingTask) return;
    const updatedTask: Task = {
      ...this.editingTask,
      title: this.editingTaskTitle,
      description: this.editingTaskDescription,
      deadline: this.editingTaskDeadline ? new Date(this.editingTaskDeadline) : undefined,
      assignTo: this.editingTaskAssignToId ? ({ id: this.editingTaskAssignToId } as User) : undefined
    };
    this.taskService.updateTask(updatedTask.id, updatedTask).subscribe(() => {
      this.editingTask = null;
      this.loadColumnsAndTasks();
    });
  }

  cancelEditTask() {
    this.editingTask = null;
  }

  deleteTask(task: Task) {
    if (!confirm('Are you sure you want to delete this task?')) return;
    this.taskService.deleteTask(task.id).subscribe(() => {
      this.loadColumnsAndTasks();
    });
  }

  onTaskDoubleClick(task: Task) {
    this.consultingTask = task;
    this.showConsultTaskModal = true;
    this.fetchCommentsForTask(task.id);
  }

  closeConsultTaskModal() {
    this.showConsultTaskModal = false;
    this.consultingTask = null;
    this.comments = [];
    this.newCommentContent = '';
    this.editingCommentId = null;
    this.editingCommentContent = '';
  }

  fetchCommentsForTask(taskId: number) {
    this.commentsService.getAllComments().subscribe(comments => {
      this.comments = comments.filter(c => c.task && c.task.id === taskId);
    });
  }

  addComment() {
    if (!this.consultingTask || !this.newCommentContent.trim() || !this.currentUserId) return;
    const comment: Comment = {
      id: 0,
      content: this.newCommentContent,
      author: { id: this.currentUserId },
      task: { id: this.consultingTask.id }
    };
    this.commentsService.createComment(comment).subscribe({
      next: () => {
        this.newCommentContent = '';
        this.fetchCommentsForTask(this.consultingTask!.id);
      },
      error: err => {
        alert('Failed to add comment: ' + (err.error || err.message));
      }
    });
  }

  startEditComment(comment: Comment) {
    this.editingCommentId = comment.id;
    this.editingCommentContent = comment.content;
  }

  confirmEditComment(comment: Comment) {
    if (!this.editingCommentContent.trim()) return;
    const updated: Comment = { ...comment, content: this.editingCommentContent };
    this.commentsService.updateComment(comment.id, updated).subscribe(() => {
      this.editingCommentId = null;
      this.editingCommentContent = '';
      this.fetchCommentsForTask(this.consultingTask!.id);
    });
  }

  cancelEditComment() {
    this.editingCommentId = null;
    this.editingCommentContent = '';
  }

  deleteComment(comment: Comment) {
    if (!confirm('Delete this comment?')) return;
    this.commentsService.deleteComment(comment.id).subscribe(() => {
      this.fetchCommentsForTask(this.consultingTask!.id);
    });
  }

  get connectedDropListsIds(): string[] {
    return this.columns.map(col => 'column-' + col.id);
  }

  get assignableMembers() {
    return this.teamMembers.filter(m => m.role === 'MEMBER');
  }

  refreshTasks() {
    this.loadColumnsAndTasks();
  }
} 