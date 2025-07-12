export interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  columnId?: number;
  // Add other fields as needed based on your backend Task entity
} 