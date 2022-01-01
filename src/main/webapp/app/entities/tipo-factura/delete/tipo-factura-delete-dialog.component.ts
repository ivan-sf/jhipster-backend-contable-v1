import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoFactura } from '../tipo-factura.model';
import { TipoFacturaService } from '../service/tipo-factura.service';

@Component({
  templateUrl: './tipo-factura-delete-dialog.component.html',
})
export class TipoFacturaDeleteDialogComponent {
  tipoFactura?: ITipoFactura;

  constructor(protected tipoFacturaService: TipoFacturaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoFacturaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
