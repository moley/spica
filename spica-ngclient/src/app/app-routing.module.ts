import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DefaultComponent } from './layouts/default/default.component';
import { LoginActivate } from './loginActivate.guard';
import { DashboardComponent } from './modules/dashboard/dashboard.component';
import { LoginComponent } from './modules/login/login.component';
import { SoftwareComponent } from './modules/software/software.component';
import { SoftwareDetailsComponent } from './modules/softwaredetails/softwaredetails.component';

const routes: Routes = [{
  path: '',
  component: DefaultComponent,
  children: [{
    path: 'login',
    component: LoginComponent
  }, {
    path: 'app',
    component: DashboardComponent,
    canActivate:[LoginActivate]
  }, {
    path: 'app/software',
    component: SoftwareComponent, 
    canActivate:[LoginActivate]
  }, {
    path: 'app/software/:id',
    component: SoftwareDetailsComponent, 
    canActivate:[LoginActivate]
  }]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
