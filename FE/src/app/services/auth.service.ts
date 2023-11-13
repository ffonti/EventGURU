import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../dtos/request/LoginRequest';
import { RegisterRequest } from '../dtos/request/RegisterRequest';
import { LoginResponse } from '../dtos/response/LoginResponse';
import { Router } from '@angular/router';
import { SpinnerService } from './spinner.service';
import { ToastrService } from 'ngx-toastr';
import { MessageResponse } from '../dtos/response/MessageResponse';
import { globalBackendUrl } from 'environment';

/**
 * service che chiama il backend per l'autenticazione.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = globalBackendUrl + 'auth/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient, private router: Router, private spinnerService: SpinnerService, private toastr: ToastrService) { }

  /**
   * chiamo il backend per il login, gli mando un dto con i dati per la request e riceverò un dto con l'utente e il token
   * @param request DTO con i dati del login
   * @returns dati dell'utente registrato
   */
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.backendUrl + 'login', request);
  }

  /**
   * chiamo il backend per la registraione, mando un dto con i dati per la registrazione e riceverò un messaggio di risposta
   * @param request DTO con i dati per la registrazione
   * @returns Messaggio di avvenuta registrazione
   */
  register(request: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.backendUrl + 'register', request);
  }

  /**
   * chiamo il backend per recuperare la password, specifico la mail tramite il path
   * @param email email dell'utente che vuole recuperare la password
   * @returns messaggio di avvenuto invio della mail
   */
  recuperaPassword(email: string): Observable<MessageResponse> {
    this.spinnerService.requestStarted();
    return this.http.get<MessageResponse>(this.backendUrl + 'recuperaPassword/' + email);
  }

  //per il logout pulisco il localstorage
  logout(): void {
    this.router.navigateByUrl('login');
    localStorage.clear();
    this.toastr.success('Logout effettuato con successo');
  }
}
