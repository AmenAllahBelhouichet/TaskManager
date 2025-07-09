import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './service/auth.interceptor';
// Import other components as needed
import { AdminDashboardComponent } from './component/admin-dashboard.component';
import { LoginComponent } from './component/login.component';
import { SignupComponent } from './component/signup.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminDashboardComponent,
    LoginComponent,
    SignupComponent
    // Add other components here
  ],
  imports: [
    BrowserModule,
    HttpClientModule
    // Add other modules here
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { } 