import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomepageComponent } from './components/homepage/homepage.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { EventsComponent } from './components/events/events.component';
import { AccountComponent } from './components/account/account.component';
import { EsploraComponent } from './components/esplora/esplora.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { CreaEventoComponent } from './components/crea-evento/crea-evento.component';
import { OrganizzatoriComponent } from './components/organizzatori/organizzatori.component';
import { IscrizioneEventiComponent } from './components/iscrizione-eventi/iscrizione-eventi.component';
import { RecoveryPasswordComponent } from './components/recovery-password/recovery-password.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomepageComponent,
    NavbarComponent,
    EventsComponent,
    AccountComponent,
    EsploraComponent,
    AdminPageComponent,
    CreaEventoComponent,
    OrganizzatoriComponent,
    IscrizioneEventiComponent,
    RecoveryPasswordComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
