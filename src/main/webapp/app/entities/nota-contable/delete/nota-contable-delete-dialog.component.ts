import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotaContable } from '../nota-contable.model';
import { NotaContableService } from '../service/nota-contable.service';

@Component({
  templateUrl: './nota-contable-delete-dialog.component.html',
})
export class NotaContableDeleteDialogComponent {
  notaContable?: INotaContable;

  constructor(protected notaContableService: NotaContableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notaContableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
