import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDataResponse } from '../dtos/response/GetUserDataResponse';
import { UpdateUserDataRequest } from '../dtos/request/UpdateUserDataRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  constructor(private http: HttpClient) { }

  getUserData(): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getUserData/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  updateUserData(nome: string, cognome: string, email: string, username: string, vecchiaPassword: string, nuovaPassword: string, iscrittoNewsletter: boolean): Observable<GetUserDataResponse> {
    const header = this.getHeader();
    const request: UpdateUserDataRequest = { nome, cognome, email, username, vecchiaPassword, nuovaPassword, iscrittoNewsletter };
    console.log(request);

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'updateUserData/' + localStorage.getItem('id')?.toString(), request, { headers: header });
  }

  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
