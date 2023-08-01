import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { GetAllResponse } from 'src/app/dtos/response/GetAllResponse';
import { OrganizzatoreService } from 'src/app/services/organizzatore.service';
import { TuristaService } from 'src/app/services/turista.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent {
  protected usernameList: string[] = [];
  protected showUsersList: boolean = false;

  constructor(private turistaService: TuristaService, private organizzatoreService: OrganizzatoreService, private router: Router) { }

  getAllTuristi(): void {
    //chiamo il backend per prendere tutti gli utenti con ruolo turista
    this.turistaService.getAllTurista().subscribe({
      next: (res: GetAllResponse[]) => {
        this.compilaUsersList(res);
        this.toggleModalUsersList();
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
        this.compilaUsersList(res);
        this.toggleModalUsersList();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  toggleModalUsersList(): void {
    this.showUsersList = !this.showUsersList;
  }

  compilaUsersList(res: GetAllResponse[]): void {
    this.usernameList = [];
    for (const user of res) {
      this.usernameList.push(user.username);
    }
  }

  goToAccountPage(username: string): void {
    this.router.navigateByUrl('homepage/account/' + username);
  }
}
