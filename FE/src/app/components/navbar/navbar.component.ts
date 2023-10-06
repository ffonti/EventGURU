import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

/**
 * componente per la navbar. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  protected urlPath: string = '';
  protected showLogoutModal: boolean = false;
  protected ruolo?: string = '';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private router: Router, private authService: AuthService, private toastr: ToastrService) {
    router.events.subscribe((val) => {
      //ogni volta che cambia il path del sito, viene salvato in una variabile
      if (val instanceof NavigationEnd) {
        this.pathName();
      }
    })
  }

  //metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    //prendo il ruolo dell'utente per visualizzare la navbar custom
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
  }

  //assegno il path del sito ad una variabile
  pathName(): void {
    this.urlPath = this.router.url;
  }

  //cambio la visualizzazione della modale
  toggleLogoutModal(): void {
    this.showLogoutModal = !this.showLogoutModal;
  }

  //pulisco il localstorage e mando l'utente al login
  logout(): void {
    this.authService.logout();
  }
}
