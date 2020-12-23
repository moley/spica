import { HttpClient } from '@angular/common/http';
import { Inject, Injectable }                      from '@angular/core';
import { Router} from '@angular/router';
import { map } from 'rxjs/operators';
import { BASE_PATH } from '../generated/links'; //todo make our own constant


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'

  public username: String;
  public password: String;

  constructor(@Inject(BASE_PATH) private basePath: string, private router: Router, private http: HttpClient) {

  }

  authenticationService(username: String, password: String) {
    console.log ("Authenticate user " + username + " via " + this.basePath)
    return this.http.get(this.basePath + `/basicauth`,
      { headers: { authorization: this.createBasicAuthToken(username, password) } }).pipe(map((res) => {
        this.username = username;
        this.password = password;
        this.registerSuccessfulLogin(username, password);
      }));
  }

  createBasicAuthToken(username: String, password: String) {
    return 'Basic ' + window.btoa(username + ":" + password)
  }

  registerSuccessfulLogin(username, password) {
    sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username)
  }

  logout() {
    sessionStorage.removeItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
    this.http.get(this.basePath + `/logout`).toPromise().then(response => {
      this.username = null;
      this.password = null;
      this.router.navigate(['/']);
    });

    
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME)
    if (user === null) return false
    return true
  }

  getLoggedInUserName() {
    let user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME)
    if (user === null) return ''
    return user
  }
}
