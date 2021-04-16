import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DefaultComponent} from './layouts/default/default.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ApiModule as CommunicationApiModule} from '../app/modules/generated/communication/api.module';
import {ApiModule as DashboardApiModule} from '../app/modules/generated/dashboard/api.module';
import {ApiModule as LinksApiModule} from '../app/modules/generated/links/api.module';
import {ApiModule as ProjectApiModule} from '../app/modules/generated/project/api.module';
import {ApiModule as SoftwareApiModule} from '../app/modules/generated/software/api.module';
import {ApiModule as TimesApiModule} from '../app/modules/generated/times/api.module';
import {ApiModule as UserApiModule} from '../app/modules/generated/user/api.module';
import {HttpInterceptorService} from './httpInterceptor.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './modules/login/login.component';
import {LogoutComponent} from './modules/logout/logout.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LoginActivate} from './loginActivate.guard';

import {ToolbarModule} from 'primeng/toolbar';
import {MenuModule} from 'primeng/menu';
import {SidebarModule} from 'primeng/sidebar';
import {ButtonModule} from 'primeng/button';
import {TreeTableModule} from 'primeng/treetable';
import {TabViewModule} from 'primeng/tabview';
import {InputTextModule} from 'primeng/inputtext';
import {DropdownModule} from 'primeng/dropdown';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {CalendarModule} from 'primeng/calendar';
import {CheckboxModule} from 'primeng/checkbox';
import {TableModule} from 'primeng/table';
import {PickListModule} from 'primeng/picklist';
import {PasswordModule} from 'primeng/password';
import {ListboxModule} from 'primeng/listbox';
import {InputNumberModule} from "primeng/inputnumber";
import {ChartModule} from "primeng/chart";


import {environment} from '../environments/environment';
import {BASE_PATH as COMMUNICATION_BASE_PATH} from './modules/generated/communication';
import {BASE_PATH as DASHBOARD_BASE_PATH} from './modules/generated/dashboard';
import {BASE_PATH as LINKS_BASE_PATH} from './modules/generated/links';
import {BASE_PATH as PROJECT_BASE_PATH} from './modules/generated/project';
import {BASE_PATH as SOFTWARE_BASE_PATH} from './modules/generated/software';
import {BASE_PATH as TIMES_BASE_PATH} from './modules/generated/times';
import {BASE_PATH as USER_BASE_PATH} from './modules/generated/user';
import {SoftwareComponent} from './modules/software/software.component';
import {SoftwareDetailsComponent} from './modules/softwaredetails/softwaredetails.component';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FlexLayoutModule} from '@angular/flex-layout';
import {HeaderComponent} from './shared/components/header/header.component';
import {FooterComponent} from './shared/components/footer/footer.component';
import {NodeService} from './modules/software/nodeservice';
import {SoftwaremetricsComponent} from "./modules/softwaremetrics/softwaremetrics.component";
import {DashboardComponent} from "./modules/dashboard/dashboard.component";


@NgModule({
  declarations: [
    AppComponent,
    DefaultComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    LogoutComponent,
    SoftwareComponent,
    SoftwareDetailsComponent,
    SoftwaremetricsComponent,
    DashboardComponent
  ],
  imports: [
    CommunicationApiModule,
    DashboardApiModule,
    LinksApiModule,
    ProjectApiModule,
    SoftwareApiModule,
    TimesApiModule,
    UserApiModule,

    HttpClientModule,
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,

    CommonModule,
    RouterModule,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,

    ToolbarModule,
    MenuModule,
    SidebarModule,
    ButtonModule,
    TreeTableModule,
    TabViewModule,
    InputTextModule,
    InputTextareaModule,
    DropdownModule,
    CalendarModule,
    CheckboxModule,
    TableModule,
    PickListModule,
    PasswordModule,
    ListboxModule,
    ChartModule,
    InputNumberModule,

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    {
      provide: COMMUNICATION_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: DASHBOARD_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: LINKS_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: PROJECT_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: SOFTWARE_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: TIMES_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },
    {
      provide: USER_BASE_PATH,
      useValue: environment.API_BASE_PATH
    },

    LoginActivate,
    NodeService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {


}
