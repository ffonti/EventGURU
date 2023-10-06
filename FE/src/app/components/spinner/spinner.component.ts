import { Component } from '@angular/core';
import { SpinnerService } from 'src/app/services/spinner.service';

/**
 * componente per gestire lo spinner durante il caricamento.
 */
@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent {
  protected showSpinner: boolean = false;

  //mi iscrivo all'observer dello spinner e lo visualizzo solo quando lo stato è 'start'
  constructor(private spinnerService: SpinnerService) {
    this.spinnerService.getSpinnerObserver().subscribe((status: string) => {
      this.showSpinner = status == 'start';
    });
  }
}
