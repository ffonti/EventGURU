import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  constructor(private http: HttpClient) { }

  getAllEvents(): Observable<any> {
    const header = this.getHeader();

    return this.http.get('http://localhost:3000/api/v1/event/getAllEvents', { headers: header });
  }

  getHeader(): HttpHeaders {
    return new HttpHeaders({
      'x-access-token': localStorage.getItem('token')
        ? `${localStorage.getItem('token')}`
        : '',
    });
  }
}
