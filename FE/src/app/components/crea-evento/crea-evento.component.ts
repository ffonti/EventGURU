import { HttpErrorResponse } from '@angular/common/http';
import { Component, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CreaEventoResponse } from 'src/app/dtos/response/CreaEventoResponse';
import { EventService } from 'src/app/services/event.service';
import { MapService } from 'src/app/services/map.service';

@Component({
  selector: 'app-crea-evento',
  templateUrl: './crea-evento.component.html',
  styleUrls: ['./crea-evento.component.css']
})
export class CreaEventoComponent implements AfterViewInit {
  private map: any;
  protected titolo: string = '';
  protected descrizione: string = '';
  protected nomeLuogo: string = '';
  protected dataInizio: Date = new Date();
  protected dataFine: Date = new Date();

  constructor(
    private toastr: ToastrService,
    private eventService: EventService,
    private router: Router,
    private mapService: MapService) { }

  ngAfterViewInit(): void {
    this.map = this.mapService.initMap(this.map);
  }

  //Inizializza i campi del form
  resetForm(): void {
    this.titolo = '';
    this.descrizione = '';
    this.dataInizio = new Date();
    this.dataFine = new Date();
    this.toastr.info('Dati resettati');
  }

  creaEvento(): void {
    if (!this.mapService.getCurrentLat() || !this.mapService.getCurrentLng()) {
      this.toastr.warning('Selezionare prima un punto sulla mappa');
      return;
    }

    //TODO controllare che tutti i campi siano stati compilati (con bordo rosso)
    this.eventService.creaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLat(), this.mapService.getCurrentLng(), this.nomeLuogo).subscribe({
      next: (res: CreaEventoResponse) => {
        this.toastr.success(res.message);
        this.router.navigateByUrl('homepage/eventiOrganizzati');
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }
}
