import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from './modules/login/auth.service';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

    constructor(private authenticationService: AuthenticationService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.authenticationService.isUserLoggedIn() && req.url.indexOf('basicauth') === -1) {
            console.log("Send request with bearer token" + this.authenticationService.getLoggedInUserName() + "-" + this.authenticationService.getLoggedInPassword())
            const authReq = req.clone({
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    'Authorization': `Basic ${window.btoa(this.authenticationService.getLoggedInUserName() + ":" + this.authenticationService.getLoggedInPassword())}`
                })
            });
            return next.handle(authReq);
        } else {
            console.log("Send request without bearer token")
            return next.handle(req);
        }
    }
}
