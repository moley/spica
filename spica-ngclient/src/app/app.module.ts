import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DefaultModule } from './layouts/default/default.module';
import { ApiModule as CommunicationApiModule } from '../app/modules/generated/communication/api.module';
import { ApiModule as DashboardApiModule } from '../app/modules/generated/dashboard/api.module';
import { ApiModule as LinksApiModule } from '../app/modules/generated/links/api.module';
import { ApiModule as ProjectApiModule } from '../app/modules/generated/project/api.module';
import { ApiModule as SoftwareApiModule } from '../app/modules/generated/software/api.module';
import { ApiModule as TimesApiModule } from '../app/modules/generated/times/api.module';
import { ApiModule as UserApiModule } from '../app/modules/generated/user/api.module';
import { HttpInterceptorService } from './httpInterceptor.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginComponent } from './modules/login/login.component';
import { LogoutComponent } from './modules/logout/logout.component';
import { FormsModule } from '@angular/forms';
import { LoginActivate } from './loginActivate.guard';

import { environment } from '../environments/environment';



@NgModule({
  declarations: [
    AppComponent
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
    DefaultModule
  ],
  providers: [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpInterceptorService,
        multi: true
      },
      LoginActivate
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
