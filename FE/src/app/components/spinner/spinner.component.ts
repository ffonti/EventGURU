import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SpinnerService } from 'src/app/services/spinner.service';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent {
  protected showSpinner: boolean = false;

  constructor(private spinnerService: SpinnerService) {
    this.spinnerService.getSpinnerObserver().subscribe((status: string) => {
      this.showSpinner = status == 'start';
    });
  }
}
