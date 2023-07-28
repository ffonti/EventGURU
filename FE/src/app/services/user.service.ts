import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDataResponse } from '../dtos/response/GetUserDataResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private backendUrl: string = 'http://localhost:8080/api/v1/user/';

  constructor(private http: HttpClient) { }

  getUserData(): Observable<GetUserDataResponse> {
    const header = this.getHeader();

    return this.http.get<GetUserDataResponse>(this.backendUrl + 'getUserData/' + localStorage.getItem('id')?.toString(), { headers: header });
  }

  updateUserData(userData: any): Observable<any> {
    const header = this.getHeader();

    return this.http.put(this.backendUrl + 'updateUserData', { userData }, { headers: header, observe: 'response' });
  }

  private getHeader(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': localStorage.getItem('token') ? `${localStorage.getItem('token')}` : '',
      id: localStorage.getItem('id') ? `${localStorage.getItem('id')}` : '',
      ruolo: localStorage.getItem('ruolo') ? `${localStorage.getItem('ruolo')}` : ''
    });
  }
}
