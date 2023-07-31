import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/dtos/request/LoginRequest';
import { LoginResponse } from 'src/app/dtos/response/LoginResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  protected loginForm!: FormGroup;

  protected password: string = '';
  protected showPassword: boolean = false;

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    //pulisco il localstorage da precedenti dati
    localStorage.clear();

    //inizializzo il form, segnando con required i campi
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  login(): void {
    //Se il form non è valido lancio un messaggio di errore
    if (!this.checkForm(this.loginForm)) {
      this.toastr.error("Compilare tutti i campi");
      return;
    }

    //Assegno i valori del form a delle variabili
    const username: string = this.loginForm.controls['username'].value;
    this.password = this.loginForm.controls['password'].value;
    const password: string = this.password;

    //Assegno i valori del form a delle variabili
    const request: LoginRequest = { username, password };

    //Chiamo il service che chiamerà il server per il login
    this.authService.login(request).subscribe({
      next: (res: LoginResponse) => {
        //In caso di successo, salvo i dati nel localstorage
        this.toastr.success(res.message);
        localStorage.setItem('token', "Bearer " + res.jwt);
        localStorage.setItem('id', res.user.userId.toString());
        localStorage.setItem('nome', res.user.nome);
        localStorage.setItem('cognome', res.user.cognome);
        localStorage.setItem('username', res.user.username);
        localStorage.setItem('ruolo', res.user.ruolo);
        localStorage.setItem('email', res.user.email);
        localStorage.setItem('iscrittoNewsletter', res.user.iscrittoNewsletter.toString());
        this.router.navigateByUrl('homepage');
      },
      error: (err: HttpErrorResponse) => {
        //In caso di errore stampo un messaggio
        console.log(err);
        this.toastr.error("Errore nel login");
      },
    });
  }

  //controlla se c'è almeno un campo vuoto, null o undefined e in quel caso torna false
  checkForm(form: FormGroup): boolean {
    let isOk: boolean = true;

    Object.keys(form.controls).forEach(key => {
      if (form.controls[key].value === '' || form.controls[key].value === null || form.controls[key].value === undefined) {
        isOk = false;
      }
    });

    return isOk;
  }

  //Sceglie quale icona visualizzare nel form
  toggleShowPassword(): void {
    this.showPassword = !this.showPassword;
  }

  //true se il campo è vuoto ed è stato toccato (si circonda di rosso per far capire che bisogna compilarlo)
  emptyAndTouched(campo: string): boolean {
    return (this.loginForm.controls[campo].value === '' && this.loginForm.controls[campo].touched);
  }
}
