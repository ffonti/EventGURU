import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetEventoByIdResponse } from 'src/app/dtos/response/GetEventoByIdResponse';
import { MessageResponse } from 'src/app/dtos/response/MessageResponse';
import { EventService } from 'src/app/services/event.service';
import { MapService } from 'src/app/services/map.service';
import { SpinnerService } from 'src/app/services/spinner.service';

/**
 * componente con form e mappa per creare o modificare un evento. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-crea-evento',
  templateUrl: './crea-evento.component.html',
  styleUrls: ['./crea-evento.component.css']
})
export class CreaEventoComponent implements OnInit, AfterViewInit {
  protected titolo: string = '';
  protected descrizione: string = '';
  protected nomeLuogo: string = '';
  protected dataInizio: Date = new Date();
  protected dataFine: Date = new Date();
  protected eventoId: string = '';
  private campiIniziali!: GetEventoByIdResponse;
  protected ruolo: string | undefined = '';
  protected usernameOrganizzatore: string = '';
  private mapMarker: any;

  //costruttore dove istanzio le classi con cui interagire
  constructor(
    private toastr: ToastrService,
    private eventService: EventService,
    private router: Router,
    private mapService: MapService,
    private spinnerService: SpinnerService) { }

  //Metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    //prendo il ruolo dell'utente loggato
    this.ruolo = localStorage.getItem('ruolo')?.toString().trim().toUpperCase();

    //prendo l'id dell'evento dall'url
    this.eventoId = this.router.url.split('/homepage/creaEvento/')[1];

    //se esiste l'id, prendo i dati dell'evento
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

  //chiamo il service per inizializzare la mappa
  ngAfterViewInit(): void {
    this.mapMarker = this.mapService.initMapMarker(this.mapMarker);
  }

  //Inizializzo i campi del form
  resetForm(): void {
    this.eventoId = this.router.url.split('/homepage/creaEvento/')[1];
    //se esiste l'id nell'url, compilo i campi con i dati dell'evento già creato
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

    //se esiste l'id nel path vuol dire che si stanno modificando i dati di un evento
    if (this.eventoId !== null && this.eventoId !== undefined && this.eventoId !== '') {

      //se è un organizzatore che modifica il proprio evento chiamo un determinato endpoint
      if (this.ruolo === 'ORGANIZZATORE') {
        this.eventService.modificaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLatMarker(), this.mapService.getCurrentLngMarker(), this.nomeLuogo, this.eventoId).subscribe({
          next: (res: MessageResponse) => {
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
        //se è l'admin che modifica un evento di un dato organizzatore chiamo un altro endpoint
      } else {
        this.eventService.adminModificaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLatMarker(), this.mapService.getCurrentLngMarker(), this.nomeLuogo, this.eventoId, this.usernameOrganizzatore).subscribe({
          next: (res: MessageResponse) => {
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
      }


      //se non esiste l'id nel path vuol dire che si sta creando un nuovo evento
    } else {
      if (!this.mapService.getCurrentLatMarker() || !this.mapService.getCurrentLngMarker()) {
        this.toastr.warning('Selezionare prima un punto sulla mappa');
        return;
      }

      //se è l'organizzatore che sta creando un proprio evento chiamo un endpoint
      if (this.ruolo === 'ORGANIZZATORE') {
        this.eventService.creaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLatMarker(), this.mapService.getCurrentLngMarker(), this.nomeLuogo).subscribe({
          next: (res: MessageResponse) => {
            this.spinnerService.requestEnded();
            this.toastr.success(res.message);
            if (localStorage.getItem('ruolo') == 'ADMIN') {
              this.router.navigateByUrl('homepage/esplora');
            } else {
              this.router.navigateByUrl('homepage/eventiOrganizzati');
            }
          },
          error: (err: HttpErrorResponse) => {
            this.spinnerService.resetSpinner();
            this.toastr.error(err.error.message);
            console.log(err);
          }
        });
        return;
        //se è un admin che crea un evento a nome di un organizzatore chiamo un altro endopint
      } else {
        this.eventService.adminCreaEvento(this.titolo, this.descrizione, this.dataInizio, this.dataFine, this.mapService.getCurrentLatMarker(), this.mapService.getCurrentLngMarker(), this.nomeLuogo, this.usernameOrganizzatore).subscribe({
          next: (res: MessageResponse) => {
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

  //metodo per compilare i campi del form dato un oggetto con tutti i dati
  compilaCampi(res: GetEventoByIdResponse) {
    this.titolo = res.titolo;
    this.descrizione = res.descrizione;
    this.dataInizio = res.dataInizio;
    this.dataFine = res.dataFine;
    this.nomeLuogo = res.nomeLuogo;
    this.mapMarker = this.mapService.addMarker(this.mapMarker, +res.lat, +res.lng);
    this.usernameOrganizzatore = res.usernameOrganizzatore;
  }
}
