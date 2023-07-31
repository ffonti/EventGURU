import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../dtos/request/LoginRequest';
import { RegisterRequest } from '../dtos/request/RegisterRequest';
import { LoginResponse } from '../dtos/response/LoginResponse';
import { RegisterResponse } from '../dtos/response/RegisterResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = 'http://localhost:8080/api/v1/auth/';

  constructor(private http: HttpClient) { }

  //chiamo il backend per il login, gli mando un dto con i dati per la request e riceverò un dto con l'utente e il token
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.backendUrl + 'login', request);
  }

  //chiamo il backend per la registraione, mando un dto con i dati per la registrazione e riceverò un messaggio di risposta
  register(request: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(this.backendUrl + 'register', request);
  }

  //per il logout pulisco il localstorage
  logout(): void {
    localStorage.clear();
  }
}
