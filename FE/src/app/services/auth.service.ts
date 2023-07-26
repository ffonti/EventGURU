import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Ruolo } from '../types/Ruolo';
import { LoginRequest } from '../dto/request/LoginRequest';
import { RegisterRequest } from '../dto/request/RegisterRequest';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  login(request: LoginRequest): Observable<any> {
    return this.http.post(
      this.backendUrl + 'api/v1/auth/login',
      request,
      { observe: 'response' });
  }

  register(request: RegisterRequest): Observable<any> {
    // const data = { nome, cognome, email, username, password, ruolo };
    return this.http.post(
      this.backendUrl + 'api/v1/auth/register',
      request,
      { observe: 'response' });
  }

  logout(): void {
    localStorage.clear();
  }
}
