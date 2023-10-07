import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * service per gestire il funzionamento dello spinner per il caricamento
 */
@Injectable({
  providedIn: 'root'
})
export class SpinnerService {

  private count: number = 0;
  //creo un subject del pattern observer per lo spinner
  private spinner$: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor() { }

  //ritorno l'observer dello spinner
  getSpinnerObserver(): Observable<string> {
    return this.spinner$.asObservable();
  }

  //cambia lo stato dello spinner
  requestStarted(): void {
    if (++this.count === 1) {
      this.spinner$.next('start');
    }
  }

  //cambia lo stato dello spinner
  requestEnded(): void {
    if (this.count === 0 || --this.count === 0) {
      this.spinner$.next('stop');
    }
  }

  //cambia lo stato dello spinner
  resetSpinner(): void {
    this.count = 0;
    this.spinner$.next('stop');
  }
}
