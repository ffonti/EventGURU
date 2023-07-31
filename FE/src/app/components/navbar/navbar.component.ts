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
      //ogni volta che cambia il path del sito, viene salvato in una variabile
      if (val instanceof NavigationEnd) {
        this.pathName();
      }
    })
  }

  ngOnInit(): void {
    //prendo il ruolo dell'utente per visualizzare la navbar custom
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
  }

  pathName(): void {
    //assegno il path del sito ad una variabile
    this.urlPath = this.router.url;
  }

  toggleLogoutModal(): void {
    //cambio la visualizzazione della modale
    this.showLogoutModal = !this.showLogoutModal;
  }

  logout(): void {
    //pulisco il localstorage e mando l'utente al login
    this.authService.logout();
    this.toastr.success('Logout effettuato');
    this.router.navigateByUrl('login');
  }
}
