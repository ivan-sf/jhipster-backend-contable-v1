import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaService } from '../service/detalle-factura.service';

@Component({
  templateUrl: './detalle-factura-delete-dialog.component.html',
})
export class DetalleFacturaDeleteDialogComponent {
  detalleFactura?: IDetalleFactura;

  constructor(protected detalleFacturaService: DetalleFacturaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detalleFacturaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
