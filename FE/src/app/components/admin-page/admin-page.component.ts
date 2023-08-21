import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllResponse } from 'src/app/dtos/response/GetAllResponse';
import { OrganizzatoreService } from 'src/app/services/organizzatore.service';
import { TuristaService } from 'src/app/services/turista.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
  protected usersList: GetAllResponse[] = []; //inizializzo la lista di utenti da visualizzare
  protected showUsersList: boolean = false;
  protected usernameTurista: string = '';
  protected usernameOrganizzatore: string = '';
  protected nomeEvento: string = '';

  //costruttore dove istanzio le classi con cui interagire
  constructor(
    private turistaService: TuristaService,
    private organizzatoreService: OrganizzatoreService,
    private router: Router,
    private toastr: ToastrService) { }

  //metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    this.isAdmin(); //controllo il ruolo    
  }

  //per visualizzare tutti i turisti registrati
  getAllTuristi(): void {
    this.isAdmin(); //controllo il ruolo

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

  //per visualizzare tutti gli organizzatori registrati
  getAllOrganizzatori(): void {
    this.isAdmin(); //controllo il ruolo

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

  //per visualizzare la modale
  toggleModalUsersList(): void {
    this.isAdmin(); //controllo il ruolo

    this.showUsersList = !this.showUsersList;
  }

  //per assegnare alla variabile la lista di utenti da visualizzare
  compilaUsersList(res: GetAllResponse[]): void {
    this.isAdmin(); //controllo il ruolo

    this.usersList = [];
    for (const user of res) {
      this.usersList.push(user);
    }
  }

  //per andare alla pagina dove è possibile modificare i dati di un utente
  goToAccountPage(username: string): void {
    this.isAdmin(); //controllo il ruolo

    this.router.navigateByUrl('homepage/account/' + username);
  }

  //per andare alla pagina dove è possibile visualizzare tutti gli eventi
  goToEsploraPage(): void {
    this.isAdmin();

    this.router.navigateByUrl('homepage/esplora');
  }

  //per cercare un evento e visualizzarne i dati per poterli modificare
  cercaEventoPerNome(): void {
    this.isAdmin(); //controllo il ruolo

    if (this.nomeEvento === '' || this.nomeEvento === null || this.nomeEvento === undefined) {
      this.toastr.warning("Il campo non può essere vuoto");
    } else {
      //TODO Event page con modale evento aperta per modifica
    }
  }

  //per andare alla pagina dove è possibile modificare i dati di un utente, divisi per organizzatore o turista
  cercaPerUsername(ruolo: string): void {
    this.isAdmin(); //controllo il ruolo

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

  //controllo se si tratta veramente di un admin, in caso di contrario, logout
  isAdmin(): void {
    if (localStorage.getItem('ruolo')?.toString().trim().toUpperCase() !== 'ADMIN') {
      this.router.navigateByUrl("login");
      localStorage.clear();
      this.toastr.error("Utente non autorizzato");
    }
  }
}
