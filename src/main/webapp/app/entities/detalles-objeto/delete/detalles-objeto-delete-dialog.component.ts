import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetallesObjeto } from '../detalles-objeto.model';
import { DetallesObjetoService } from '../service/detalles-objeto.service';

@Component({
  templateUrl: './detalles-objeto-delete-dialog.component.html',
})
export class DetallesObjetoDeleteDialogComponent {
  detallesObjeto?: IDetallesObjeto;

  constructor(protected detallesObjetoService: DetallesObjetoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detallesObjetoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
