import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllEventiByOrganizzatoreResponse } from 'src/app/dtos/response/GetAllEventiByOrganizzatoreResponse';
import { EventService } from 'src/app/services/event.service';
import { RecensioneService } from 'src/app/services/recensione.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {
  protected allEventiByOrganizzatore: GetAllEventiByOrganizzatoreResponse[] = [];
  protected allEventiByOrganizzatoreWithDateFormatted: any[] = [];
  protected showModalEliminaEvento: boolean = false;
  protected eventoIdDaEliminare: number = 0;
  protected cercaPerTitolo: string = '';
  protected cercaPerLuogo: string = '';
  protected cercaPerStato: string = '';
  protected modoOrdine: string = '';
  protected attributoOrdine: string = '';
  protected showModalPartecipanti: boolean = false;
  protected usernamePartecipanti: string[] = [];
  protected eventoIdSelected: number = 0;
  protected showModalPartecipantiNoRemove: boolean = false;
  protected showModalRecensioni: boolean = false;

  constructor(private eventService: EventService,
    private toastr: ToastrService,
    private router: Router,
    private recensioneService: RecensioneService) { }

  ngOnInit(): void {
    this.eventService.getEventiByOrganizzatore().subscribe({
      next: (res: GetAllEventiByOrganizzatoreResponse[]) => {
        res.forEach(evento => {
          this.allEventiByOrganizzatore.push(evento);
        });

        this.allEventiByOrganizzatoreWithDateFormatted = JSON.parse(JSON.stringify(this.allEventiByOrganizzatore));

        this.changeFormatDate(this.allEventiByOrganizzatoreWithDateFormatted);
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
    this.allEventiByOrganizzatore = this.allEventiByOrganizzatore.filter((evento: GetAllEventiByOrganizzatoreResponse) => {
      return +evento.eventoId !== +eventoId;
    });

    this.allEventiByOrganizzatoreWithDateFormatted = this.allEventiByOrganizzatoreWithDateFormatted.filter((evento: any) => {
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

  onChangeOrdinaPer(value: string) {
    switch (value) {
      case 'DATA':
        this.allEventiByOrganizzatoreWithDateFormatted =
          this.allEventiByOrganizzatoreWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.dataInizio > b.dataInizio) return 1;
              if (a.dataInizio < b.dataInizio) return -1;
              return 0;
            });
        break;
      case 'TITOLO':
        this.allEventiByOrganizzatoreWithDateFormatted =
          this.allEventiByOrganizzatoreWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.titolo.toLowerCase() > b.titolo.toLowerCase()) return 1;
              if (a.titolo.toLowerCase() < b.titolo.toLowerCase()) return -1;
              return 0;
            });
        break;
      case 'LUOGO':
        this.allEventiByOrganizzatoreWithDateFormatted =
          this.allEventiByOrganizzatoreWithDateFormatted.sort(
            (a: any, b: any) => {
              if (a.nomeLuogo.toLowerCase() > b.nomeLuogo.toLowerCase()) return 1;
              if (a.nomeLuogo.toLowerCase() < b.nomeLuogo.toLowerCase()) return -1;
              return 0;
            });
        break;
    }
    this.modoOrdine = 'CRESCENTE';
  }

  onChangeModoOrdine(value: string) {
    this.allEventiByOrganizzatoreWithDateFormatted = this.allEventiByOrganizzatoreWithDateFormatted.reverse();
  }

  modificaEvento(eventoId: number) {
    this.router.navigateByUrl('/homepage/creaEvento/' + eventoId.toString().trim());
  }

  toggleModalPartecipanti(eventoId: number): void {
    this.eventoIdSelected = eventoId;
    this.allEventiByOrganizzatore.forEach((evento) => {
      if (evento.eventoId == eventoId) {
        this.usernamePartecipanti = evento.usernameTuristi;
      }
    })
    this.showModalPartecipanti = !this.showModalPartecipanti;
  }

  toggleModalPartecipantiNoRemove(eventoId: number): void {
    this.eventoIdSelected = eventoId;
    this.allEventiByOrganizzatore.forEach((evento) => {
      if (evento.eventoId == eventoId) {
        this.usernamePartecipanti = evento.usernameTuristi;
      }
    })
    this.showModalPartecipantiNoRemove = !this.showModalPartecipantiNoRemove;
  }

  rimuoviTuristaDaEvento(usernameTurista: string): void {
    this.eventService.rimuoviTuristaDaEvento(usernameTurista, this.eventoIdSelected.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);

        this.usernamePartecipanti = this.usernamePartecipanti.filter((username) => {
          return username != usernameTurista;
        });

        this.allEventiByOrganizzatore.forEach((evento) => {
          if (evento.eventoId == this.eventoIdSelected) {
            evento.usernameTuristi = evento.usernameTuristi.filter((username) => {
              return username != usernameTurista;
            });
          }
        });
      },
      error: (err: HttpErrorResponse) => {
        this.showModalPartecipanti = false;
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }

  toggleModalRecensioni(eventoId: number): void {
    this.showModalRecensioni = !this.showModalRecensioni;
    if (this.showModalRecensioni) {
      this.recensioneService.getRecensioniByEvento(eventoId.toString().trim()).subscribe({
        next: (res: any) => {
          console.log(res);
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message);
          console.log(err);
          this.showModalRecensioni = !this.showModalRecensioni;
        }
      })
    }
  }
}
