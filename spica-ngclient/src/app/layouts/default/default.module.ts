import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultComponent } from './default.component';
import { RouterModule } from '@angular/router';
import { SoftwareComponent } from 'src/app/modules/software/software.component';
import { SharedModule } from 'src/app/shared/shared.module';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatTableModule} from '@angular/material/table';
import {MatTreeModule} from '@angular/material/tree';
import {MatTabsModule} from '@angular/material/tabs';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SoftwareDetailsComponent } from 'src/app/modules/softwaredetails/softwaredetails.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from 'src/app/modules/login/login.component';
import { LogoutComponent } from 'src/app/modules/logout/logout.component';

@NgModule({
  declarations: [
    DefaultComponent,
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
    MatListModule, 
    MatTabsModule, 
    MatGridListModule,
    MatCheckboxModule , 
    FormsModule, 
    ReactiveFormsModule,
    MatFormFieldModule,        
  ],
})
export class DefaultModule { }
