import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultComponent } from './default.component';
import { DashboardComponent } from 'src/app/modules/dashboard/dashboard.component';
import { RouterModule } from '@angular/router';
import { SoftwareComponent } from 'src/app/modules/software/software.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatSidenavModule, MatDividerModule, MatCardModule, MatPaginatorModule, MatTableModule,
         MatInputModule, MatSelectModule, MatButtonModule, MatDatepickerModule, MatNativeDateModule, MatListModule, MatTabsModule, MatCheckboxModule, MatGridListModule, MatFormFieldModule, MatTreeModule, MatIconModule } from '@angular/material';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DashboardService } from 'src/app/modules/dashboard.service';
import { SoftwareDetailsComponent } from 'src/app/modules/softwaredetails/softwaredetails.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from 'src/app/modules/login/login.component';
import { LogoutComponent } from 'src/app/modules/logout/logout.component';

@NgModule({
  declarations: [
    DefaultComponent,
    DashboardComponent,
    LoginComponent, 
    LogoutComponent,
    SoftwareComponent,
    SoftwareDetailsComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    MatSidenavModule,
    MatDividerModule,
    FlexLayoutModule,
    MatCardModule,
    MatPaginatorModule,
    MatTableModule,
    MatIconModule, 
    MatTreeModule, 
    MatInputModule,
    MatSelectModule,
    MatButtonModule, 
    MatDatepickerModule,        
    MatNativeDateModule,
    MatListModule, 
    MatTabsModule, 
    MatGridListModule,
    MatCheckboxModule , 
    FormsModule, 
    ReactiveFormsModule,
    MatFormFieldModule,        
  ],
  providers: [
    DashboardService
  ]
})
export class DefaultModule { }
