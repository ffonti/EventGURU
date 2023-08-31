import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllEventiByOrganizzatoreResponse } from 'src/app/dtos/response/GetAllEventiByOrganizzatoreResponse';
import { EventService } from 'src/app/services/event.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {
  protected allEventiByOrganizzatore: GetAllEventiByOrganizzatoreResponse[] = [];
  protected allEventiByOrganizzatoreWithDateFormatted: any[] = [];
  protected showModalEliminaEvento: boolean = false;

  constructor(private eventService: EventService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.eventService.getEventiByOrganizzatore().subscribe({
      next: (res: GetAllEventiByOrganizzatoreResponse[]) => {
        res.forEach(evento => {
          this.allEventiByOrganizzatore.push(evento);
        });

        this.allEventiByOrganizzatoreWithDateFormatted = JSON.parse(JSON.stringify(this.allEventiByOrganizzatore));

        this.changeFormatDate(this.allEventiByOrganizzatoreWithDateFormatted);
        console.log(this.allEventiByOrganizzatore);
        console.log(this.allEventiByOrganizzatoreWithDateFormatted);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error('Errore nella visualizzazione degli eventi');
        this.router.navigateByUrl('homepage/creaEvento');
      }
    });
  }

  changeFormatDate(eventi: any[]): void {
    let giorno: string, mese: string, anno: string, ore: string, minuti: string;

    eventi.forEach(evento => {
      let dataInizio: string = evento.dataInizio.toString();

      anno = dataInizio.slice(0, 4);
      mese = dataInizio.slice(5, 7);
      giorno = dataInizio.slice(8, 10);
      ore = dataInizio.slice(11, 13);
      minuti = dataInizio.slice(14, 16);

      evento.dataInizio = giorno + '/' + mese + '/' + anno + ' ' + ore + ':' + minuti;

      let dataFine: string = evento.dataFine.toString();

      anno = dataFine.slice(0, 4);
      mese = dataFine.slice(5, 7);
      giorno = dataFine.slice(8, 10);
      ore = dataFine.slice(11, 13);
      minuti = dataFine.slice(14, 16);

      evento.dataFine = giorno + '/' + mese + '/' + anno + ' ' + ore + ':' + minuti;
    });
  }

  toggleModalEliminaEvento(): void {
    this.showModalEliminaEvento = !this.showModalEliminaEvento;
  }

  eliminaEvento(eventoId: number): void {
    console.log('elimino evento id ' + eventoId);
  }
}
