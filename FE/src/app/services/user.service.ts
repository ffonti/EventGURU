import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDataResponse } from '../dtos/response/GetUserDataResponse';
import { UpdateUserDataRequest } from '../dtos/request/UpdateUserDataRequest';
import { DeleteUserResponse } from '../dtos/response/DeleteUserResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  constructor(private http: HttpClient) { }

  //chiamo il backend che mi ritornerà un dto con i dati dell'utente dato un id
  getUserData(): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getUserData/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  //chiamo il backend passandogli i nuovi dati per aggiornare l'utente, mi ritornerà l'oggetto utente aggiornato
  updateUserData(nome: string, cognome: string, email: string, username: string, vecchiaPassword: string, nuovaPassword: string, iscrittoNewsletter: boolean): Observable<GetUserDataResponse> {
    const header = this.getHeader();
    const request: UpdateUserDataRequest = { nome, cognome, email, username, vecchiaPassword, nuovaPassword, iscrittoNewsletter };
    console.log(request);

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'updateUserData/' + localStorage.getItem('id')?.toString(), request, { headers: header });
  }

  eliminaAccount(): Observable<DeleteUserResponse> {
    const header = this.getHeader();

    return this.http.delete<DeleteUserResponse>(this.backendUrl + 'delete/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  getAdminUserData(username: string): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getAdminUserData/' + username, { headers: header });
  }

  adminEliminaUser(username: string): Observable<DeleteUserResponse> {
    const header = this.getHeader();

    return this.http.delete<DeleteUserResponse>(this.backendUrl + 'adminDelete/' + username, { headers: header });
  }

  //creo l'header con il token da mandare al backend
  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
