import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetUserDataResponse } from 'src/app/dtos/response/GetUserDataResponse';
import { MessageResponse } from 'src/app/dtos/response/MessageResponse';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  //creo un nuovo tipo con i dati dell'utente
  protected userData: any = {
    username: '',
    nome: '',
    cognome: '',
    nuovaPassword: '',
    email: '',
    vecchiaPassword: '',
    iscrittoNewsletter: false
  };
  protected ripeti_password: string = '';

  protected username: string = '';
  protected oldUsername: string = '';

  //per gestire le icone
  protected showPassword: boolean = false;
  protected showVecchiaPassword: boolean = false;
  protected showRepeatPassword: boolean = false;

  //per gestire le modali
  protected showModalEliminaAccount: boolean = false;

  //costruttore dove istanzio le classi con cui interagire
  constructor(private userService: UserService, private toastr: ToastrService, private router: Router) { }

  //metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    this.username = ''; //inizzializzo l'username
    this.oldUsername = '';

    //se nell'url è presente un username, vuol dire che un admin vuole visualizzare i dati di un altro utente
    this.username = this.router.url.split('/homepage/account/')[1];
    if (this.username !== null && this.username !== undefined && this.username !== '') {
      this.getAdminUserData(this.username); //chiamo il metodo adatto per richiedere i dati di un altro utente
      return;
    }

    //chiamo il server per chiedere i dati dell'utente in sessione
    this.userService.getUserData().subscribe({
      next: (res: GetUserDataResponse) => {
        //in caso di successo compilo il form
        this.compilaCampi(res);
      },
      error: (err: any) => {
        //in caso di errore mando l'utente al login
        console.log(err);
        this.toastr.error('Eseguire nuovamente il login');
        this.router.navigateByUrl('login');
      }
    })
  }

  //per gestire la visualizzazione dell'icona
  toggleShowPassword(type: string): void {
    switch (type) {
      case 'repeat':
        this.showRepeatPassword = !this.showRepeatPassword;
        break;
      case 'no-repeat':
        this.showPassword = !this.showPassword;
        break;
      case 'vecchia':
        this.showVecchiaPassword = !this.showVecchiaPassword;
        break;
    }
  }

  //per visualizzare i messaggi di errore
  ripetiPasswordNotOk(): boolean {
    return (this.userData.nuovaPassword !== this.ripeti_password);
  }

  //per aggiornare i dati dell'utente
  updateUserData(): void {

    this.oldUsername = this.router.url.split('/homepage/account/')[1];

    //caso in cui un admin modifica i dati di un altro utente
    if (this.oldUsername !== null && this.oldUsername !== undefined && this.oldUsername !== '') {
      this.userService.adminUpdateUserData(this.userData.nome, this.userData.cognome, this.userData.email, this.userData.username, this.userData.vecchiaPassword, this.userData.nuovaPassword, this.userData.iscrittoNewsletter, this.oldUsername).subscribe({
        next: (res: GetUserDataResponse) => {
          this.toastr.success("Dati modificati con successo");
        },
        error: (err: any) => {
          console.log(err);
          this.toastr.error(err.error.message);
        }
      });

    } else {
      //chiamo il server per aggiornare i dati dell'utente
      this.userService.updateUserData(this.userData.nome, this.userData.cognome, this.userData.email, this.userData.username, this.userData.vecchiaPassword, this.userData.nuovaPassword, this.userData.iscrittoNewsletter).subscribe({
        next: (res: GetUserDataResponse) => {
          this.router.navigateByUrl('login');
          this.toastr.success("Dati modificati con successo");
          this.toastr.info("Eseguire nuovamente il login");
        },
        error: (err: any) => {
          console.log(err);
          this.toastr.error(err.error.message);
        }
      });
    }
  }

  //per visualizzare i messaggi di errore
  vecchiaENuovaPasswordUguali(): boolean {
    return (this.userData.vecchiaPassword === this.userData.nuovaPassword && this.userData.vecchiaPassword !== '');
  }

  //per visualizzare la modale
  toggleModalEliminaAccount(): void {
    this.showModalEliminaAccount = !this.showModalEliminaAccount;
  }

  //per eliminare un account
  eliminaAccount(): void {
    //se è presente un username nell'url, si tratta di un admin che vuole eliminare un altro account
    this.username = this.router.url.split('/homepage/account/')[1];
    if (this.username !== null && this.username !== undefined && this.username !== '') {
      this.adminEliminaUser(this.username);
      return;
    }

    //per eliminare l'account dell'utente stesso
    this.userService.eliminaAccount().subscribe({
      next: (res: MessageResponse) => {
        this.toastr.success(res.message);
        this.router.navigateByUrl('login');
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error("Errore durante l'eliminazione dell'account");
      }
    })
  }

  //per far ricevere ad un admin i dati di un altro utente
  getAdminUserData(username: string): void {
    this.userService.getAdminUserData(username).subscribe({
      next: (res: GetUserDataResponse) => {
        this.compilaCampi(res);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error("Utente non trovato");
        this.router.navigateByUrl("homepage/admin");
      }
    })
  }

  //assegna a delle variabili i dati della response, così da vedere il form compilato
  compilaCampi(res: GetUserDataResponse): void {
    this.userData.nome = res.nome;
    this.userData.cognome = res.cognome;
    this.userData.email = res.email;
    this.userData.username = res.username;
    this.userData.iscrittoNewsletter = res.iscrittoNewsletter;
  }

  //per far eliminare all'admin un altro account dato un username
  adminEliminaUser(username: string): void {
    this.userService.adminEliminaUser(username).subscribe({
      next: (res: MessageResponse) => {
        this.toastr.success(res.message);
        this.router.navigateByUrl('homepage/admin');
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error("Errore nell'eliminazione dell'account");
        this.router.navigateByUrl('homepage/admin');
      }
    })
  }
}
