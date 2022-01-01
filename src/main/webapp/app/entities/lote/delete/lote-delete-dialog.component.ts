import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILote } from '../lote.model';
import { LoteService } from '../service/lote.service';

@Component({
  templateUrl: './lote-delete-dialog.component.html',
})
export class LoteDeleteDialogComponent {
  lote?: ILote;

  constructor(protected loteService: LoteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
