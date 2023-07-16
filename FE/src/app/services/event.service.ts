import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private backendUrl: string = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  getAllEvents(): Observable<any> {
    const header = this.getHeader();

    return this.http.get(this.backendUrl + 'api/v1/event/getAllEvents', { headers: header });
  }

  getHeader(): HttpHeaders {
    return new HttpHeaders({
      'x-access-token': localStorage.getItem('token')
        ? `${localStorage.getItem('token')}`
        : '',
    });
  }
}
