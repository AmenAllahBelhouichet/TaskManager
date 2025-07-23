import { User } from './user.model';

export interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  columnId?: number;
  taskColumn?: { id: number };
  deadline?: Date;
  assignTo?: User;
  // Add other fields as needed based on your backend Task entity
} 