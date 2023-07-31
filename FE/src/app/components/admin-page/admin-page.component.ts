import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { GetAllResponse } from 'src/app/dtos/response/GetAllResponse';
import { OrganizzatoreService } from 'src/app/services/organizzatore.service';
import { TuristaService } from 'src/app/services/turista.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent {

  constructor(private turistaService: TuristaService, private organizzatoreService: OrganizzatoreService) { }

  getAllTuristi(): void {
    //chiamo il backend per prendere tutti gli utenti con ruolo turista
    this.turistaService.getAllTurista().subscribe({
      next: (res: GetAllResponse[]) => {
        console.log(res);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  getAllOrganizzatori(): void {
    //chiamo il backend per prendere tutti gli utenti con ruolo organizzatore
    this.organizzatoreService.getAllOrganizzatore().subscribe({
      next: (res: GetAllResponse[]) => {
        console.log(res);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }
}
