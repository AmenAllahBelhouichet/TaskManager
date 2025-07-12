import { Board } from './board.model';
import { User } from './user.model';

export interface Project {
  id: number;
  name: string;
  owner?: User;
  boards?: Board[];
} 