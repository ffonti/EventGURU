import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetAllEventiByOrganizzatoreResponse } from '../dtos/response/GetAllEventiByOrganizzatoreResponse';
import { GetEventoByIdResponse } from '../dtos/response/GetEventoByIdResponse';
import { CreaModificaEventoRequest } from '../dtos/request/CreaModificaEventoRequest';
import { GetAllEventiResponse } from '../dtos/response/GetAllEventiResponse';
import { AdminCreaEventoRequest } from '../dtos/request/AdminCreaEventoRequest';
import { MessageResponse } from '../dtos/response/MessageResponse';
import { IscrizioneEventoRequest } from '../dtos/request/IscrizioneEventoRequest';
import { SpinnerService } from './spinner.service';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/api/v1/evento/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient, private spinnerService: SpinnerService) { }

  creaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };
    this.spinnerService.requestStarted();
    return this.http.post<MessageResponse>(this.backendUrl + 'crea/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  adminCreaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, usernameOrganizzatore: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.post<MessageResponse>(this.backendUrl + 'adminCrea', request, { headers: header });
  }

  modificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, eventoId: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };

    return this.http.put<MessageResponse>(this.backendUrl + 'modifica/' + eventoId, request, { headers: header });
  }

  adminModificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string, eventoId: string, usernameOrganizzatore: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.put<MessageResponse>(this.backendUrl + 'adminModifica/' + eventoId, request, { headers: header });
  }

  getEventiByOrganizzatore(): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  getEventiByOrganizzatoreId(organizzatoreId: string): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + organizzatoreId, { headers: header });
  }

  getAllEventi(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAll', { headers: header });
  }

  eliminaEvento(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.delete<MessageResponse>(this.backendUrl + 'eliminaEvento/' + eventoId.toString().trim(), { headers: header });
  }

  getEventoById(eventoId: string): Observable<GetEventoByIdResponse> {
    const header = this.getHeader();

    return this.http.get<GetEventoByIdResponse>(this.backendUrl + 'getEventoById/' + eventoId.toString().trim(), { headers: header });
  }

  iscrizioneEvento(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();
    let turistaId = +(localStorage.getItem('id') || -1);
    const request: IscrizioneEventoRequest = { eventoId, turistaId };

    return this.http.post<MessageResponse>(this.backendUrl + 'iscrizione', request, { headers: header });
  }

  annullaIscrizione(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();
    let turistaId = +(localStorage.getItem('id') || -1);
    const request: IscrizioneEventoRequest = { eventoId, turistaId };

    return this.http.post<MessageResponse>(this.backendUrl + 'annullaIscrizione', request, { headers: header });
  }

  getEventiByTurista(usernameTurista: string): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByTurista/' + usernameTurista, { headers: header });
  }

  rimuoviTuristaDaEvento(usernameTurista: string, eventoId: string): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.delete<MessageResponse>(this.backendUrl + 'rimuoviTuristaDaEvento/' + usernameTurista + '/' + eventoId, { headers: header });
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
