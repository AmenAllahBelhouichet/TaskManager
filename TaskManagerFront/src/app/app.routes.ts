import { Routes } from '@angular/router';
import { LoginComponent } from './component/login.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { SignupComponent } from './component/signup.component';
import { AdminDashboardComponent } from './component/admin-dashboard.component';
import { BoardsPageComponent } from './component/boards-page.component';
import { TasksPageComponent } from './component/tasks-page.component';
import { AiAgentManagementComponent } from './component/ai-agent-management.component';
import { AdminLayoutComponent } from './component/admin-layout.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'dashboard', component: DashboardComponent },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      { path: '', component: AdminDashboardComponent },
      { path: 'ai-agents', component: AiAgentManagementComponent }
    ]
  },
  { path: 'boards/:projectId', component: BoardsPageComponent },
  { path: 'tasks/:boardId', component: TasksPageComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
