export interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  columnId?: number;
  taskColumn?: { id: number };
  deadline?: Date;
  assignTo?: { id: number };
  // Add other fields as needed based on your backend Task entity
} 