import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  urlPath: string = '';
  showLogoutModal: boolean = false;

  constructor(private router: Router, private authService: AuthService, private toastr: ToastrService) {
    router.events.subscribe((val) => {
      if (val instanceof NavigationEnd) {
        this.pathName();
      }
    })
  }

  pathName(): void {
    this.urlPath = this.router.url;
  }

  toggleLogoutModal(): void {
    this.showLogoutModal = !this.showLogoutModal;
  }

  logout(): void {
    this.authService.logout();
    this.toastr.success('Logout effettuato');
    this.router.navigateByUrl('login');
  }
}
