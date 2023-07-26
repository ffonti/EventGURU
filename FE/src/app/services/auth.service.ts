import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Ruolo } from '../types/Ruolo';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(
      this.backendUrl + 'api/v1/auth/login',
      { username, password },
      { observe: 'response' });
  }

  register(nome: string, cognome: string, email: string, username: string, password: string, ruolo: Ruolo): Observable<any> {
    // const data = { nome, cognome, email, username, password, ruolo };
    return this.http.post(
      this.backendUrl + 'api/v1/auth/register',
      { nome, cognome, email, username, password, ruolo },
      { observe: 'response' });
  }

  logout(): void {
    localStorage.clear();
  }
}
