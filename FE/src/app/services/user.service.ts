import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDataResponse } from '../dtos/response/GetUserDataResponse';
import { UpdateUserDataRequest } from '../dtos/request/UpdateUserDataRequest';
import { OrganizzatoreResponse } from '../dtos/response/OrganizzatoreResponse';
import { GetOrganizzatoriSeguiti } from '../dtos/response/GetOrganizzatoriSeguiti';
import { MessageResponse } from '../dtos/response/MessageResponse';

/**
 * service per gestire le interazioni col backend riguardo l'utente.
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  /**
   * metodo per prendere i dati di un utente
   * @returns DTO con i dati dell'utente richiesto
   */
  getUserData(): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getUserData/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  /**
   * metodo per aggiornare i propri dati
   * @param nome nome dell'utente
   * @param cognome cognome dell'utente
   * @param email email dell'utente
   * @param username username dell'utente
   * @param vecchiaPassword vecchia password dell'utente
   * @param nuovaPassword nuova password dell'utente
   * @param iscrittoNewsletter configurazione della newsletter dell'utente
   * @param oldUsername vecchio username dell'utente
   * @returns DTO con i dati aggiornati dell'utente
   */
  updateUserData(nome: string, cognome: string, email: string, username: string, vecchiaPassword: string, nuovaPassword: string, iscrittoNewsletter: boolean): Observable<GetUserDataResponse> {
    const header = this.getHeader();
    const request: UpdateUserDataRequest = { nome, cognome, email, username, vecchiaPassword, nuovaPassword, iscrittoNewsletter };

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'updateUserData/' + localStorage.getItem('id')?.toString(), request, { headers: header });
  }

  /**
   * metodo per l'admin per aggiornare i dati di un utente
   * @param nome nome dell'utente
   * @param cognome cognome dell'utente
   * @param email email dell'utente
   * @param username username dell'utente
   * @param vecchiaPassword vecchia password dell'utente
   * @param nuovaPassword nuova password dell'utente
   * @param iscrittoNewsletter configurazione della newsletter dell'utente
   * @param oldUsername vecchio username dell'utente
   * @returns DTO con i dati aggiornati dell'utente
   */
  adminUpdateUserData(nome: string, cognome: string, email: string, username: string, vecchiaPassword: string, nuovaPassword: string, iscrittoNewsletter: boolean, oldUsername: string): Observable<GetUserDataResponse> {
    const header = this.getHeader();
    //creo il DTO con i dati da aggiorare
    const request: UpdateUserDataRequest = { nome, cognome, email, username, vecchiaPassword, nuovaPassword, iscrittoNewsletter };

    return this.http.put<GetUserDataResponse>(this.backendUrl + 'adminUpdateUserData/' + oldUsername, request, { headers: header });
  }

  /**
   * metodo per eliminare l'account di un utente
   * @returns messaggio di risposta
   */
  eliminaAccount(): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.delete<MessageResponse>(this.backendUrl + 'delete/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  /**
   * chiamo il backend per prendere i dati di un utente dato un username
   * @param username username dell'utente
   * @returns DTO con i dati dell'utente
   */
  getAdminUserData(username: string): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getAdminUserData/' + username, { headers: header });
  }

  /**
   * chiamo il backend per eliminare i dati di un altro utente (solo se si è admin)
   * @param username username dell'utente in questione
   * @returns messaggio di risposta
   */
  adminEliminaUser(username: string): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.delete<MessageResponse>(this.backendUrl + 'adminDelete/' + username, { headers: header });
  }

  /**
   * metodo per prendere tutti gli organizzatori salvati sul database
   * @returns lista di DTO con i dati degli organizzatori
   */
  getAllOrganizzatori(): Observable<OrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<OrganizzatoreResponse[]>(this.backendUrl + 'getAllOrganizzatori', { headers: header });
  }

  /**
   * metodo per far seguire ad un turista un organizzatore
   * @param organizzatoreId id dell'organizzatore da seguire
   * @returns messaggio di risposta
   */
  seguiOrganizzatore(organizzatoreId: string): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.get<MessageResponse>(this.backendUrl + 'seguiOrganizzatore/' + organizzatoreId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  /**
   * metodo per far smettere di seguire ad un turista un organizzatore
   * @param organizzatoreId id dell'organizzatore da unfolloware
   * @returns messaggio di risposta
   */
  smettiSeguireOrganizzatore(organizzatoreId: string): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.get<MessageResponse>(this.backendUrl + 'smettiSeguireOrganizzatore/' + organizzatoreId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  /**
   * metodo per prendere tutti gli organizzatori seguiti da un dato turista
   * @returns array di DTO con gli organizzatori seguiti
   */
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
