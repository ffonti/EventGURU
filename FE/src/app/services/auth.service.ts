import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserLoginDTO } from '../dto/UserDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl: string = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(this.backendUrl + 'api/v1/auth/login', { username, password }, { observe: 'response' });
  }

  register(nome: string, cognome: string, email: string, username: string, password: any): Observable<any> {
    const data = { nome, cognome, email, username, password };
    return this.http.post(this.backendUrl + 'api/v1/auth/register', { data }, { observe: 'response' });
  }

  logout(): void {
    localStorage.clear();
  }
}
