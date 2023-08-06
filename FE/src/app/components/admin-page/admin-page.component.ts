import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllResponse } from 'src/app/dtos/response/GetAllResponse';
import { OrganizzatoreService } from 'src/app/services/organizzatore.service';
import { TuristaService } from 'src/app/services/turista.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
  protected usersList: GetAllResponse[] = [];
  protected showUsersList: boolean = false;
  protected allCheckboxesSelected: boolean = false;
  protected usernameTurista: string = '';
  protected usernameOrganizzatore: string = '';

  constructor(
    private turistaService: TuristaService,
    private organizzatoreService: OrganizzatoreService,
    private router: Router,
    private toastr: ToastrService) { }

  ngOnInit(): void {
    if (localStorage.getItem('ruolo')?.toString().trim().toUpperCase() !== 'ADMIN') {
      this.router.navigateByUrl("login");
      localStorage.clear();
      this.toastr.error("Utente non autorizzato");
    }
  }

  getAllTuristi(): void {
    //chiamo il backend per prendere tutti gli utenti con ruolo turista
    this.turistaService.getAllTurista().subscribe({
      next: (res: GetAllResponse[]) => {
        this.compilaUsersList(res);
        this.toggleModalUsersList();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error("Errore nel caricamento dei turisti");
      }
    })
  }

  getAllOrganizzatori(): void {
    //chiamo il backend per prendere tutti gli utenti con ruolo organizzatore
    this.organizzatoreService.getAllOrganizzatore().subscribe({
      next: (res: GetAllResponse[]) => {
        this.compilaUsersList(res);
        this.toggleModalUsersList();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error("Errore nel caricamento degli organizzatori");
      }
    })
  }

  toggleModalUsersList(): void {
    this.showUsersList = !this.showUsersList;
  }

  compilaUsersList(res: GetAllResponse[]): void {
    this.usersList = [];
    for (const user of res) {
      this.usersList.push(user);
    }
  }

  goToAccountPage(username: string): void {
    this.router.navigateByUrl('homepage/account/' + username);
  }

  cercaPerUsername(ruolo: string): void {
    if (ruolo === 'TURISTA') {
      if (this.usernameTurista === '' || this.usernameTurista === null || this.usernameTurista === undefined) {
        this.toastr.warning("Il campo non può essere vuoto");
      } else {
        this.goToAccountPage(this.usernameTurista);
      }

    } else if (ruolo === 'ORGANIZZATORE') {
      if (this.usernameOrganizzatore === '' || this.usernameOrganizzatore === null || this.usernameOrganizzatore === undefined) {
        this.toastr.warning("Il campo non può essere vuoto");
      } else {
        this.goToAccountPage(this.usernameOrganizzatore);
      }
    }
  }
}
