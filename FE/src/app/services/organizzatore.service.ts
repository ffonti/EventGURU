import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GetAllResponse } from '../dtos/response/GetAllResponse';
import { Observable } from 'rxjs';
import { globalBackendUrl } from 'environment';

/**
 * service per gestire le interazioni col backend riguardo l'organizzatore
 */
@Injectable({
  providedIn: 'root'
})
export class OrganizzatoreService {

  private backendUrl: string = globalBackendUrl + 'user/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  //chiamo il backend che mi ritornerà il dto con tutti i dati degli utenti di ruolo organizzatore
  getAllOrganizzatore(): Observable<GetAllResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllResponse[]>(this.backendUrl + 'getAll/ORGANIZZATORE', { headers: header });
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
