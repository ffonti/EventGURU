import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  protected loginForm!: FormGroup;

  protected password: string = '';

  protected showPassword: boolean = false;

  constructor(private authService: AuthService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    localStorage.clear();

    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  login(): void {
    if (!this.checkForm(this.loginForm)) return;

    const username: string = this.loginForm.controls['username'].value;
    this.password = this.loginForm.controls['password'].value;

    this.authService.login(username, this.password).subscribe({
      next: (res: any) => {
        console.log(res);

        this.toastr.success(res.body.message);
        localStorage.setItem('token', res.body.jwt);
        localStorage.setItem('id', res.body.user.userId);
        localStorage.setItem('nome', res.body.user.nome);
        localStorage.setItem('cognome', res.body.user.cognome);
        localStorage.setItem('username', res.body.user.username);
        localStorage.setItem('ruolo', res.body.user.ruolo);
        localStorage.setItem('email', res.body.user.email);
        localStorage.setItem('iscrittoNewsletter', res.body.user.iscrittoNewsletter);
        this.router.navigateByUrl('homepage');
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.toastr.error("Errore nel login");
      },
    });
  }

  checkForm(form: FormGroup): boolean {
    return true;
  }

  toggleShowPassword(): void {
    this.showPassword = !this.showPassword;
    return;
  }

  emptyAndTouched(campo: string): boolean {
    return (this.loginForm.controls[campo].value === '' && this.loginForm.controls[campo].touched);
  }
}
