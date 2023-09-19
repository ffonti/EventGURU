import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { OrganizzatoreResponse } from 'src/app/dtos/response/OrganizzatoreResponse';
import { UserService } from 'src/app/services/user.service';

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
  protected showModalEventiOrganizzatore: boolean = false;

  constructor(private toastr: ToastrService, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getAllOrganizzatori().subscribe({
      next: (res: OrganizzatoreResponse[]) => {
        this.organizzatori = res;
        let giorno: string, mese: string, anno: string, ore: string, minuti: string;
        this.organizzatori.forEach(organizzatore => {
          let dataCreazioneAccount: string = organizzatore.dataCreazioneAccount.toString();

          anno = dataCreazioneAccount.slice(0, 4);
          mese = dataCreazioneAccount.slice(5, 7);
          giorno = dataCreazioneAccount.slice(8, 10);
          ore = dataCreazioneAccount.slice(11, 13);
          minuti = dataCreazioneAccount.slice(14, 16);

          organizzatore.dataCreazioneAccount = giorno + '/' + mese + '/' + anno + ' ' + ore + ':' + minuti;
        });
        console.log(this.organizzatori);

      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }

  resetFiltri(): void {
    this.cercaPerNome = '';
  }

  onChangeOrdinaPer(value: string) {
    switch (value) {
      case 'NOME':
        this.organizzatori =
          this.organizzatori.sort(
            (a: any, b: any) => {
              if (a.nometoLowerCase() > b.nometoLowerCase()) return 1;
              if (a.nometoLowerCase() < b.nometoLowerCase()) return -1;
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

  onChangeModoOrdine(value: string) {
    this.organizzatori = this.organizzatori.reverse();
  }

  checkFilters(organizzatore: any): boolean {
    return organizzatore.nome.toLowerCase().trim().includes(this.cercaPerNome.toLowerCase().trim());
  }

  toggleModalEventiOrganizzatore(organizzatoreId: number) {
    this.showModalEventiOrganizzatore = !this.showModalEventiOrganizzatore;
    console.log(organizzatoreId);
    
  }
}
