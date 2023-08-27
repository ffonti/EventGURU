import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreaEventoRequest } from '../dtos/request/CreaEventoRequest';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/api/v1/evento';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  creaEvento(titolo: string, descrizione: string, dataInizio: Date, dataFine: Date): Observable<void> {
    const header = this.getHeader();
    const request: CreaEventoRequest = { titolo, descrizione, dataInizio, dataFine };
    console.log(request);

    return this.http.post<void>(this.backendUrl + 'crea', request, { headers: header });
  }

  //chiamo il backend per prendere tutti gli eventi
  getAllEvents(): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + '/getAllEvents', { headers: header });
  }

  //ritorno l'oggetto header con il jwt
  getHeader(): HttpHeaders {
    return new HttpHeaders({
      'x-access-token': localStorage.getItem('token')
        ? `${localStorage.getItem('token')}`
        : '',
    });
  }
}
