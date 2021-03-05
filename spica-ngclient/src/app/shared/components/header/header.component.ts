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

  sidebarVisible:boolean;
  


  ngOnInit () {
    
    
  }
  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  userMenuItems () : MenuItem[] {
  if (this.isLoggedIn()) {
      return [{label: 'Logout', icon: 'pi pi-fw pi-plus', command: (event) => {this.handleLogout();}}
             ];
       
    }
    else {
      return [{label: 'Github', icon: 'pi pi-fw pi-plus', url: 'https://github.com/moley/spica'},  
              {label: 'Login', icon: 'pi pi-fw pi-plus', command: (event) => {this.stepToLogin();}}
             ];
              
    }
  }

  stepToLogin () {
    console.log ("Step to login")
    this.router.navigate(['/login']);

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
    console.log ("Logout called (" + this.authenticationService.isUserLoggedIn() + ")")
    this.authenticationService.logout()
    this.router.navigate(['/login']);
  }

  isLoggedIn () {
    return this.authenticationService.isUserLoggedIn();
  }

  
}
