import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-recovery-password',
  templateUrl: './recovery-password.component.html',
  styleUrls: ['./recovery-password.component.css']
})
export class RecoveryPasswordComponent {
  protected email: string = '';

  constructor(private toastr: ToastrService, private authService: AuthService, private router: Router) { }

  inviaEmail(): void {
    this.authService.recuperaPassword(this.email).subscribe({
      next: (res: any) => {
        this.toastr.success('Password modificata con successo');
        this.toastr.info('Controllare la mail per accedere con la nuova password');
        this.router.navigateByUrl('/login');
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error.message);
      }
    })
  }
}
