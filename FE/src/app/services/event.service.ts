import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  //chiamo il backend per prendere tutti gli eventi
  getAllEvents(): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'api/v1/event/getAllEvents', { headers: header });
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
