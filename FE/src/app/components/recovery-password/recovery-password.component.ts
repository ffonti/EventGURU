import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MessageResponse } from 'src/app/dtos/response/MessageResponse';
import { AuthService } from 'src/app/services/auth.service';
import { SpinnerService } from 'src/app/services/spinner.service';

/**
 * componente per recuperare la password. implementa OnInit, un'interfaccia
 * che espone un metodo che viene eseguito non appena il componente viene visualizzato.
 */
@Component({
  selector: 'app-recovery-password',
  templateUrl: './recovery-password.component.html',
  styleUrls: ['./recovery-password.component.css']
})
export class RecoveryPasswordComponent {
  protected email: string = '';

  //costruttore dove istanzio le classi con cui interagire
  constructor(private toastr: ToastrService,
    private authService: AuthService,
    private router: Router,
    private spinnerService: SpinnerService) { }

  //metodo per inviare la mail per recuperare la password
  inviaEmail(): void {
    if (this.email === '') {
      this.toastr.warning('Inserire prima una mail');
      return;
    }

    //chiamo il backend per effettuare il recupero password
    this.authService.recuperaPassword(this.email).subscribe({
      next: (res: MessageResponse) => {
        this.spinnerService.requestEnded();
        this.toastr.success('Password modificata con successo');
        this.toastr.info('Controllare la mail per accedere con la nuova password');
        this.router.navigateByUrl('/login');
      },
      error: (err: HttpErrorResponse) => {
        this.spinnerService.resetSpinner();
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }
}
