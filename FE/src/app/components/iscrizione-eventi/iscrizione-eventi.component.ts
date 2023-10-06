import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllEventiByOrganizzatoreResponse } from 'src/app/dtos/response/GetAllEventiByOrganizzatoreResponse';
import { EventService } from 'src/app/services/event.service';
import { RecensioneService } from 'src/app/services/recensione.service';

/**
 * componente con cui iscriversi agli eventi. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-iscrizione-eventi',
  templateUrl: './iscrizione-eventi.component.html',
  styleUrls: ['./iscrizione-eventi.component.css']
})
export class IscrizioneEventiComponent implements OnInit {
  protected allEventi: GetAllEventiByOrganizzatoreResponse[] = [];
  protected allEventiWithDateFormatted: any = [];
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
  protected rating: number = 0;
  protected usernameRecensioni: string[] = [];

  //costruttore dove istanzio le classi con cui interagire
  constructor(private toastr: ToastrService,
    private eventService: EventService,
    private router: Router,
    private recensioneService: RecensioneService) { }

  //Metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    //prendo i dati dell'utente loggato
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
    this.username = localStorage.getItem('username')?.toString().trim().toLowerCase() || '';

    //prendo tutti gli eventi a cui è iscritto un turista
    this.eventService.getEventiByTurista(this.username).subscribe({
      next: (res: GetAllEventiByOrganizzatoreResponse[]) => {
        res.forEach(evento => {
          this.allEventi.push(evento);
        });

        this.allEventiWithDateFormatted = JSON.parse(JSON.stringify(this.allEventi));

        //cambio il formato delle date per renderle più leggibili
        this.changeFormatDate(this.allEventiWithDateFormatted);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error('Errore nella visualizzazione degli eventi');
        this.router.navigateByUrl('homepage/admin');
      }
    });
  }

  //cambio il formato delle date per renderle più leggibili
  changeFormatDate(eventi: any): void {
    let giorno: string, mese: string, anno: string, ore: string, minuti: string;

    eventi.forEach((evento: any) => {
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

  //cambio la visualizzazione della modale
  toggleModalEliminaEvento(eventoId: number): void {
    this.eventoIdDaEliminare = eventoId;
    this.showModalEliminaEvento = !this.showModalEliminaEvento;
  }

  //chiamo il backend per eliminare l'evento con un dato id
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

  //rimuovo un evento dalla visualizzazione dopo essere stato eliminato
  rimuoviEventoDaArray(eventoId: number): void {
    this.allEventi = this.allEventi.filter((evento: GetAllEventiByOrganizzatoreResponse) => {
      // return +evento.eventoId !== +eventoId;
    });

    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.filter((evento: any) => {
      return +evento.eventoId !== +eventoId;
    });
  }

  //controllo i campi con cui filtrare la visualizzazione degli eventi
  checkFilters(evento: GetAllEventiByOrganizzatoreResponse): boolean {
    return evento.titolo.toLowerCase().trim().includes(this.cercaPerTitolo.toLowerCase().trim()) &&
      evento.nomeLuogo.toLowerCase().trim().includes(this.cercaPerLuogo.toLowerCase().trim()) &&
      (evento.stato.toString().trim().toUpperCase() == this.cercaPerStato.toString().trim().toUpperCase() ||
        this.cercaPerStato.toString().toLowerCase() == '');
  }

  //inizializzo i campi in modo da resettare i filtri
  resetFiltri(): void {
    this.cercaPerLuogo = '';
    this.cercaPerTitolo = '';
    this.cercaPerStato = '';
  }

  //ordino la visualizzazione degli eventi in base a vari parametri
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

  //cambio da crescente a decrescente, o viceversa
  onChangeModoOrdine(): void {
    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.reverse();
  }

  //indirizzo l'utente alla pagina in cui modificare l'evento
  modificaEvento(eventoId: number): void {
    this.router.navigateByUrl('/homepage/creaEvento/' + eventoId.toString().trim());
  }

  //metodo per iscrivere l'utente a un evento
  iscrizioneEvento(eventoId: number): void {
    //aggiungo l'username del turista all'array con gli iscritti
    this.allEventiWithDateFormatted.forEach((evento: any) => {
      if (evento.eventoId == eventoId && this.username) {
        evento.usernameTuristi.push(this.username);
      }
    });

    //iscrivo l'utente all'evento
    this.eventService.iscrizioneEvento(+eventoId).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  //metodo per annullare l'iscrizione all'evento
  annullaIscrizione(eventoId: number): void {
    //rimuovo l'username dalla lista dell'evento
    this.allEventiWithDateFormatted.forEach((evento: any) => {
      if (evento.eventoId == eventoId) {
        evento.usernameTuristi.pop(this.username);
      }
    });

    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.filter((evento: any) => {
      return evento.eventoId != eventoId;
    })

    //annullo l'iscrizione
    this.eventService.annullaIscrizione(+eventoId).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  //cambio la visualizzazione della modale
  toggleModalRecensione(eventoId: number): void {
    this.rating = 0;
    this.testoRecensione = '';
    this.eventoIdDaRecensire = eventoId;
    this.showModalRecensione = !this.showModalRecensione;
  }

  //metodo per inviare la recensione
  inviaRecensione(): void {
    //controllo sul voto
    if (this.rating === 0) {
      this.toastr.warning('Inserire prima un voto');
      return;
    }

    //mando la recensione al backend
    this.recensioneService.inviaRecensione(this.eventoIdDaRecensire.toString().trim(), this.rating, this.testoRecensione).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    });

    this.showModalRecensione = !this.showModalRecensione;
  }

  //assegno alla variabile il voto inserito dall'utente
  doRating(n: number): void {
    this.rating = n;
  }

  //controllo se l'evento è già stato recensito
  //TODO fixarlo
  nonAncoraRecensito(eventoId: number): boolean {
    let isRecensito: boolean = false;
    const usernameLogged: string | null = localStorage.getItem('username');
    const evento = this.allEventiWithDateFormatted.filter((evento: any) => {
      return evento.eventoId == eventoId;
    })[0];

    let usernameRecensioniFilter: any = [];

    evento.recensioni.forEach((recensione: any) => {
      usernameRecensioniFilter.push(recensione.usernameTurista);
    });

    if (usernameLogged) {
      usernameRecensioniFilter.forEach((username: string) => {
        if (usernameLogged == username) isRecensito = true;
      });
    }
    return !isRecensito;
  }
}
