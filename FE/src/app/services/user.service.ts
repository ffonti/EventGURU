import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDataResponse } from '../dtos/response/GetUserDataResponse';
import { UpdateUserDataRequest } from '../dtos/request/UpdateUserDataRequest';
import { DeleteUserResponse } from '../dtos/response/DeleteUserResponse';
import { OrganizzatoreResponse } from '../dtos/response/OrganizzatoreResponse';
import { GetOrganizzatoriSeguiti } from '../dtos/response/GetOrganizzatoriSeguiti';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  //costruttore dove istanzio le classi con cui interagire
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

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'updateUserData/' + localStorage.getItem('id')?.toString(), request, { headers: header });
  }

  //chiamo il backend passandogli i nuovi dati per aggiornare l'utente scelto dall'admin, mi ritornerà l'oggetto utente aggiornato
  adminUpdateUserData(nome: string, cognome: string, email: string, username: string, vecchiaPassword: string, nuovaPassword: string, iscrittoNewsletter: boolean, oldUsername: string): Observable<GetUserDataResponse> {
    const header = this.getHeader();
    const request: UpdateUserDataRequest = { nome, cognome, email, username, vecchiaPassword, nuovaPassword, iscrittoNewsletter };

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'adminUpdateUserData/' + oldUsername, request, { headers: header });
  }

  //chiamo il backend per eliminare l'account
  eliminaAccount(): Observable<DeleteUserResponse> {
    const header = this.getHeader();

    return this.http.delete<DeleteUserResponse>(this.backendUrl + 'delete/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  //chiamo il backend per prendere i dati di un utente dato un username
  getAdminUserData(username: string): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getAdminUserData/' + username, { headers: header });
  }

  //chiamo il backend per eliminare i dati di un altro utente (solo se si è admin)
  adminEliminaUser(username: string): Observable<DeleteUserResponse> {
    const header = this.getHeader();

    return this.http.delete<DeleteUserResponse>(this.backendUrl + 'adminDelete/' + username, { headers: header });
  }

  getAllOrganizzatori(): Observable<OrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<OrganizzatoreResponse[]>(this.backendUrl + 'getAllOrganizzatori', { headers: header });
  }

  seguiOrganizzatore(organizzatoreId: string): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'seguiOrganizzatore/' + organizzatoreId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  smettiSeguireOrganizzatore(organizzatoreId: string): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'smettiSeguireOrganizzatore/' + organizzatoreId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  getAllOrganizzatoriSeguiti(): Observable<GetOrganizzatoriSeguiti[]> {
    const header = this.getHeader();

    return this.http.get<GetOrganizzatoriSeguiti[]>(this.backendUrl + 'getOrganizzatoriSeguiti/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
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
