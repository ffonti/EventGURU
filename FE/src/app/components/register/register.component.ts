import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Ruolo } from 'src/app/types/Ruolo';
import { RegisterRequest } from 'src/app/dtos/request/RegisterRequest';
import { MessageResponse } from 'src/app/dtos/response/MessageResponse';

/**
 * componente per registrare un utente. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  protected registerForm!: FormGroup;

  protected password: string = '';
  protected ripeti_password: string = '';

  protected showPassword: boolean = false;
  protected showRepeatPassword: boolean = false;

  //costruttore dove istanzio le classi con cui interagire
  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) { }

  //Metodo eseguito all'avvio del componente
  ngOnInit(): void {
    //Inizializzo il form, nello specifico tutti i campi sono vincolati dal required
    this.registerForm = new FormGroup({
      nome: new FormControl('', [Validators.required]),
      cognome: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      ripeti_password: new FormControl('', [Validators.required]),
      ruolo: new FormControl('', [Validators.required])
    });
  }

  //per la registrazione
  register(): void {
    //Se il form non è valido lancio un messaggio di errore
    if (!this.checkForm(this.registerForm)) {
      this.toastr.error("Compilare tutti i campi");
      return;
    }

    //Assegno i valori del form a delle variabili
    const nome: string = this.registerForm.controls['nome'].value;
    const cognome: string = this.registerForm.controls['cognome'].value;
    const email: string = this.registerForm.controls['email'].value;
    const username: string = this.registerForm.controls['username'].value;
    const password: string = this.registerForm.controls['password'].value;
    const ruolo: Ruolo = this.registerForm.controls['ruolo'].value == "TURISTA" ? Ruolo.TURISTA : Ruolo.ORGANIZZATORE;

    //Creo il DTO per mandare la richiesta al server
    const request: RegisterRequest = { nome, cognome, email, username, password, ruolo };

    //Tramite il service mando la richiesta al server
    this.authService.register(request).subscribe({
      next: (res: MessageResponse) => {
        //In caso di successo mando l'utente al login
        this.toastr.success(res.message);
        this.router.navigateByUrl('/login');
      },
      error: (err: any) => {
        //In caso di errore visualizzo un messaggio
        console.log(err);
        this.toastr.error(err.error.message);
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
  toggleShowPassword(type: string): void {
    type === 'repeat' ? this.showRepeatPassword = !this.showRepeatPassword : this.showPassword = !this.showPassword;
  }

  //true se il campo è vuoto ed è stato toccato (si circonda di rosso per far capire che bisogna compilarlo)
  emptyAndTouched(campo: string): boolean {
    return (this.registerForm.controls[campo].value === '' && this.registerForm.controls[campo].touched);
  }

  /* true se password e ripeti password sono diversi e il campo è stato toccato 
  (si circonda di rosso per far capire che bisogna compilarlo e dev'essere uguale a password) */
  ripetiPasswordNotOk(): boolean {
    return (this.password !== this.ripeti_password && this.registerForm.controls['ripeti_password'].touched);
  }
}
