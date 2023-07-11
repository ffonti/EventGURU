import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { EventsComponent } from './components/events/events.component';
import { AccountComponent } from './components/account/account.component';
import { EsploraComponent } from './components/esplora/esplora.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'homepage', component: HomepageComponent, children: [
      { path: '', redirectTo: '/homepage/esplora', pathMatch: 'full' },
      { path: 'esplora', component: EsploraComponent },
      { path: 'eventi', component: EventsComponent },
      { path: 'account', component: AccountComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
