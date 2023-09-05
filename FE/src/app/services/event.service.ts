import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreaModificaEventoResponse } from '../dtos/response/CreaModificaEventoResponse';
import { GetAllEventiByOrganizzatoreResponse } from '../dtos/response/GetAllEventiByOrganizzatoreResponse';
import { GetEventoByIdResponse } from '../dtos/response/GetEventoByIdResponse';
import { CreaModificaEventoRequest } from '../dtos/request/CreaModificaEventoRequest';
import { GetAllEventiResponse } from '../dtos/response/GetAllEventiResponse';
import { AdminCreaEventoRequest } from '../dtos/request/AdminCreaEventoRequest';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/api/v1/evento/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  creaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string): Observable<CreaModificaEventoResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };

    return this.http.post<CreaModificaEventoResponse>(this.backendUrl + 'crea', request, { headers: header });
  }

  adminCreaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, usernameOrganizzatore: string): Observable<CreaModificaEventoResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.post<CreaModificaEventoResponse>(this.backendUrl + 'adminCrea', request, { headers: header });
  }

  modificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, eventoId: string): Observable<CreaModificaEventoResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };

    return this.http.put<CreaModificaEventoResponse>(this.backendUrl + 'modifica/' + eventoId, request, { headers: header });
  }

  adminModificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, eventoId: string, usernameOrganizzatore: string): Observable<CreaModificaEventoResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.put<CreaModificaEventoResponse>(this.backendUrl + 'adminModifica/' + eventoId, request, { headers: header });
  }

  getEventiByOrganizzatore(): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  getAllEventi(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAll', { headers: header });
  }

  eliminaEvento(eventoId: number): Observable<string> {
    const header = this.getHeader();

    return this.http.delete<string>(this.backendUrl + 'eliminaEvento/' + eventoId.toString().trim(), { headers: header });
  }

  getEventoById(eventoId: string): Observable<GetEventoByIdResponse> {
    const header = this.getHeader();

    return this.http.get<GetEventoByIdResponse>(this.backendUrl + 'getEventoById/' + eventoId.toString().trim(), { headers: header });
  }

  iscrizioneEvento(eventoId: string): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'iscrizione/' + eventoId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  annullaIscrizione(eventoId: string): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'annullaIscrizione/' + eventoId + '/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
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
