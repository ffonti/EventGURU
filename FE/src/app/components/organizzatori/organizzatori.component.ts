import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetOrganizzatoriSeguiti } from 'src/app/dtos/response/GetOrganizzatoriSeguiti';
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
  protected usernameOrganizzatoriSeguiti: string[] = [];
  protected username?: string = localStorage.getItem('username')?.toString().toLowerCase().trim();

  constructor(private toastr: ToastrService, private userService: UserService, private router: Router) { }

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
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })

    this.userService.getAllOrganizzatoriSeguiti().subscribe({
      next: (res: GetOrganizzatoriSeguiti[]) => {
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

  resetFiltri(): void {
    this.cercaPerNome = '';
  }

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

  onChangeModoOrdine(value: string): void {
    this.organizzatori = this.organizzatori.reverse();
  }

  checkFilters(organizzatore: any): boolean {
    return organizzatore.nome.toLowerCase().trim().includes(this.cercaPerNome.toLowerCase().trim());
  }

  goToEventiOrganizzatore(organizzatoreId: number): void {
    this.router.navigateByUrl('homepage/esplora/' + organizzatoreId.toString().trim());
  }

  seguiOrganizzatore(organizzatoreId: number, organizzatoreUsername: string): void {
    this.userService.seguiOrganizzatore(organizzatoreId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
        this.usernameOrganizzatoriSeguiti.push(organizzatoreUsername);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

  smettiSeguireOrganizzatore(organizzatoreId: number): void {
    this.userService.seguiOrganizzatore(organizzatoreId.toString().trim()).subscribe({
      next: (res: any) => {
        this.toastr.success(res.message);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }

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
