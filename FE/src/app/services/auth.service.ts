import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post('http://localhost:3000/api/v1/auth/login', { username, password });
  }

  register(nome: string, cognome: string, email: string, username: string, password: any): Observable<any> {
    const data = { nome, cognome, email, username, password };
    return this.http.post('http://localhost:3000/api/v1/auth/register', { data }, { observe: 'response' });
  }

  logout(): void {
    localStorage.clear();
  }
}
