import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetAllResponse } from '../dtos/response/GetAllResponse';

/**
 * service per gestire le interazioni col backend riguardo il turista
 */
@Injectable({
  providedIn: 'root'
})
export class TuristaService {

  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private http: HttpClient) { }

  //chiamo il backend che mi ritornerà un dto con tutti gli utenti di ruolo turista
  getAllTurista(): Observable<GetAllResponse[]> {
    const header = this.getHeader();

    return this.http.get<GetAllResponse[]>(this.backendUrl + 'getAll/TURISTA', { headers: header });
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
