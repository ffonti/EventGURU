import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GetUserDataResponse } from 'src/app/dtos/response/GetUserDataResponse';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  userData: any = {
    username: '',
    nome: '',
    cognome: '',
    password: '',
    email: '',
    vecchia_password: '',
  };
  ripeti_password: string = '';


  protected showPassword: boolean = false;
  protected showVecchiaPassword: boolean = false;
  protected showRepeatPassword: boolean = false;

  constructor(private userService: UserService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.userService.getUserData().subscribe({
      next: (res: GetUserDataResponse) => {
        console.log(res);

        this.userData.nome = res.nome;
        this.userData.cognome = res.cognome;
        this.userData.email = res.email;
        this.userData.username = res.username;
        console.log(this.userData);


      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error('Eseguire nuovamente il login');
        this.router.navigateByUrl('login');
      }
    })
  }

  toggleShowPassword(type: string): void {
    if (type === 'repeat') {
      this.showRepeatPassword = !this.showRepeatPassword;
      return;
    } else if (type === 'no-repeat') {
      this.showPassword = !this.showPassword;
      return;
    } else if (type === 'vecchia') {
      this.showVecchiaPassword = !this.showVecchiaPassword;
    }
  }

  ripetiPasswordNotOk(): boolean {
    return (this.userData.password !== this.ripeti_password);
  }

  updateUserData(): void {
    this.userService.updateUserData(this.userData);
  }

  vecchiaENuovaPasswordUguali(): boolean {
    return (this.userData.vecchia_password === this.userData.password);
  }
}
