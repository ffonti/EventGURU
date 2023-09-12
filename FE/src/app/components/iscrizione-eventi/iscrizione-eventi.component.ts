import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllEventiByOrganizzatoreResponse } from 'src/app/dtos/response/GetAllEventiByOrganizzatoreResponse';
import { EventService } from 'src/app/services/event.service';

@Component({
  selector: 'app-iscrizione-eventi',
  templateUrl: './iscrizione-eventi.component.html',
  styleUrls: ['./iscrizione-eventi.component.css']
})
export class IscrizioneEventiComponent implements OnInit {
  protected allEventi: GetAllEventiByOrganizzatoreResponse[] = [];
  protected allEventiWithDateFormatted: any[] = [];
  protected showModalEliminaEvento: boolean = false;
  protected eventoIdDaEliminare: number = 0;
  protected cercaPerTitolo: string = '';
  protected cercaPerLuogo: string = '';
  protected cercaPerStato: string = '';
  protected modoOrdine: string = '';
  protected attributoOrdine: string = '';
  protected ruolo: string | undefined = '';
  protected username: string = '';
  protected showModalRecensione: boolean = false;
  protected eventoIdDaRecensire: number = 0;
  protected testoRecensione: string = '';

  constructor(private toastr: ToastrService, private eventService: EventService, private router: Router) { }

  ngOnInit(): void {
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
    this.username = localStorage.getItem('username')?.toString().trim().toLowerCase() || '';

    this.eventService.getEventiByTurista(this.username).subscribe({
      next: (res: GetAllEventiByOrganizzatoreResponse[]) => {
        res.forEach(evento => {
          this.allEventi.push(evento);
        });

        this.allEventiWithDateFormatted = JSON.parse(JSON.stringify(this.allEventi));

        this.changeFormatDate(this.allEventiWithDateFormatted);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error('Errore nella visualizzazione degli eventi');
        this.router.navigateByUrl('homepage/admin');
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

  toggleModalEliminaEvento(eventoId: number): void {
    this.eventoIdDaEliminare = eventoId;
    this.showModalEliminaEvento = !this.showModalEliminaEvento;
  }

  eliminaEvento(): void {
    this.eventService.eliminaEvento(this.eventoIdDaEliminare).subscribe({
      next: (res: any) => {
        this.rimuoviEventoDaArray(this.eventoIdDaEliminare);
        this.eventoIdDaEliminare = 0;
        this.showModalEliminaEvento = !this.showModalEliminaEvento;
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

  rimuoviEventoDaArray(eventoId: number): void {
    this.allEventi = this.allEventi.filter((evento: GetAllEventiByOrganizzatoreResponse) => {
      // return +evento.eventoId !== +eventoId;
    });

    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.filter((evento: any) => {
      return +evento.eventoId !== +eventoId;
    });
  }

  checkFilters(evento: GetAllEventiByOrganizzatoreResponse): boolean {
    return evento.titolo.toLowerCase().trim().includes(this.cercaPerTitolo.toLowerCase().trim()) &&
      evento.nomeLuogo.toLowerCase().trim().includes(this.cercaPerLuogo.toLowerCase().trim()) &&
      (evento.stato.toString().trim().toUpperCase() == this.cercaPerStato.toString().trim().toUpperCase() ||
        this.cercaPerStato.toString().toLowerCase() == '');
  }

  resetFiltri(): void {
    this.cercaPerLuogo = '';
    this.cercaPerTitolo = '';
    this.cercaPerStato = '';
  }

  onChangeOrdinaPer(value: string): void {
    switch (value) {
      case 'DATA':
        this.allEventiWithDateFormatted =
          this.allEventiWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.dataInizio > b.dataInizio) return 1;
              if (a.dataInizio < b.dataInizio) return -1;
              return 0;
            });
        break;
      case 'TITOLO':
        this.allEventiWithDateFormatted =
          this.allEventiWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.titolo.toLowerCase() > b.titolo.toLowerCase()) return 1;
              if (a.titolo.toLowerCase() < b.titolo.toLowerCase()) return -1;
              return 0;
            });
        break;
      case 'LUOGO':
        this.allEventiWithDateFormatted =
          this.allEventiWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.nomeLuogo.toLowerCase() > b.nomeLuogo.toLowerCase()) return 1;
              if (a.nomeLuogo.toLowerCase() < b.nomeLuogo.toLowerCase()) return -1;
              return 0;
            });
        break;
    }
    this.modoOrdine = 'CRESCENTE';
  }

  onChangeModoOrdine(): void {
    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.reverse();
  }

  modificaEvento(eventoId: number): void {
    this.router.navigateByUrl('/homepage/creaEvento/' + eventoId.toString().trim());
  }

  iscrizioneEvento(eventoId: number): void {
    this.allEventiWithDateFormatted.forEach((evento) => {
      if (evento.eventoId == eventoId && this.username) {
        evento.usernameTuristi.push(this.username);
      }
    });

    this.eventService.iscrizioneEvento(eventoId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  annullaIscrizione(eventoId: number): void {
    this.allEventiWithDateFormatted.forEach((evento) => {
      if (evento.eventoId == eventoId) {
        evento.usernameTuristi.pop(this.username);
      }
    });

    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.filter((evento: any) => {
      return evento.eventoId != eventoId;
    })

    console.log(this.allEventiWithDateFormatted);

    this.eventService.annullaIscrizione(eventoId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  toggleModalRecensione(eventoId: number): void {
    this.eventoIdDaRecensire = eventoId;
    this.showModalRecensione = !this.showModalRecensione;
  }
}
