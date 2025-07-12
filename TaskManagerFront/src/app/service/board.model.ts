import { Project } from './project.model';

export interface Board {
  id: number;
  name: string;
  project?: Project;
} 