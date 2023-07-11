import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  getUserData(): Observable<any> {
    const header = this.getHeader();

    return this.http.get('http://localhost:3000/api/v1/user/getUserData', { headers: header, observe: 'response' });
  }

  updateUserData(userData: any): Observable<any> {
    const header = this.getHeader();

    return this.http.put('http://localhost:3000/api/v1/user/updateUserData', { userData }, { headers: header, observe: 'response' });
  }

  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'x-access-token': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      username: localStorage.getItem('username') ? `${localStorage.getItem('username')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : '',
      nome: localStorage.getItem('nome') ? `${localStorage.getItem('nome')}` : '',
    });
  }
}
