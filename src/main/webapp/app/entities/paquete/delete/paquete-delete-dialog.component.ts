import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

@Component({
  templateUrl: './paquete-delete-dialog.component.html',
})
export class PaqueteDeleteDialogComponent {
  paquete?: IPaquete;

  constructor(protected paqueteService: PaqueteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paqueteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
