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

/**
 * service per chiamare il backend riguardo gli eventi
 */
@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/api/v1/evento/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient, private spinnerService: SpinnerService) { }

  /**
   * metodo per creare un evento
   * @param titolo titolo dell'evento
   * @param descrizione descrizione dell'evento
   * @param dataInizio data di inziio dell'evento
   * @param dataFine data di fine dell'evento
   * @param lat latitudine dell'evento
   * @param lng longitudine dell'evento
   * @param nomeLuogo nome del luogo dell'evento
   * @returns messaggio di avvenuta registrazione
   */
  creaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: number, lng: number, nomeLuogo: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };
    this.spinnerService.requestStarted();
    return this.http.post<MessageResponse>(this.backendUrl + 'crea/' + localStorage.getItem('id')?.toString().trim(), request, { headers: header });
  }

  /**
   * metodo per gli admin. permette di creare un evento
   * @param titolo titolo dell'evento
   * @param descrizione descrizione dell'evento
   * @param dataInizio data di inziio dell'evento
   * @param dataFine data di fine dell'evento
   * @param lat latitudine dell'evento
   * @param lng longitudine dell'evento
   * @param nomeLuogo nome del luogo dell'evento
   * @param usernameOrganizzatore username dell'organizzatore dell'evento
   * @returns messaggio di avvenuta registrazione
   */
  adminCreaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: number, lng: number, nomeLuogo: string, usernameOrganizzatore: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.post<MessageResponse>(this.backendUrl + 'adminCrea', request, { headers: header });
  }

  /**
   * metodo per modificare i dati di un evento
   * @param titolo titolo dell'evento
   * @param descrizione descrizione dell'evento
   * @param dataInizio data di inziio dell'evento
   * @param dataFine data di fine dell'evento
   * @param lat latitudine dell'evento
   * @param lng longitudine dell'evento
   * @param nomeLuogo nome del luogo dell'evento
   * @param eventoId id dell'evento da modificare
   * @returns messaggio di avvenuta modifica
   */
  modificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: number, lng: number, nomeLuogo: string, eventoId: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: CreaModificaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };

    return this.http.put<MessageResponse>(this.backendUrl + 'modifica/' + eventoId, request, { headers: header });
  }

  /**
   * metodo per gli admin. permette di modificare i dati di un evento
   * @param titolo titolo dell'evento
   * @param descrizione descrizione dell'evento
   * @param dataInizio data di inziio dell'evento
   * @param dataFine data di fine dell'evento
   * @param lat latitudine dell'evento
   * @param lng longitudine dell'evento
   * @param nomeLuogo nome del luogo dell'evento
   * @param eventoId id dell'evento da modificare
   * @param usernameOrganizzatore useranme dell'organizzatore
   * @returns messaggio di avvenuta modifica
   */
  adminModificaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: number, lng: number, nomeLuogo: string, eventoId: string, usernameOrganizzatore: string): Observable<MessageResponse> {
    const header = this.getHeader();
    const request: AdminCreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo, usernameOrganizzatore };

    return this.http.put<MessageResponse>(this.backendUrl + 'adminModifica/' + eventoId, request, { headers: header });
  }

  /**
   * metodo per prendere dal db tutti gli eventi di un dato organizzatore
   * @returns array di DTO con i dati degli eventi
   */
  getEventiByOrganizzatore(): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  /**
   * metodo per prendere dal db tutti gli eventi di un dato organizzatore
   * @param organizzatoreId id dell'organizzatore
   * @returns array di DTO con i dati degli eventi
   */
  getEventiByOrganizzatoreId(organizzatoreId: string): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + organizzatoreId, { headers: header });
  }

  /**
   * metodo per prendere tutti gli eventi presenti sul database
   * @returns array di DTO con i dati degli eventi
   */
  getAllEventi(): Observable<GetAllEventiResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiResponse[]>(this.backendUrl + 'getAll', { headers: header });
  }

  /**
   * metodo per eliminare un evento sul database
   * @param eventoId id dell'evento da eliminare
   * @returns messaggio di avvenuta eliminazione
   */
  eliminaEvento(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();

    return this.http.delete<MessageResponse>(this.backendUrl + 'eliminaEvento/' + eventoId.toString().trim(), { headers: header });
  }

  /**
   * metodo per prendere un singolo evento dal database dato un id
   * @param eventoId id dell'evento
   * @returns dati del singolo evento
   */
  getEventoById(eventoId: string): Observable<GetEventoByIdResponse> {
    const header = this.getHeader();

    return this.http.get<GetEventoByIdResponse>(this.backendUrl + 'getEventoById/' + eventoId.toString().trim(), { headers: header });
  }

  /**
   * metodo per iscriversi ad un evento
   * @param eventoId id dell'evento
   * @returns messaggio di avvenuta iscrizione
   */
  iscrizioneEvento(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();
    let turistaId = +(localStorage.getItem('id') || -1);
    const request: IscrizioneEventoRequest = { eventoId, turistaId };

    return this.http.post<MessageResponse>(this.backendUrl + 'iscrizione', request, { headers: header });
  }

  /**
   * metodo per disiscriversi da un evento
   * @param eventoId id dell'evento
   * @returns messaggio di avvenuta disiscrizione
   */
  annullaIscrizione(eventoId: number): Observable<MessageResponse> {
    const header = this.getHeader();
    //se non è presente l'id assegno -1 che lancerà un'exception
    let turistaId = +(localStorage.getItem('id') || -1);
    const request: IscrizioneEventoRequest = { eventoId, turistaId };

    return this.http.post<MessageResponse>(this.backendUrl + 'annullaIscrizione', request, { headers: header });
  }

  /**
   * metodo per prendere gli eventi a cui è iscritto un dato turista
   * @param usernameTurista username del turista
   * @returns array di DTO con i dati degli eventi
   */
  getEventiByTurista(usernameTurista: string): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByTurista/' + usernameTurista, { headers: header });
  }

  /**
   * metodo per rimuovere un turista da un dato evento
   * @param usernameTurista username del turista
   * @param eventoId id dell'evento
   * @returns messaggio di avvenuta rimozione
   */
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
