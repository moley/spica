import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/modules/login/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Output() toggleSideBarForMe: EventEmitter<any> = new EventEmitter();

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  ngOnInit() { }

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
