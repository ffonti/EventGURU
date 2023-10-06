import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetAllEventiByOrganizzatoreResponse } from 'src/app/dtos/response/GetAllEventiByOrganizzatoreResponse';
import { GetAllEventiResponse } from 'src/app/dtos/response/GetAllEventiResponse';
import { MessageResponse } from 'src/app/dtos/response/MessageResponse';
import { EventService } from 'src/app/services/event.service';
import { MapService } from 'src/app/services/map.service';

/**
 * componente dove visuaizzare le card con gli eventi. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-esplora',
  templateUrl: './esplora.component.html',
  styleUrls: ['./esplora.component.css']
})
export class EsploraComponent implements OnInit, AfterViewInit {
  protected allEventi: GetAllEventiResponse[] = [];
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
  protected showMappaFiltro: boolean = true;
  protected organizzatoreId: string = '';
  protected pathId: boolean = false;
  protected allMarkerCoordinates: GetAllEventiResponse[] = [];
  protected allEventiFiltered: GetAllEventiResponse[] = [];
  protected allEventiWithDateFormattedFiltered: any[] = [];
  protected temp: any = [];
  protected eventoIdSelected: number = 0;
  protected allEventiByOrganizzatore: GetAllEventiByOrganizzatoreResponse[] = [];
  protected showModalPartecipantiNoRemove: boolean = false;
  protected showModalPartecipanti: boolean = false;
  protected usernamePartecipanti: string[] = [];

  mapDraw: any;

  //costruttore dove istanzio le classi con cui interagire
  constructor(private eventService: EventService, private toastr: ToastrService, private router: Router, private mapService: MapService) { }

  //Metodo eseguito appena viene caricato il componente
  ngOnInit(): void {

    //prendo ruolo e username dell'utente loggato
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();
    this.username = localStorage.getItem('username')?.toString().trim().toLowerCase() || '';

    this.organizzatoreId = this.router.url.split('/homepage/esplora/')[1];
    //se esiste l'id nell'url vuol dire che è un orgaizzatore quindi prendo dal server tutti gli eventi di quell'organizzatore
    if (this.organizzatoreId !== null && this.organizzatoreId !== undefined && this.organizzatoreId !== '') {
      this.pathId = true;
      this.eventService.getEventiByOrganizzatoreId(this.organizzatoreId.toString().trim()).subscribe({
        next: (res: GetAllEventiByOrganizzatoreResponse[]) => {
          res.forEach(evento => {
            if (evento.stato == 'FUTURO') {
              this.allEventi.push(evento);
            }
          });

          this.allEventiWithDateFormatted = JSON.parse(JSON.stringify(this.allEventi));

          //cambio il formato delle date per renderle esteticamente più leggibili
          this.changeFormatDate(this.allEventiWithDateFormatted);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
          this.toastr.error(err.error.message);
          this.router.navigateByUrl('/homepage/organizzatori');
        }
      });
      return;
    }

    //metodo per i turisti o admin, vengono presi dal server tutti gli eventi futuri per i turisti o tutti gli eventi sul db per gli admin
    this.eventService.getAllEventi().subscribe({
      next: (res: GetAllEventiResponse[]) => {
        res.forEach(evento => {
          if ((this.ruolo == 'TURISTA' && evento.stato == 'FUTURO') || this.ruolo == 'ADMIN') {
            this.allEventi.push(evento);
          }
        });

        this.allEventiWithDateFormatted = JSON.parse(JSON.stringify(this.allEventi));

        //cambio il formato delle date per renderle esteticamente più leggibili
        this.changeFormatDate(this.allEventiWithDateFormatted);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error('Errore nella visualizzazione degli eventi');
        this.router.navigateByUrl('homepage/admin');
      }
    });

    //prendo tutte le coordinate degli eventi
    this.mapService.getAllMarkerCoordinates().subscribe({
      next: (res: GetAllEventiResponse[]) => {
        this.allMarkerCoordinates = res;
        //assegno un marker alla per ogni coordinata
        this.mapDraw = this.mapService.placeMarkers(this.mapDraw, this.allMarkerCoordinates);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  //inizializzo la mappa
  ngAfterViewInit(): void {
    this.showMappaFiltro = false;
    this.mapDraw = this.mapService.initMapDraw(this.mapDraw);
  }

  //"gioco" con le stringhe per rendere le date più leggibili sul browser
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

  //cambio la visualizzazione della modale per eliminare un dato evento
  toggleModalEliminaEvento(eventoId: number): void {
    this.eventoIdDaEliminare = eventoId;
    this.showModalEliminaEvento = !this.showModalEliminaEvento;
  }

  //metodo per eliminare un evento
  eliminaEvento(): void {
    //chiamo il backend passandogli l'id dell'evento da eliminare
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

  //rimuovo dalla visualizzazione l'evento eliminato
  rimuoviEventoDaArray(eventoId: number): void {
    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.filter((evento: any) => {
      return +evento.eventoId !== +eventoId;
    });
  }

  //controllo i campi per filtrare gli eventi
  checkFilters(evento: GetAllEventiResponse): boolean {
    return evento.titolo.toLowerCase().trim().includes(this.cercaPerTitolo.toLowerCase().trim()) &&
      evento.nomeLuogo.toLowerCase().trim().includes(this.cercaPerLuogo.toLowerCase().trim()) &&
      (evento.stato.toString().trim().toUpperCase() == this.cercaPerStato.toString().trim().toUpperCase() ||
        this.cercaPerStato.toString().toLowerCase() == '');
  }

  //azzero i campi per filtrare gli eventi
  resetFiltri(): void {
    this.cercaPerLuogo = '';
    this.cercaPerTitolo = '';
    this.cercaPerStato = '';
    this.allEventiWithDateFormatted = JSON.parse(JSON.stringify(this.temp));
  }

  //ordino gli eventi in base a determinati parametri
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

  //passo da crescente a decrescente
  onChangeModoOrdine(): void {
    this.allEventiWithDateFormatted = this.allEventiWithDateFormatted.reverse();
  }

  //vado nella pagina per modificare un evento, con i dati già compilati
  modificaEvento(eventoId: number): void {
    this.router.navigateByUrl('/homepage/creaEvento/' + eventoId.toString().trim());
  }

  //metodo per far iscrivere un turista ad un evento
  iscrizioneEvento(eventoId: number): void {
    //aggiungo l'username del turista appena iscritto alla lista
    this.allEventiWithDateFormatted.forEach((evento) => {
      if (evento.eventoId == eventoId && this.username) {
        evento.usernameTuristi.push(this.username);
      }
    });

    //iscrivo il turista ad un evento
    this.eventService.iscrizioneEvento(+eventoId).subscribe({
      next: (res: MessageResponse) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  //metodo per annullare l'iscrizione di un turista ad un dato evento
  annullaIscrizione(eventoId: number): void {

    //rimuovo l'username del turista disiscritto dalla lista nell'evento
    this.allEventiWithDateFormatted.forEach((evento) => {
      if (evento.eventoId == eventoId && this.username) {
        evento.usernameTuristi = evento.usernameTuristi.filter((username: string) => {
          return username !== this.username;
        });
      }
    });

    //chiamo il server per annullare l'iscrizione
    this.eventService.annullaIscrizione(+eventoId).subscribe({
      next: (res: MessageResponse) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    });
  }

  //cambio la visualizzazione della modale
  toggleMappaFiltro(): void {
    this.showMappaFiltro = !this.showMappaFiltro;
    this.mapDraw = this.mapService.removeLayers(this.mapDraw);
  }

  //metodo per filtrare gli eventi e visualizzarli con le date corrette
  filtraEventi(): void {
    this.allEventiFiltered = this.mapService.markersAggiornati();
    this.showMappaFiltro = !this.showMappaFiltro;

    this.allEventiWithDateFormattedFiltered = JSON.parse(JSON.stringify(this.allEventiFiltered));
    this.changeFormatDate(this.allEventiWithDateFormattedFiltered);

    this.temp = JSON.parse(JSON.stringify(this.allEventiWithDateFormatted));

    this.allEventiWithDateFormatted = this.allEventiWithDateFormattedFiltered;
  }

  //cambio la visualizzazione della modale
  toggleModalPartecipanti(eventoId: number): void {
    this.eventoIdSelected = eventoId;
    this.allEventiWithDateFormatted.forEach((evento) => {
      if (evento.eventoId == eventoId) {
        this.usernamePartecipanti = evento.usernameTuristi;
      }
    });

    this.showModalPartecipanti = !this.showModalPartecipanti;
  }

  //cambio la visualizzazione della modale
  toggleModalPartecipantiNoRemove(eventoId: number): void {
    this.eventoIdSelected = eventoId;
    this.allEventiByOrganizzatore.forEach((evento) => {
      if (evento.eventoId == eventoId) {
        this.usernamePartecipanti = evento.usernameTuristi;
      }
    })
    this.showModalPartecipantiNoRemove = !this.showModalPartecipantiNoRemove;
  }

  //rimuovo un turista da un dato evento
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
}