import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { inviaRecensioneRequest } from '../dtos/request/InviaRecensioneRequest';
import { RecensioneResponse } from '../dtos/response/RecensioneResponse';
import { RecensioneDettagliataResponse } from '../dtos/response/RecensioneDettagliataResponse';
import { MessageResponse } from '../dtos/response/MessageResponse';
import { globalBackendUrl } from 'environment';

/**
 * service per gestire le interazioni col backend riguardo le recensioni
 */
@Injectable({
  providedIn: 'root'
})
export class RecensioneService {
  private backendUrl: string = globalBackendUrl + 'recensione/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  /**
   * metodo per salvare una recensione sul database
   * @param eventoId id dell'evento
   * @param voto voto della recensione
   * @param testo testo della recensione
   * @returns messaggio di avvenuto invio della recensione
   */
  inviaRecensione(eventoId: string, voto: number, testo: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: inviaRecensioneRequest = { voto, testo };

    return this.http.post<MessageResponse>(this.backendUrl + 'inviaRecensione/' + eventoId + '/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  /**
   * chiamo il backend per prendere le recensioni di un dato evento
   * @param eventoId id dell'evento
   * @returns array di DTO con le recensioni
   */
  getRecensioniByEvento(eventoId: string): Observable<RecensioneResponse[]> {
    const header = this.getHeader();

    return this.http.get<RecensioneResponse[]>(this.backendUrl + 'getByEvento/' + eventoId, { headers: header });
  }

  /**
   * chiamo il backend per prendere i dati dettagliati di una singola recensione
   * @param eventoId id dell'evento
   * @param usernameTurista useranme del turista
   * @returns DTO con i dati dettagliati della singola recensione
   */
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
