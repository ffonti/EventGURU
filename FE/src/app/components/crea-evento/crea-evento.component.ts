import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CreaEventoResponse } from 'src/app/dtos/response/CreaEventoResponse';
import { EventService } from 'src/app/services/event.service';

@Component({
  selector: 'app-crea-evento',
  templateUrl: './crea-evento.component.html',
  styleUrls: ['./crea-evento.component.css']
})
export class CreaEventoComponent {
  protected titolo: string = '';
  protected descrizione: string = '';
  protected dataInizio: Date = new Date();
  protected dataFine: Date = new Date();

  constructor(private toastr: ToastrService, private eventService: EventService, private router: Router) { }

  //Inizializza i campi del form
  resetForm(): void {
    this.titolo = '';
    this.descrizione = '';
    this.dataInizio = new Date();
    this.dataFine = new Date();
    this.toastr.info('Dati resettati');
  }

  creaEvento(): void {
    //TODO controllare che tutti i campi siano stati compilati (con bordo rosso)
    this.eventService.creaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine).subscribe({
      next: (res: CreaEventoResponse) => {
        this.toastr.success(res.message);
        // this.router.navigateByUrl('homepage/eventiOrganizzati');
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }
}
