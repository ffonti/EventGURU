import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-crea-evento',
  templateUrl: './crea-evento.component.html',
  styleUrls: ['./crea-evento.component.css']
})
export class CreaEventoComponent {
  protected titolo: string = '';
  protected descrizione: string = '';
  protected dataInizio: string = '';
  protected dataFine: string = '';

  constructor(private toastr: ToastrService) { }

  //Inizializza i campi del form
  resetForm(): void {
    this.titolo = '';
    this.descrizione = '';
    this.dataInizio = '';
    this.dataFine = '';
    this.toastr.info('Dati resettati');
  }

  creaEvento(): void {
    //TODO controllare che tutti i campi siano stati compilati (con bordo rosso)
    console.log(this.titolo, this.descrizione, this.dataInizio, this.dataFine);
  }
}
