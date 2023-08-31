import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreaEventoRequest } from '../dtos/request/CreaEventoRequest';
import { CreaEventoResponse } from '../dtos/response/CreaEventoResponse';
import { GetAllEventiByOrganizzatoreResponse } from '../dtos/response/GetAllEventiByOrganizzatoreResponse';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/api/v1/evento/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  creaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date, lat: string, lng: string, nomeLuogo: string): Observable<CreaEventoResponse> {
    const header = this.getHeader();
    const request: CreaEventoRequest = { titolo, descrizione, dataInizio, dataFine, lat, lng, nomeLuogo };
    console.log(request);

    return this.http.post<CreaEventoResponse>(this.backendUrl + 'crea', request, { headers: header });
  }

  getEventiByOrganizzatore(): Observable<GetAllEventiByOrganizzatoreResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllEventiByOrganizzatoreResponse[]>(this.backendUrl + 'getByOrganizzatore/' + localStorage.getItem('id')?.toString().trim(), { headers: header });
  }

  //chiamo il backend per prendere tutti gli eventi
  // getAllEvents(): Observable<any> {
  //   const header = this.getHeader();

  //   return this.http.get(this.backendUrl + '/getAllEvents', { headers: header });
  // }

  //creo l'header con il token da mandare al backend
  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
