import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../dtos/request/LoginRequest';
import { RegisterRequest } from '../dtos/request/RegisterRequest';
import { LoginResponse } from '../dtos/response/LoginResponse';
import { RegisterResponse } from '../dtos/response/RegisterResponse';
import { Router } from '@angular/router';
import { SpinnerService } from './spinner.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = 'http://localhost:8080/api/v1/auth/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient, private router: Router, private spinnerService: SpinnerService, private toastr: ToastrService) { }

  //chiamo il backend per il login, gli mando un dto con i dati per la request e riceverò un dto con l'utente e il token
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.backendUrl + 'login', request);
  }

  //chiamo il backend per la registraione, mando un dto con i dati per la registrazione e riceverò un messaggio di risposta
  register(request: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(this.backendUrl + 'register', request);
  }

  recuperaPassword(email: string): Observable<any> {
    this.spinnerService.requestStarted();
    return this.http.get(this.backendUrl + 'recuperaPassword/' + email);
  }

  //per il logout pulisco il localstorage
  logout(): void {
    this.router.navigateByUrl('login');
    localStorage.clear();
    this.toastr.success('Logout effettuato con successo');
  }
}
