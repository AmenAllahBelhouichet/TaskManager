export interface Comment {
  id: number;
  content: string;
  createdAt?: string;
  updatedAt?: string;
  createdDate?: string;
  author?: { id: number; username?: string; email?: string };
  task?: { id: number };
} 