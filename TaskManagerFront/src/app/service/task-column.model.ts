import { Board } from './board.model';
 
export interface TaskColumn {
  id: number;
  name: string;
  board: Board;
} 