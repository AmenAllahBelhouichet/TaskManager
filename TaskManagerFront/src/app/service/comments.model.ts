export interface Comment {
  id: number;
  content: string;
  createdAt?: string;
  updatedAt?: string;
  author?: { id: number; username?: string; email?: string };
  task?: { id: number };
} 