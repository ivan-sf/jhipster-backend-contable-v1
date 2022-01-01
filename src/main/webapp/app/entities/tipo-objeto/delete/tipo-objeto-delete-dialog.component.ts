import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoObjeto } from '../tipo-objeto.model';
import { TipoObjetoService } from '../service/tipo-objeto.service';

@Component({
  templateUrl: './tipo-objeto-delete-dialog.component.html',
})
export class TipoObjetoDeleteDialogComponent {
  tipoObjeto?: ITipoObjeto;

  constructor(protected tipoObjetoService: TipoObjetoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoObjetoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
