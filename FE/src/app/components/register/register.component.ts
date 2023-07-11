import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

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
    });
  }

  register(): void {
    if (!this.checkForm(this.registerForm)) return;

    const nome: string = this.registerForm.controls['nome'].value;
    const cognome: string = this.registerForm.controls['cognome'].value;
    const email: string = this.registerForm.controls['email'].value;
    const username: string = this.registerForm.controls['username'].value;
    const password: string = this.registerForm.controls['password'].value;

    this.authService.register(nome, cognome, email, username, password).subscribe({
      next: (res: HttpResponse<any>) => {
        this.toastr.success('Registrazione completata!');
        this.router.navigateByUrl('/login');
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error(err.error);
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
