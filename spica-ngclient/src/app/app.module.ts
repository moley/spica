import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DefaultModule } from './layouts/default/default.module';
import { ApiModule } from '../app/modules/generated/api.module';
import { HttpInterceptorService } from './httpInterceptor.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginComponent } from './modules/login/login.component';
import { LogoutComponent } from './modules/logout/logout.component';
import { FormsModule } from '@angular/forms';
import { LoginActivate } from './loginActivate.guard';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    ApiModule,
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
