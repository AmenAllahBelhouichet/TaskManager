import { Routes } from '@angular/router';
import { LoginComponent } from './component/login.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { SignupComponent } from './component/signup.component';
import { AdminDashboardComponent } from './component/admin-dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
