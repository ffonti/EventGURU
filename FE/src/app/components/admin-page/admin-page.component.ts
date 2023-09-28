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
  //inizializzo la lista di utenti da visualizzare
  protected usersList: GetAllResponse[] = [];
  protected showUsersList: boolean = false;
  protected usernameTurista: string = '';
  protected usernameOrganizzatore: string = '';
  protected nomeEvento: string = '';
  //variabili per il filtro
  protected isIdArrowUp: boolean = true;
  protected isNomeArrowUp: boolean = false;
  protected isCognomeArrowUp: boolean = false;
  protected isUsernameArrowUp: boolean = false;
  protected isEmailArrowUp: boolean = false;
  protected isNewsletterArrowUp: boolean = false;

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
        //aggiungo tutti i dati a delle variabili così da poterli visualizzare
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

    //sorting in ordine crescente
    this.usersList.sort((a, b) => {
      if (a.userId > b.userId) {
        return 1;
      } else if (a.userId < b.userId) {
        return -1;
      } else {
        return 0;
      }
    });
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

  //Ordina gli utenti per id (crescente o decrescente)
  toggleId(ordine: string): void {
    this.isIdArrowUp = !this.isIdArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.userId > a.userId) {
          return 1;
        } else if (b.userId < a.userId) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.userId > b.userId) {
          return 1;
        } else if (a.userId < b.userId) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }

  //Ordina gli utenti per nome (crescente o decrescente)
  toggleNome(ordine: string): void {
    this.isNomeArrowUp = !this.isNomeArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.nome > a.nome) {
          return 1;
        } else if (b.nome < a.nome) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.nome > b.nome) {
          return 1;
        } else if (a.nome < b.nome) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }

  //Ordina gli utenti per cognome (crescente o decrescente)
  toggleCognome(ordine: string): void {
    this.isCognomeArrowUp = !this.isCognomeArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.cognome > a.cognome) {
          return 1;
        } else if (b.cognome < a.cognome) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.cognome > b.cognome) {
          return 1;
        } else if (a.cognome < b.cognome) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }

  //Ordina gli utenti per username (crescente o decrescente)
  toggleUsername(ordine: string): void {
    this.isUsernameArrowUp = !this.isUsernameArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.username > a.username) {
          return 1;
        } else if (b.username < a.username) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.username > b.username) {
          return 1;
        } else if (a.username < b.username) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }

  //Ordina gli utenti per email (crescente o decrescente)
  toggleEmail(ordine: string): void {
    this.isEmailArrowUp = !this.isEmailArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.email > a.email) {
          return 1;
        } else if (b.email < a.email) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.email > b.email) {
          return 1;
        } else if (a.email < b.email) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }

  //Ordina gli utenti in base all'iscrizione alla newsletter
  toggleNewsletter(ordine: string): void {
    this.isNewsletterArrowUp = !this.isNewsletterArrowUp;

    if (ordine === 'DECRESCENTE') {
      this.usersList.sort((a, b) => {
        if (b.iscrittoNewsletter > a.iscrittoNewsletter) {
          return 1;
        } else if (b.iscrittoNewsletter < a.iscrittoNewsletter) {
          return -1;
        } else {
          return 0;
        }
      });
    } else if (ordine === 'CRESCENTE') {
      this.usersList.sort((a, b) => {
        if (a.iscrittoNewsletter > b.iscrittoNewsletter) {
          return 1;
        } else if (a.iscrittoNewsletter < b.iscrittoNewsletter) {
          return -1;
        } else {
          return 0;
        }
      });
    }
  }
}
