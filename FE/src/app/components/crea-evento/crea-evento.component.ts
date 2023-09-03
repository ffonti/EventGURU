import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CreaModificaEventoResponse } from 'src/app/dtos/response/CreaModificaEventoResponse';
import { GetEventoByIdResponse } from 'src/app/dtos/response/GetEventoByIdResponse';
import { EventService } from 'src/app/services/event.service';
import { MapService } from 'src/app/services/map.service';

@Component({
  selector: 'app-crea-evento',
  templateUrl: './crea-evento.component.html',
  styleUrls: ['./crea-evento.component.css']
})
export class CreaEventoComponent implements OnInit, AfterViewInit {
  private map: any;
  protected titolo: string = '';
  protected descrizione: string = '';
  protected nomeLuogo: string = '';
  protected dataInizio: Date = new Date();
  protected dataFine: Date = new Date();
  protected eventoId: string = '';
  private campiIniziali!: GetEventoByIdResponse;
  protected ruolo: string | undefined = '';
  protected usernameOrganizzatore: string = '';

  constructor(
    private toastr: ToastrService,
    private eventService: EventService,
    private router: Router,
    private mapService: MapService) { }

  ngOnInit(): void {
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();

    this.eventoId = this.router.url.split('/homepage/creaEvento/')[1];
    if (this.eventoId !== null && this.eventoId !== undefined && this.eventoId !== '') {
      this.eventService.getEventoById(this.eventoId).subscribe({
        next: (res: GetEventoByIdResponse) => {
          this.compilaCampi(res);
          this.campiIniziali = JSON.parse(JSON.stringify(res));
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
          this.toastr.error(err.error.message);
          this.router.navigateByUrl('/homepage/creaEvento');
        }
      });
    }
  }

  ngAfterViewInit(): void {
    this.map = this.mapService.initMap(this.map);
  }

  //Inizializza i campi del form
  resetForm(): void {
    this.eventoId = this.router.url.split('/homepage/creaEvento/')[1];
    if (this.eventoId !== null && this.eventoId !== undefined && this.eventoId !== '') {
      this.compilaCampi(this.campiIniziali);
      return;

    } else {
      this.titolo = '';
      this.descrizione = '';
      this.dataInizio = new Date();
      this.dataFine = new Date();
      this.nomeLuogo = '';
    }
    this.toastr.info('Dati resettati');
  }

  creaOModificaEvento(): void {

    this.eventoId = this.router.url.split('/homepage/creaEvento/')[1];
    if (this.eventoId !== null && this.eventoId !== undefined && this.eventoId !== '') {
      this.eventService.modificaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLat(), this.mapService.getCurrentLng(), this.nomeLuogo, this.eventoId).subscribe({
        next: (res: CreaModificaEventoResponse) => {
          this.toastr.success(res.message);
          if (localStorage.getItem('ruolo') == 'ADMIN') {
            this.router.navigateByUrl('homepage/esplora');
          } else {
            this.router.navigateByUrl('homepage/eventiOrganizzati');
          }
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
          this.toastr.error(err.error.message);
        }
      });
      return;

    } else {
      if (!this.mapService.getCurrentLat() || !this.mapService.getCurrentLng()) {
        this.toastr.warning('Selezionare prima un punto sulla mappa');
        return;
      }
      //TODO controllare che tutti i campi siano stati compilati (con bordo rosso)
      if (this.ruolo === 'ORGANIZZATORE') {
        this.eventService.creaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLat(), this.mapService.getCurrentLng(), this.nomeLuogo).subscribe({
          next: (res: CreaModificaEventoResponse) => {
            this.toastr.success(res.message);
            if (localStorage.getItem('ruolo') == 'ADMIN') {
              this.router.navigateByUrl('homepage/esplora');
            } else {
              this.router.navigateByUrl('homepage/eventiOrganizzati');
            }
          },
          error: (err: HttpErrorResponse) => {
            this.toastr.error(err.error.message);
            console.log(err);
          }
        });
      } else {
        this.eventService.adminCreaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLat(), this.mapService.getCurrentLng(), this.nomeLuogo, this.usernameOrganizzatore).subscribe({
          next: (res: CreaModificaEventoResponse) => {
            this.toastr.success(res.message);
            if (localStorage.getItem('ruolo') == 'ADMIN') {
              this.router.navigateByUrl('homepage/esplora');
            } else {
              this.router.navigateByUrl('homepage/eventiOrganizzati');
            }
          },
          error: (err: HttpErrorResponse) => {
            this.toastr.error(err.error.message);
            console.log(err);
          }
        });
      }
    }
  }

  compilaCampi(res: GetEventoByIdResponse) {
    this.titolo = res.titolo;
    this.descrizione = res.descrizione;
    this.dataInizio = res.dataInizio;
    this.dataFine = res.dataFine;
    this.nomeLuogo = res.nomeLuogo;
    this.map = this.mapService.addMarker(this.map, +res.lat, +res.lng);
  }
}
