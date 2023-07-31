import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  urlPath: string = '';
  showLogoutModal: boolean = false;
  ruolo?: string = '';

  constructor(private router: Router, private authService: AuthService, private toastr: ToastrService) {
    router.events.subscribe((val) => {
      if (val instanceof NavigationEnd) {
        this.pathName();
      }
    })
  }

  ngOnInit(): void {
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
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
