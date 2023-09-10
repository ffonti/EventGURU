import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { EventService } from 'src/app/services/event.service';

@Component({
  selector: 'app-iscrizione-eventi',
  templateUrl: './iscrizione-eventi.component.html',
  styleUrls: ['./iscrizione-eventi.component.css']
})
export class IscrizioneEventiComponent implements OnInit {
  protected usernameTurista: string = '';

  constructor(private toastr: ToastrService, private eventService: EventService) { }

  ngOnInit(): void {
    this.usernameTurista = localStorage.getItem('username')?.toString().trim().toLowerCase() || '';

    this.eventService.getEventiByTurista(this.usernameTurista).subscribe({
      next: (res: any) => {
        console.log(res);
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
        console.log(err);
      }
    })
  }
}
