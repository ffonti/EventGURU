import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { EventsComponent } from './components/events/events.component';
import { AccountComponent } from './components/account/account.component';
import { EsploraComponent } from './components/esplora/esplora.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { CreaEventoComponent } from './components/crea-evento/crea-evento.component';
import { OrganizzatoriComponent } from './components/organizzatori/organizzatori.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'homepage', component: HomepageComponent, children: [
      { path: '', redirectTo: '/homepage/esplora', pathMatch: 'full' },
      { path: 'esplora', component: EsploraComponent },
      { path: 'eventi', component: EventsComponent },
      { path: 'admin', component: AdminPageComponent },
      { path: 'creaEvento', component: CreaEventoComponent },
      { path: 'organizzatori', component: OrganizzatoriComponent },
      {
        path: 'account', component: AccountComponent, children: [
          { path: ':username', component: AccountComponent }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
