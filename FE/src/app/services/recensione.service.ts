import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { inviaRecensioneRequest } from '../dtos/request/InviaRecensioneRequest';
import { RecensioneResponse } from '../dtos/response/RecensioneResponse';
import { RecensioneDettagliataResponse } from '../dtos/response/RecensioneDettagliataResponse';

@Injectable({
  providedIn: 'root'
})
export class RecensioneService {
  private backendUrl: string = 'http://localhost:8080/api/v1/recensione/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  inviaRecensione(eventoId: string, voto: number, testo: string): Observable<any> {
    const header = this.getHeader();
    const request: inviaRecensioneRequest = { voto, testo };

    return this.http.post(this.backendUrl + 'inviaRecensione/' + eventoId + '/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  getRecensioniByEvento(eventoId: string): Observable<RecensioneResponse[]> {
    const header = this.getHeader();

    return this.http.get<RecensioneResponse[]>(this.backendUrl + 'getByEvento/' + eventoId, { headers: header });
  }

  getRecensione(eventoId: string, usernameTurista: string): Observable<RecensioneDettagliataResponse> {
    const header = this.getHeader();

    return this.http.get<RecensioneDettagliataResponse>(this.backendUrl + 'get/' + eventoId + '/' + usernameTurista, { headers: header });
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
