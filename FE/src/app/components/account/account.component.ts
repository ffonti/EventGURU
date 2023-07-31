import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UpdateUserDataRequest } from 'src/app/dtos/request/UpdateUserDataRequest';
import { GetUserDataResponse } from 'src/app/dtos/response/GetUserDataResponse';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  //creo un nuovo tipo con i dati dell'utente
  userData: any = {
    username: '',
    nome: '',
    cognome: '',
    nuovaPassword: '',
    email: '',
    vecchiaPassword: '',
    iscrittoNewsletter: false
  };
  ripeti_password: string = '';

  //per gestire le icone
  protected showPassword: boolean = false;
  protected showVecchiaPassword: boolean = false;
  protected showRepeatPassword: boolean = false;

  constructor(private userService: UserService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    //chiamo il server per chiedere i dati dell'utente
    this.userService.getUserData().subscribe({
      next: (res: GetUserDataResponse) => {
        //in caso di successo assegno i dati al tipo
        this.userData.nome = res.nome;
        this.userData.cognome = res.cognome;
        this.userData.email = res.email;
        this.userData.username = res.username;
        this.userData.iscrittoNewsletter = res.iscrittoNewsletter;
      },
      error: (err: any) => {
        //in caso di errore mando l'utente al login
        console.log(err);
        this.toastr.error('Eseguire nuovamente il login');
        this.router.navigateByUrl('login');
      }
    })
  }

  //per gestire l'icona
  toggleShowPassword(type: string): void {
    if (type === 'repeat') {
      this.showRepeatPassword = !this.showRepeatPassword;
      return;
    } else if (type === 'no-repeat') {
      this.showPassword = !this.showPassword;
      return;
    } else if (type === 'vecchia') {
      this.showVecchiaPassword = !this.showVecchiaPassword;
    }
  }

  ripetiPasswordNotOk(): boolean {
    return (this.userData.nuovaPassword !== this.ripeti_password);
  }

  updateUserData(): void {
    //chiamo il server per aggiornare i dati dell'utente
    this.userService.updateUserData(this.userData.nome, this.userData.cognome, this.userData.email, this.userData.username, this.userData.vecchiaPassword, this.userData.nuovaPassword, this.userData.iscrittoNewsletter).subscribe({
      next: (res: GetUserDataResponse) => {
        this.toastr.success("Dati modificati con successo");
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

  vecchiaENuovaPasswordUguali(): boolean {
    return (this.userData.vecchiaPassword === this.userData.nuovaPassword && this.userData.vecchiaPassword !== '');
  }
}
