import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVencimiento } from '../vencimiento.model';
import { VencimientoService } from '../service/vencimiento.service';

@Component({
  templateUrl: './vencimiento-delete-dialog.component.html',
})
export class VencimientoDeleteDialogComponent {
  vencimiento?: IVencimiento;

  constructor(protected vencimientoService: VencimientoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vencimientoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
