import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/modules/login/auth.service';
import {MenuItem, PrimeIcons} from 'primeng/api';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent  {

  @Output() toggleSideBarForMe: EventEmitter<any> = new EventEmitter();

  sidebarVisible;


  constructor(private authenticationService: AuthenticationService, private router: Router) { }


  menu () : MenuItem[]  {
    if (this.isLoggedIn()) {
       return  [{
         label: 'Logout', 
         command: (event) => {
          return this.handleLogout();
        }
       }];
    }
    else {
      return  [{
        label: 'Login', 
        
        routerLink: "/login"
      }];
    }

  }

  toggleSideBar() {
    this.toggleSideBarForMe.emit();
    setTimeout(() => {
      window.dispatchEvent(
        new Event('resize')
      );
    }, 300);
  }

  handleLogout() {
    console.log ("Lgout called (" + this.authenticationService.isUserLoggedIn() + ")")
    this.authenticationService.logout()
    this.router.navigate(['/login']);
  }

  isLoggedIn () {
    return this.authenticationService.isUserLoggedIn();
  }

}
