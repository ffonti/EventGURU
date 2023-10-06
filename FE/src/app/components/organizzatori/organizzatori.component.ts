import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetOrganizzatoriSeguiti } from 'src/app/dtos/response/GetOrganizzatoriSeguiti';
import { OrganizzatoreResponse } from 'src/app/dtos/response/OrganizzatoreResponse';
import { UserService } from 'src/app/services/user.service';

/**
 * componente per visualizzare tutti gli organizzatori. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-organizzatori',
  templateUrl: './organizzatori.component.html',
  styleUrls: ['./organizzatori.component.css']
})
export class OrganizzatoriComponent implements OnInit {
  protected organizzatori: any[] = [];
  protected cercaPerNome: string = '';
  protected modoOrdine: string = '';
  protected attributoOrdine: string = '';
  protected usernameOrganizzatoriSeguiti: string[] = [];
  protected username?: string = localStorage.getItem('username')?.toString().toLowerCase().trim();

  //costruttore dove istanzio le classi con cui interagire
  constructor(private toastr: ToastrService, private userService: UserService, private router: Router) { }

  //metodo eseguito appena viene caricato il componente
  ngOnInit(): void {
    //prendo tutti gli organizzatori presenti sul db
    this.userService.getAllOrganizzatori().subscribe({
      next: (res: OrganizzatoreResponse[]) => {
        this.organizzatori = res;
        let giorno: string, mese: string, anno: string, ore: string, minuti: string;
        this.organizzatori.forEach(organizzatore => {

          //cambio la visualizzazione della data
          let dataCreazioneAccount: string = organizzatore.dataCreazioneAccount.toString();

          anno = dataCreazioneAccount.slice(0, 4);
          mese = dataCreazioneAccount.slice(5, 7);
          giorno = dataCreazioneAccount.slice(8, 10);
          ore = dataCreazioneAccount.slice(11, 13);
          minuti = dataCreazioneAccount.slice(14, 16);

          organizzatore.dataCreazioneAccount = giorno + '/' + mese + '/' + anno + ' ' + ore + ':' + minuti;
        });
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })

    //prendo dal db tutti gli organizzatori che segue il turista loggato
    this.userService.getAllOrganizzatoriSeguiti().subscribe({
      next: (res: GetOrganizzatoriSeguiti[]) => {
        //aggiungo all'array tutti gli username degli organizzatori seguiti
        res.forEach((username: any) => {
          this.usernameOrganizzatoriSeguiti.push(username.usernameOrganizzatore);
        });
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }

  //resetto l'attributo per filtrare la ricerca
  resetFiltri(): void {
    this.cercaPerNome = '';
  }

  //cambio l'ordine dei dati in base da determinati attributi
  onChangeOrdinaPer(value: string): void {
    switch (value) {
      case 'NOME':
        this.organizzatori =
          this.organizzatori.sort(
            (a: any, b: any) => {
              if (a.nome.toLowerCase() > b.nome.toLowerCase()) return 1;
              if (a.nome.toLowerCase() < b.nome.toLowerCase()) return -1;
              return 0;
            });
        break;
      case 'COGNOME':
        this.organizzatori =
          this.organizzatori.sort(
            (a: any, b: any) => {
              if (a.cognome.toLowerCase() > b.cognome.toLowerCase()) return 1;
              if (a.cognome.toLowerCase() < b.cognome.toLowerCase()) return -1;
              return 0;
            });
        break;
      case 'EVENTI':
        this.organizzatori =
          this.organizzatori.sort(
            (a: any, b: any) => {
              if (a.numeroEventiOrganizzati > b.numeroEventiOrganizzati) return 1;
              if (a.numeroEventiOrganizzati < b.numeroEventiOrganizzati) return -1;
              return 0;
            });
        break;
    }
    this.modoOrdine = 'CRESCENTE';
  }

  //da crescente a decrescente o viceversa
  onChangeModoOrdine(value: string): void {
    this.organizzatori = this.organizzatori.reverse();
  }

  //controllo gli attributi per filtrare la ricerca
  checkFilters(organizzatore: any): boolean {
    return organizzatore.nome.toLowerCase().trim().includes(this.cercaPerNome.toLowerCase().trim());
  }

  //indirizzo l'utente alla pagina degli eventi di un dato organizzatore
  goToEventiOrganizzatore(organizzatoreId: number): void {
    this.router.navigateByUrl('homepage/esplora/' + organizzatoreId.toString().trim());
  }

  //metodo per seguire un organizzatore
  seguiOrganizzatore(organizzatoreId: number, organizzatoreUsername: string): void {
    this.userService.seguiOrganizzatore(organizzatoreId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
        //aggiungo l'username all'array di organizzatori seguiti
        this.usernameOrganizzatoriSeguiti.push(organizzatoreUsername);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

  //metodo per smettere di seguire un organi
  smettiSeguireOrganizzatore(organizzatoreId: number, organizzatoreUsername: string): void {
    this.userService.smettiSeguireOrganizzatore(organizzatoreId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
        //rimuovo l'username dalla lista di organizzatori seguiti
        this.usernameOrganizzatoriSeguiti = this.usernameOrganizzatoriSeguiti.filter((username) => {
          return username != organizzatoreUsername;
        });
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

  //controllo se un turista segue già un dato organizzatore tramite l'useranme
  checkGiaSegue(usernameOrganizzatore: string): boolean {
    let giaSegue: boolean = false;

    this.usernameOrganizzatoriSeguiti.forEach((username: string) => {
      if (username == usernameOrganizzatore) {
        giaSegue = true;
      }
    });

    return !giaSegue;
  }
}
