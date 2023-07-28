import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Ruolo } from 'src/app/types/Ruolo';
import { RegisterRequest } from 'src/app/dtos/request/RegisterRequest';
import { RegisterResponse } from 'src/app/dtos/response/RegisterResponse';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  protected registerForm!: FormGroup;

  protected password: string = '';
  protected ripeti_password: string = '';

  protected showPassword: boolean = false;
  protected showRepeatPassword: boolean = false;

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      nome: new FormControl('', [Validators.required]),
      cognome: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      ripeti_password: new FormControl('', [Validators.required]),
      ruolo: new FormControl('', [Validators.required])
    });
  }

  register(): void {
    if (!this.checkForm(this.registerForm)) return;

    const nome: string = this.registerForm.controls['nome'].value;
    const cognome: string = this.registerForm.controls['cognome'].value;
    const email: string = this.registerForm.controls['email'].value;
    const username: string = this.registerForm.controls['username'].value;
    const password: string = this.registerForm.controls['password'].value;
    const ruolo: Ruolo = this.registerForm.controls['ruolo'].value == "TURISTA" ? Ruolo.TURISTA : Ruolo.ORGANIZZATORE;

    const request: RegisterRequest = { nome, cognome, email, username, password, ruolo };

    this.authService.register(request).subscribe({
      next: (res: RegisterResponse) => {
        this.toastr.success(res.message);
        this.router.navigateByUrl('/login');
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error(err.message);
      },
    });
  }

  checkForm(form: FormGroup): boolean {
    return true;
  }

  toggleShowPassword(type: string): void {
    if (type === 'repeat') {
      this.showRepeatPassword = !this.showRepeatPassword;
      return;
    } else if (type === 'no-repeat') {
      this.showPassword = !this.showPassword;
      return;
    }
  }

  emptyAndTouched(campo: string): boolean {
    return (this.registerForm.controls[campo].value === '' && this.registerForm.controls[campo].touched);
  }

  ripetiPasswordNotOk(): boolean {
    return (this.password !== this.ripeti_password && this.registerForm.controls['ripeti_password'].touched);
  }
}
